package IOT_house.services.admin.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import IOT_house.entity.Equipments;
import IOT_house.entity.Houses;
import IOT_house.repository.EquipRepository;
import IOT_house.services.admin.EquipmentService;

@Service
public class EquipmentServiceImpl implements EquipmentService {
	@Autowired
	EquipRepository equipRepository;

	public EquipmentServiceImpl(EquipRepository equipRepository) {
		this.equipRepository = equipRepository;
	}

	@Override
	public <S extends Equipments> S save(S entity) {
		// TODO Auto-generated method stub
		return equipRepository.save(entity);
	}

	@Override
	public List<Equipments> findAll() {
		// TODO Auto-generated method stub
		return equipRepository.findAll();
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return equipRepository.count();
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		equipRepository.deleteById(id);
	}

	@Override
	public Optional<Equipments> findById(Long id) {
		// TODO Auto-generated method stub
		return equipRepository.findById(id);
	}

	@Override
	public Page<Equipments> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return equipRepository.findAll(pageable);
	}

	public List<Equipments> findByHouseId(String idHouse) {
        return equipRepository.findByHouse_idHouse(idHouse);
    }

	@Override
	public List<Equipments> findBySensor(List<Equipments> equip, String sensor) {
		// TODO Auto-generated method stub
		return equip.stream()
                .filter(e -> sensor.equals(e.getSensor()))
                .collect(Collectors.toList());
	}
	
}
