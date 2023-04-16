package kodlama.io.rentacar.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String cardNumber; // kart numarası
    private String cardHolder; // kart sahibi
    private int cardExpirationYear; // son kullanma yılı
    private int cardExpirationMonth; // son kullanma ayı
    private String cardCvv;
    private double balance;
}
