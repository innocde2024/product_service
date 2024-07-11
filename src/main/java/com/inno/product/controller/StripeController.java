package com.inno.product.controller;

import com.inno.product.entity.OrderDTO;
import com.inno.product.entity.UserDTO;
import com.inno.product.exception.ApiRequestException;
import com.inno.product.model.Order;
import com.inno.product.model.OrderItem;
import com.inno.product.service.auth.AuthService;
import com.inno.product.service.order.OrderService;
import com.inno.product.service.orderItem.OrderItemService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class StripeController {
    @Value("${stripe.apiKey}")
    private String stripeApiKey;
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }
    @Value("${allow.origin}")
    private String path;
    @Value("${signing.secret}")
    private String SIGNING_SECRET;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private AuthService authService;

    @PostMapping("/payment/charge")
    public String createPaymentUrl(@RequestBody OrderDTO orderDTO, @RequestHeader("Authorization") String token) {
        UserDTO userDTO = authService.verifyToken(token);
        try {
            String currency = "vnd";
            Map<String, String> metadata = new HashMap<>();
            metadata.put("userId", String.valueOf(userDTO.getId()));
            List<OrderItem> orderItems = orderDTO.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                metadata.put(String.valueOf(orderItem.getProduct().getId()), String.valueOf(orderItem.getQuantity()));
            }
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(path+"/payment/result/success")
                    .setCancelUrl(path+"/payment/result")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(currency)
                                                    .setUnitAmountDecimal(BigDecimal.valueOf(orderItemService.calculatePrice(orderDTO.getOrderItems())))
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("ALL PRODUCTS")
                                                                    .build())
                                                    .build())
                                    .build())
                    .putAllMetadata(metadata)
                    .build();
            Session session = Session.create(params);
            return session.getUrl();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiRequestException("payment_fail", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/payment/webhook")
    public ResponseEntity<String> handleWebhookEvent(
            @RequestBody String payload, HttpServletRequest request) {
        Event event = null;
        String header = request.getHeader("Stripe-Signature");
        try {
            event = Webhook.constructEvent(payload, header, SIGNING_SECRET);
            if (event.getType().equals("checkout.session.completed")) {
                @SuppressWarnings("deprecation")
                Session session = (Session) event.getData().getObject();
                String content = session.getPaymentStatus();
                String userId = session.getMetadata().get("userId");
                Order order = new Order();
                order.setUserId(Integer.parseInt(userId));
                if("paid".equalsIgnoreCase(content)) {
                    order.setPaymentStatus(true);
                }
                order.setOrderDate(new Date());

                Map<String, String> productQuantityMap = session.getMetadata().entrySet().stream()
                        .filter(entry -> !entry.getKey().equals("userId"))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                // In ra productQuantityMap
                Order orderNew = orderService.addOrder(order);
                List<OrderItem> orderItems = new ArrayList<>();
                productQuantityMap.forEach((key, value) -> orderItems.add(orderItemService.createOrderItemByProductIdAndQuantity(Integer.parseInt(key),Integer.parseInt(value),orderNew)));
                order.setOrderItems(orderItems);
            }

        } catch (SignatureVerificationException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Webhook received successfully");
    }
}
