package kodlama.io.rentacar.business.dto.responses.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateMaintenanceResponse {
    private int id;
    private int carId;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private String information;
    private boolean isCompleted;
}
