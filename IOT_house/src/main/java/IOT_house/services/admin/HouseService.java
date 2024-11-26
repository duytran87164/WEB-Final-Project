package IOT_house.services.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import IOT_house.entity.Account;
import IOT_house.entity.Houses;

public interface HouseService {

//	Optional<Category> findByName(String name);
//
//	Page<Category> findByNameContaining(String name, Pageable pageable);

	<S extends Houses> S save(S entity);

	List<Houses> findAll();

	long count();

	void deleteById(String houseId);

	Optional<Houses> findById(String houseId);

	Page<Houses> findAll(Pageable pageable);

	List<Houses> findByAccount(Account account);
	

}
