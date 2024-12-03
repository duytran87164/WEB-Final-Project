package IOT_house.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import IOT_house.entity.Equipments;

public interface EquipRepository extends JpaRepository<Equipments,Long> {
	 List<Equipments> findByHouse_idHouse(String id_house);

	List<Equipments> findBySensor(String sensor);

}
