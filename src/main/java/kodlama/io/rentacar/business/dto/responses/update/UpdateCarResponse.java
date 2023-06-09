package kodlama.io.rentacar.business.dto.responses.update;

import kodlama.io.rentacar.entities.enums.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateCarResponse {
    private int id;
    private String plate;
    private double dailyPrice;
    private State state;
    private int modelId;
    private int modelYear;
}
