package com.inno.product.controller;

import com.inno.product.exception.ApiRequestException;
import com.inno.product.model.Order;
import com.inno.product.model.OrderItem;
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
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/payment/charge")
    public String createPaymentUrl(@RequestBody Order orderGet) {
        Order order = orderService.getOrderById(orderGet.getOrderId());
        System.out.println(orderService.calculateQuantity(order));
        System.out.println(BigDecimal.valueOf(orderService.calculatePrice(order)));
        try {
            String currency = "vnd";
            Map<String, String> metadata = new HashMap<>();
            metadata.put("transactionId",order.getOrderId() + "");
            metadata.put("userId", String.valueOf(order.getUserId()));
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
                                                    .setUnitAmountDecimal(BigDecimal.valueOf(orderService.calculatePrice(order)))
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
//                String id = session.getId();
//                String email = session.getCustomerDetails().getEmail();
//                String userId = session.getMetadata().get("userId");
                String orderId = session.getMetadata().get("transactionId");
                Order order = orderService.getOrderById(Integer.parseInt(orderId));
                if("paid".equalsIgnoreCase(content)) {
                    order.setPaymentStatus(true);
                }
                orderService.addOrder(order);
            }

        } catch (SignatureVerificationException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Webhook received successfully");
    }
    @PostMapping("/payment/cancel")
    public ResponseEntity<?> handleCancelPayment(@RequestBody Order orderGet) {
        Order order = orderService.getOrderById(orderGet.getOrderId());
        List<OrderItem> orderItemList = orderItemService.getOrderItemListByOrderId(order.getOrderId());
        orderItemService.returnItems(orderItemList);
        return ResponseEntity.ok(orderItemList);
    }
}
