package IOT_house.services.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import IOT_house.entity.Equipments;
import IOT_house.entity.Houses;

public interface EquipmentService {
	<S extends Equipments> S save(S entity);

	List<Equipments> findAll();

	long count();

	void deleteById(Long id);

	Optional<Equipments> findById(Long id);

	Page<Equipments> findAll(Pageable pageable);
	
	List<Equipments> findByHouseId(String idHouse);

	List<Equipments> findBySensor(List<Equipments> equip, String sensor);
}
