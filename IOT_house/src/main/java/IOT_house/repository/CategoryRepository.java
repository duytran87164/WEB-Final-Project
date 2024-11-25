package IOT_house.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import IOT_house.entity.Category;


public interface CategoryRepository extends JpaRepository<Category, Long>  {
	
	
}
