package IOT_house.services.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import IOT_house.entity.Account;

public interface AccountService {
	<S extends Account> S save(S entity);

	List<Account> findAll();

	long count();

	void deleteById(Long id);

	Optional<Account> findById(long id);

	Page<Account> findAll(Pageable pageable);
	
	Optional<Account> findByUsername(String userName);
	
	
}
