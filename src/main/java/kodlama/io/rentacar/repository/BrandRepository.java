package kodlama.io.rentacar.repository;

import kodlama.io.rentacar.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand,Integer>  {
    //custom queries
    Brand findByName(String name);
    boolean existsByName(String name);
}
