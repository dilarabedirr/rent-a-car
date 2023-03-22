package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.BrandService;
import kodlama.io.rentacar.entities.concretes.Brand;
import kodlama.io.rentacar.repository.abstracts.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandManager implements BrandService {

    private BrandRepository brandRepository;

    @Autowired
    public BrandManager(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand add(Brand brand) {
        return brandRepository.add(brand);
    }

    @Override
    public void delete(int id) {
        brandRepository.delete(id);
    }

    @Override
    public Brand update(int id, Brand brand) {
        return brandRepository.update(id,brand);
    }

    @Override
    public List<Brand> getAll() {
        return brandRepository.getAll();
    }

    @Override
    public Brand getById(int id) {
        return brandRepository.getById(id);
    }
}
