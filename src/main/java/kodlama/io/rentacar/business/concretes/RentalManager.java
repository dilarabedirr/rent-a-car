package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.abstracts.RentalService;
import kodlama.io.rentacar.business.dto.requests.create.CreateRentalRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateRentalRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateRentalResponse;
import kodlama.io.rentacar.business.dto.responses.get.rental.GetAllRentalsResponse;
import kodlama.io.rentacar.business.dto.responses.get.rental.GetRentalResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateRentalResponse;
import kodlama.io.rentacar.entities.Rental;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.RentalRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RentalManager implements RentalService {
    private final RentalRepository repository;
    private final ModelMapper mapper;
    private final CarService carService;

    @Override
    public List<GetAllRentalsResponse> getAll() {
        List<Rental> rentals = repository.findAll();
        List<GetAllRentalsResponse> responses = rentals.stream()
                .map(rental -> mapper.map(rental, GetAllRentalsResponse.class)).toList();
        return responses;
    }

    @Override
    public GetRentalResponse getById(int id) {
        checkIfRentalExists(id);
        Rental rental = repository.findById(id).orElseThrow();
        GetRentalResponse response = mapper.map(rental, GetRentalResponse.class);
        return response;
    }

    @Override
    public CreateRentalResponse add(CreateRentalRequest request) {
        checkIfCarIsRentedOrUnderMaintenance(request.getCarId());
        Rental rental = mapper.map(request, Rental.class);
        rental.setId(0);
        rental.setStartDate(LocalDateTime.now());
        rental.setTotalPrice(getTotalPrice(rental));
        repository.save(rental);
        carService.changeState(request.getCarId(), State.RENTED);
        CreateRentalResponse response = mapper.map(rental, CreateRentalResponse.class);
        return response;
    }

    @Override
    public UpdateRentalResponse update(int id, UpdateRentalRequest request) {
        checkIfRentalExists(id);
        Rental rental=mapper.map(request,Rental.class);
        rental.setId(id);
        rental.setTotalPrice(getTotalPrice(rental));
        repository.save(rental);
        UpdateRentalResponse response=mapper.map(rental,UpdateRentalResponse.class);
        return response;
    }

    @Override
    public void delete(int id) {
        checkIfRentalExists(id);
        int carId = repository.findById(id).get().getCar().getId();
        carService.changeState(carId, State.AVAILABLE);
        repository.deleteById(id);
    }

    private void checkIfRentalExists(int id) {
        if (!repository.existsById(id)) throw new RuntimeException("Kiralama bilgisi mevcut değil.");
    }

    private double getTotalPrice(Rental rental) {
        return rental.getDailyPrice() * rental.getRentedForDays();
    }

    private void checkIfCarIsRentedOrUnderMaintenance(int carId) {
        if (carService.getById(carId).getState().equals(State.RENTED)) {
            throw new RuntimeException("Araç zaten kirada tekrar kiralanamaz.");
        }
        if (carService.getById(carId).getState().equals(State.MAINTENANCE)) {
            throw new RuntimeException("Araç bakımda kiralanamaz.");
        }
    }
}
