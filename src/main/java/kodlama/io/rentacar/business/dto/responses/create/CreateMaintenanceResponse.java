package kodlama.io.rentacar.business.dto.responses.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateMaintenanceResponse {
    private int id;
    private Date startDate;
    private Date dueDate;
    private double maintenanceCost;
    private int carId;
}
