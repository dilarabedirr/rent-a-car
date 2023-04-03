package kodlama.io.rentacar.business.dto.responses.get;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllMaintenancesResponse {
    private int id;
    private Date startDate;
    private Date dueDate;
    private double maintenanceCost;
    private int carId;
}
