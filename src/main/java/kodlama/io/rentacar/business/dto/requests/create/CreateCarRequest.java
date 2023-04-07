package kodlama.io.rentacar.business.dto.requests.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateCarRequest {
    private int modelId;
    private int modelYear;
    private String plate;
    private double dailyPrice;
    //private State state; metot içinde set edeceğiz.

}
