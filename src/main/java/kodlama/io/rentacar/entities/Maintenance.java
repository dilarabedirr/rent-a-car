package kodlama.io.rentacar.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "maintenances")
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime startDate;// bakıma alındığı tarih
    private LocalDateTime dueDate;// bakımın bittiği tarih
    private String information; // neden bakıma girdi
    private boolean isCompleted; //bakım tamamlandı mı tamamlanmadı mı
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
}
//private double maintenanceCost;//ayrı service olur faturalama için
