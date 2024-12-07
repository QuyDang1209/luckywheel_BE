package burundi.ilucky.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private final APIContext apiContext;

    public PaymentService(APIContext apiContext) {
        this.apiContext = apiContext;
    }

//    public Payment createPayment(Double total, String currency, String method,
//                                 String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
//        Amount amount = new Amount();
//        amount.setCurrency(currency);
//        amount.setTotal(String.format("%.2f", total));
//
//        Transaction transaction = new Transaction();
//        transaction.setDescription(description);
//        transaction.setAmount(amount);
//
//        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(transaction);
//
//        Payer payer = new Payer();
//        payer.setPaymentMethod(method);
//
//        Payment payment = new Payment();
//        payment.setIntent(intent);
//        payment.setPayer(payer);
//        payment.setTransactions(transactions);
//
//        RedirectUrls redirectUrls = new RedirectUrls();
//        redirectUrls.setCancelUrl(cancelUrl);
//        redirectUrls.setReturnUrl(successUrl);
//        payment.setRedirectUrls(redirectUrls);
//
//        return payment.create(apiContext);
//    }

    public Payment createPayment(BigDecimal total, String currency, String method,
                                 String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        try {
            // Đảm bảo rằng số tiền được làm tròn chính xác đến 2 chữ số thập phân
            BigDecimal formattedTotal = total.setScale(2, RoundingMode.HALF_UP);

            // Khởi tạo Amount và thiết lập tiền tệ và tổng số tiền
            Amount amount = new Amount();
            amount.setCurrency(currency);
            amount.setTotal(formattedTotal.toString());

            // Tạo Transaction và thiết lập mô tả và số tiền
            Transaction transaction = new Transaction();
            transaction.setDescription(description);
            transaction.setAmount(amount);

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            // Tạo Payer và thiết lập phương thức thanh toán
            Payer payer = new Payer();
            payer.setPaymentMethod(method);

            // Tạo Payment và thiết lập các thông tin liên quan
            Payment payment = new Payment();
            payment.setIntent(intent);
            payment.setPayer(payer);
            payment.setTransactions(transactions);

            // Thiết lập các URL chuyển hướng sau khi thanh toán
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl(cancelUrl);
            redirectUrls.setReturnUrl(successUrl);
            payment.setRedirectUrls(redirectUrls);

            // Tạo và trả về Payment
            return payment.create(apiContext);
        } catch (PayPalRESTException e) {
            System.err.println("Error occurred during payment creation: " + e.getDetails());
            throw e;
        }
    }
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecution);
    }
}
