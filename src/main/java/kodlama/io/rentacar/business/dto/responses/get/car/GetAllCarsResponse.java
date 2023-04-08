package kodlama.io.rentacar.business.dto.responses.get.car;

import kodlama.io.rentacar.entities.enums.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllCarsResponse {
    private int id;
    private String plate;
    private double dailyPrice;
    private State state;
    private int modelId;
    private int modelYear;
}

