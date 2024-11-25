package IOT_house.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import IOT_house.entity.Houses;


public interface HouseRepository extends JpaRepository<Houses, Long>  {
	
	
}
