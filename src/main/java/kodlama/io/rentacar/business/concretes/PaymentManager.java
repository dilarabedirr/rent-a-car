package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.PosService;
import kodlama.io.rentacar.business.abstracts.PaymentService;
import kodlama.io.rentacar.business.dto.requests.create.CreatePaymentRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdatePaymentRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreatePaymentResponse;
import kodlama.io.rentacar.business.dto.responses.get.payment.GetAllPaymentsResponse;
import kodlama.io.rentacar.business.dto.responses.get.payment.GetPaymentResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdatePaymentResponse;
import kodlama.io.rentacar.common.dto.CreateRentalPaymentRequest;
import kodlama.io.rentacar.entities.Payment;
import kodlama.io.rentacar.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentManager implements PaymentService {
    private final PaymentRepository repository;
    private final ModelMapper mapper;
    private final PosService posService;

    @Override
    public List<GetAllPaymentsResponse> getAll() {
        List<Payment> payments = repository.findAll();
        List<GetAllPaymentsResponse> responses = payments.stream()
                .map(payment -> mapper.map(payment, GetAllPaymentsResponse.class)).toList();
        return responses;
    }

    @Override
    public GetPaymentResponse getById(int id) {
        checkIfPaymentExists(id);
        Payment payment = repository.findById(id).orElseThrow();
        GetPaymentResponse response = mapper.map(payment, GetPaymentResponse.class);
        return response;
    }

    @Override
    public CreatePaymentResponse add(CreatePaymentRequest request) {
        checkIfCardExists(request.getCardNumber());
        Payment payment = mapper.map(request, Payment.class);
        payment.setId(0);
        repository.save(payment);
        CreatePaymentResponse response = mapper.map(payment, CreatePaymentResponse.class);
        return response;
    }

    @Override
    public UpdatePaymentResponse update(int id, UpdatePaymentRequest request) {
        checkIfPaymentExists(id);
        Payment payment = mapper.map(request, Payment.class);
        payment.setId(id);
        repository.save(payment);
        UpdatePaymentResponse response = mapper.map(payment, UpdatePaymentResponse.class);
        return response;
    }

    @Override
    public void delete(int id) {
        checkIfPaymentExists(id);
        repository.deleteById(id);
    }

    @Override
    public void processRentalPayment(CreateRentalPaymentRequest request) {
        checkIfPaymentIsValid(request); //verilen kart bilgileri doğru mu
        Payment payment = repository.findByCardNumber(request.getCardNumber());
        checkIfBalanceIsEnough(payment.getBalance(), request.getPrice());// bakiye yeterli mi
        posService.pay(); // fake pos service
        payment.setBalance(payment.getBalance() - request.getPrice());
        repository.save(payment);
    }

    private void checkIfPaymentExists(int id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Ödeme bilgisi bulunamadı.");
        }
    }

    private void checkIfCardExists(String cardNumber) {
        if (repository.existsByCardNumber(cardNumber)) {
            throw new RuntimeException("kart numarası zaten kayıtlı");
        }
    }

    private void checkIfBalanceIsEnough(double balance, double price) {
        if (balance < price) {
            throw new RuntimeException("yetersiz bakiye");
        }
    }

    private void checkIfPaymentIsValid(CreateRentalPaymentRequest request) {
        // if'te değilini kullanmak ile ilgili : (!)
        // sorgudan gelen sonuç true olursa ödeme işlemini yapabiliriz.false gelirse hata vermesi lazım.
        // else kullanmamak için true geleni false yapıyoruz ki hata vermeden devam edebilsin.
        // false gelirse true yapıyoruz hata versin.
        if (!repository.existsByCardNumberAndCardHolderAndCardExpirationYearAndCardExpirationMonthAndCardCvv(
                request.getCardNumber(),
                request.getCardHolder(),
                request.getCardExpirationYear(),
                request.getCardExpirationMonth(),
                request.getCardCvv()
        )) {
            throw new RuntimeException("Kart bilgileri hatalı");
        }
    }
}
