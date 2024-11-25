package IOT_house.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import IOT_house.entity.Account;

public interface UserRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByUsername(String userName);
	
	Optional<Account> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByUsername(String userName);

}
