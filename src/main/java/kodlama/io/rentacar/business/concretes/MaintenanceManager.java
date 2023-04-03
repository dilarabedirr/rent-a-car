package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.abstracts.MaintenanceService;
import kodlama.io.rentacar.business.dto.requests.create.CreateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateCarRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllMaintenancesResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateMaintenanceResponse;
import kodlama.io.rentacar.entities.Maintenance;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MaintenanceManager implements MaintenanceService {
    private MaintenanceRepository repository;
    private ModelMapper mapper;
    private CarService carService;

    @Override
    public List<GetAllMaintenancesResponse> getAll() {
        List<Maintenance> maintenances=repository.findAll();
        List<GetAllMaintenancesResponse> responses=maintenances.stream()
                .map(maintenance -> mapper.map(maintenance, GetAllMaintenancesResponse.class)).toList();
        return responses;
    }

    @Override
    public GetMaintenanceResponse getById(int id) {
        Maintenance maintenance =repository.findById(id).orElseThrow();
        GetMaintenanceResponse response=mapper.map(maintenance,GetMaintenanceResponse.class);
        return response;
    }

    @Override
    public CreateMaintenanceResponse add(CreateMaintenanceRequest request) {
        Maintenance maintenance=mapper.map(request,Maintenance.class);
        maintenance.setId(0);
        checkIfCarCanBeSendToMaintenance(request.getCarId());
        repository.save(maintenance);
        changeCarStateToMaintenance(request.getCarId());
        CreateMaintenanceResponse response=mapper.map(maintenance,CreateMaintenanceResponse.class);
        return response;
    }

    @Override
    public UpdateMaintenanceResponse update(int id, UpdateMaintenanceRequest request) {
        // maintenance tablosundaki tüm alanları günceller.
        Maintenance maintenance=mapper.map(request,Maintenance.class);
        maintenance.setId(id);
        repository.save(maintenance);
        UpdateMaintenanceResponse response=mapper.map(maintenance,UpdateMaintenanceResponse.class);
        return response;
    }
    @Override
    public void finishMaintenance(int maintenanceId){
        //arabanın bakımı bittiğinde car tablosunda state alanını avaliable yapıp aracı tekrardan kiralanabilir duruma getirir
        //arabanın bakımı bitince bitiş tarihini buradan ayarlar.
        Maintenance maintenance =repository.findById(maintenanceId).orElseThrow();
        maintenance.setId(maintenanceId);
        maintenance.setDueDate(new Date());
        repository.save(maintenance);
        changeCarStateToAvailable(maintenance.getCar().getId());
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    // business rules

    private void changeCarStateToMaintenance(int carId){
        GetCarResponse getCarResponse=carService.getById(carId);
        getCarResponse.setState(State.MAINTANCE);
        UpdateCarRequest updateCarRequest=mapper.map(getCarResponse,UpdateCarRequest.class);
        carService.update(carId,updateCarRequest);
    }
    private void changeCarStateToAvailable(int carId){
        GetCarResponse getCarResponse=carService.getById(carId);
        getCarResponse.setState(State.AVALIABLE);
        UpdateCarRequest updateCarRequest=mapper.map(getCarResponse,UpdateCarRequest.class);
        carService.update(carId,updateCarRequest);
    }

    private void checkIfCarCanBeSendToMaintenance(int carId){
        checkIfCarStateMaintance(carId);
        checkIfCarStateRented(carId);
    }

    private void checkIfCarStateRented(int carId) {
        GetCarResponse carResponse=carService.getById(carId);
        if (carResponse.getState()==State.RENTED) {
            throw new RuntimeException("Araç kirada bakıma gönderilemez.");
        }
    }

    private void checkIfCarStateMaintance(int carId) {
        GetCarResponse carResponse=carService.getById(carId);
        if (carResponse.getState()==State.MAINTANCE) {
            throw new RuntimeException("bakımda olan araba bakıma gönderilemez");
        }
    }
}
