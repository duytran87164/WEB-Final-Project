package IOT_house.services.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import IOT_house.entity.Houses;
import IOT_house.repository.HouseRepository;

@Service
public class HouseServiceImpl implements HouseService{
@Autowired
HouseRepository houseRepository;

public HouseServiceImpl(HouseRepository houseRepository) {
	this.houseRepository = houseRepository;
}

@Override
public void deleteById(Long id) {
	// TODO Auto-generated method stub
	houseRepository.deleteById(id);
	
}

@Override
public long count() {
	// TODO Auto-generated method stub
	return houseRepository.count();
}

@Override
public List<Houses> findAll() {
	// TODO Auto-generated method stub
	return houseRepository.findAll();
}

@Override
public Page<Houses> findAll(Pageable pageable) {
	return houseRepository.findAll(pageable);
}

@Override
public <S extends Houses> S save(S entity) {
	// TODO Auto-generated method stub
	return houseRepository.save(entity);
}

//@Override
//public Page<house> findByNameContaining(String name, Pageable pageable) {
//	// TODO Auto-generated method stub
//	return houseRepository.findByClient_nameContaining(name, pageable);
//}
//
//@Override
//public Optional<house> findByName(String name) {
//	// TODO Auto-generated method stub
//	return houseRepository.findByClient_name(name);
//}

@Override
public Optional<Houses> findById(long id) {
	// TODO Auto-generated method stub
	return houseRepository.findById(id);
}




	
	
}
