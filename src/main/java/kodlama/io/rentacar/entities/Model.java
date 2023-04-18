package kodlama.io.rentacar.entities;

import jakarta.persistence.*;
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
@Table(name = "models")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "brand_id")//yazmasakta kolon bu isimde oluşur.
    private Brand brand;
    @OneToMany(mappedBy = "model")
    // ilişki sahibini belirliyoruz.modelin idsi araç içinde tutulduğu için burada ilişki sahibi model
    private List<Car> cars;
}
