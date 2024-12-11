package IOT_house.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import IOT_house.entity.Account;

public interface AccRepository extends JpaRepository<Account, Long> {
	@Query("SELECT u FROM Account u WHERE u.username = :username")
	 public Account getUserByUsername(@Param("username") String username);

	
	Optional<Account> findByUsername(String userName);
	
	Optional<Account> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByUsername(String userName);
	

}
