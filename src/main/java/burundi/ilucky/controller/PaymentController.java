package burundi.ilucky.controller;

import burundi.ilucky.model.User;
import burundi.ilucky.service.PaymentService;
import burundi.ilucky.service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;
    @Autowired
    private UserService userService;

    private static final String successUrl = "http://localhost:3456/payment_success";

    private static final String cancelUrl = "http://localhost:3456/payment_cancel";

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Tạo thanh toán
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> request) {
        try {
            BigDecimal total = new BigDecimal((request.get("total").toString()));
            String currency = request.get("currency").toString();
            String description = request.get("description").toString();

            Payment payment = paymentService.createPayment(
                    total, currency, "paypal", "sale", description, cancelUrl, successUrl);

            for (Links link : payment.getLinks()) {
                System.out.println("Link Rel ne: " + link.getRel() + ", URL: " + link.getHref());
                if (link.getRel().equals("approval_url")) {
                    Map<String, String> response = new HashMap<>();
                    response.put("approvalUrl", link.getHref());
                    return ResponseEntity.ok(response);
                }
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unable to create payment approval URL"));
        } catch (PayPalRESTException | NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Xử lý thành công
    @GetMapping("/success/{total}")
    public ResponseEntity<?> executePayment(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("payerId") String payerId,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long total) {
        try {
            Payment payment = paymentService.executePayment(paymentId, payerId);

            // Chuẩn bị phản hồi khi thành công
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Payment executed successfully.");
            response.put("paymentId", paymentId);
            response.put("payerId", payerId);
            response.put("paymentDetails", payment.toJSON());
            // lấy thông tin user và lưu số tiền vừa mới nộp vào dữ liệu người chơi
            User user = userService.findByUserName(userDetails.getUsername());
            userService.depositMoney(user,total);
            return ResponseEntity.ok(response);
        } catch (PayPalRESTException e) {
            // Chuẩn bị phản hồi khi thất bại
            Map<String, Object> response = new HashMap<>();
            response.put("status", "failed");
            response.put("message", "Payment execution failed.");
            response.put("error", e.getDetails());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    // Xử lý hủy bỏ
    @GetMapping("/cancel")
    public ResponseEntity<?> cancelPayment() {
        return ResponseEntity.ok(Map.of(
                "status", "canceled",
                "message", "Payment was canceled by user"
        ));
    }
}
