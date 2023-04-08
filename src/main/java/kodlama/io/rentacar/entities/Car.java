package kodlama.io.rentacar.entities;

import jakarta.persistence.*;
import kodlama.io.rentacar.entities.enums.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int modelYear;
    private String plate;
    private double dailyPrice;
    @Enumerated(EnumType.STRING)
    private State state; // 1 - available, 2 - rented, 3 - maintance
    @ManyToOne
    @JoinColumn(name="model_id")
    private Model model;
    @OneToMany(mappedBy = "car")
    List<Maintenance> maintenances;
    @OneToMany(mappedBy = "car")
    List<Rental> rentals;
}
