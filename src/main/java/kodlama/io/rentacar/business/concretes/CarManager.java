package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.dto.requests.create.CreateCarRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateCarRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllCarsResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetCarResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateCarResponse;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Service
public class CarManager implements CarService {
    private CarRepository repository;
    private ModelMapper mapper;

    @Override
    public List<GetAllCarsResponse> getAll(String state) {
        List<Car> cars = repository.findAll();
        if (state.equals("MAINTANCE")){
            List<Car> maintanceCarList=new ArrayList<>();
            for (Car car : cars) {
                if (car.getState().name().equals("MAINTANCE")){
                    maintanceCarList.add(car);
                }
            }
            return maintanceCarList.stream().map(car -> mapper.map(car, GetAllCarsResponse.class)).toList();
        }
        List<GetAllCarsResponse> responses = cars.stream()
                .map(car -> mapper.map(car, GetAllCarsResponse.class)).toList();
        return responses;
    }

    @Override
    public GetCarResponse getById(int id) {
        Car car = repository.findById(id).orElseThrow();
        GetCarResponse response = mapper.map(car, GetCarResponse.class);
        return response;
    }

    @Override
    public CreateCarResponse add(CreateCarRequest request) {
        Car car = mapper.map(request, Car.class);
        car.setId(0);
        repository.save(car);
        CreateCarResponse response = mapper.map(car, CreateCarResponse.class);
        return response;
    }

    @Override
    public UpdateCarResponse update(int id, UpdateCarRequest request) {
        Car car = mapper.map(request, Car.class);
        car.setId(id);
        repository.save(car);
        UpdateCarResponse response = mapper.map(car, UpdateCarResponse.class);
        return response;
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    public void sendCarToMaintenance(int id) {
        Car car = repository.findById(id).orElseThrow();
        checkIfCarStateRented(car.getState());
        checkIfCarStateMaintance(car.getState());
        car.setState(State.MAINTANCE);
        car.setId(id);
        repository.save(car);
    }

    public void carAvailable(int id) {
        Car car = repository.findById(id).orElseThrow();
        //checkIfCarStateRented(car.getState());
        checkIfCarStateAvailable(car.getState());
        car.setState(State.AVALIABLE);
        car.setId(id);
        repository.save(car);
    }

//    arabalar bakıma (maintenance) gönderilebilmelidir. Bakımdan gelen araba yeniden kiralanabilir duruma gelmelidir.
//    Zaten bakımda olan araba bakıma gönderilememez. Kirada olan araba bakıma gönderilemez. Bakımda olan araba araba listesinde
//    görüntülenip görüntülenmeyeceğine kullanıcıdan bir parametre alarak gelmelidir veya gelmemelidir.

    private void checkIfCarStateRented(State state) {
        if (state.equals(State.RENTED)) {
            throw new RuntimeException("Araç kirada bakıma gönderilemez.");
        }
    }

    private void checkIfCarStateMaintance(State state) {
        if (state.equals(State.MAINTANCE)) {
            throw new RuntimeException("bakımda olan araba bakıma gönderilemez");
        }
    }

    private void checkIfCarStateAvailable(State state) {
        if (state.equals(State.AVALIABLE)) {
            throw new RuntimeException("Araba kiralanabilir durumda.");
        }
    }
}
