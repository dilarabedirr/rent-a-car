package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.constants.Messages;
import kodlama.io.rentacar.core.exceptions.BusinessException;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.RentalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RentalBusinessRules {
    private final RentalRepository repository;

    public void checkIfRentalExists(int id) {
        if (!repository.existsById(id)) throw new BusinessException(Messages.Rental.NotExists);
    }

    public void checkIfCarIsRentedOrUnderMaintenance(State state) {
        if (state.equals(State.RENTED)) {
            throw new BusinessException(Messages.Car.CarRental);
        }
        if (state.equals(State.MAINTENANCE)) {
            throw new BusinessException(Messages.Car.CarMaintenance);
        }
    }

}
