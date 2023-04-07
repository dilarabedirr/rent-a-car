package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.abstracts.MaintenanceService;
import kodlama.io.rentacar.business.dto.requests.create.CreateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllMaintenancesResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateMaintenanceResponse;
import kodlama.io.rentacar.entities.Maintenance;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MaintenanceManager implements MaintenanceService {
    private final MaintenanceRepository repository;
    private final ModelMapper mapper;
    private final CarService carService;

    @Override
    public List<GetAllMaintenancesResponse> getAll() {
        List<Maintenance> maintenances = repository.findAll();
        List<GetAllMaintenancesResponse> responses = maintenances.stream()
                .map(maintenance -> mapper.map(maintenance, GetAllMaintenancesResponse.class)).toList();
        return responses;
    }

    @Override
    public GetMaintenanceResponse getById(int id) {
        Maintenance maintenance = repository.findById(id).orElseThrow();
        GetMaintenanceResponse response = mapper.map(maintenance, GetMaintenanceResponse.class);
        return response;
    }

    @Override
    public GetMaintenanceResponse returnCarFromMaintenance(int carId) {
        //maintenance tablosunda verdiğimiz carId'nin isCompleted alanı false ise bize bir maintenance
        //nesnesi dönecek. Bizde aracı bakımdan çıkarmak için isCompleted kısmını burada true
        //olacak şekilde set edeceğiz.
        checkIfCarIsNotUnderMaintenance(carId);
        Maintenance maintenance = repository.findMaintenanceByCarIdAndIsCompletedFalse(carId);
        maintenance.setCompleted(true);
        maintenance.setDueDate(LocalDateTime.now());
        repository.save(maintenance);
        carService.changeState(carId, State.AVALIABLE);
        GetMaintenanceResponse response = mapper.map(maintenance, GetMaintenanceResponse.class);
        return response;
    }

    @Override
    public CreateMaintenanceResponse add(CreateMaintenanceRequest request) {
        checkCarAvalibilityForMaintenance(request.getCarId());
        checkIfCarUnderMaintenance(request.getCarId());
        Maintenance maintenance = mapper.map(request, Maintenance.class);
        maintenance.setId(0);
        maintenance.setCompleted(false);
        maintenance.setStartDate(LocalDateTime.now());
        maintenance.setDueDate(null);
        carService.changeState(request.getCarId(), State.MAINTENANCE);
        repository.save(maintenance);
        CreateMaintenanceResponse response = mapper.map(maintenance, CreateMaintenanceResponse.class);
        return response;
    }

    @Override
    public UpdateMaintenanceResponse update(int id, UpdateMaintenanceRequest request) {
        // maintenance tablosundaki tüm alanları günceller.
        Maintenance maintenance = mapper.map(request, Maintenance.class);
        maintenance.setId(id);
        repository.save(maintenance);
        UpdateMaintenanceResponse response = mapper.map(maintenance, UpdateMaintenanceResponse.class);
        return response;
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    // business rules
    private void checkIfCarUnderMaintenance(int carId) {
        if (repository.existsByCarIdAndIsCompletedFalse(carId)) {
            throw new RuntimeException("Araç şuanda bakımda!");
        }
    }

    private void checkIfCarIsNotUnderMaintenance(int carId) {
        if (!repository.existsByCarIdAndIsCompletedFalse(carId)) {
            throw new RuntimeException("Bakımda böyle bir araç bulunamadı!");
        }
    }

    private void checkCarAvalibilityForMaintenance(int carId) {
        if (carService.getById(carId).getState().equals(State.RENTED)) {
            throw new RuntimeException("Araç kirada olduğu için bakıma alınamaz.");
        }
    }
}
