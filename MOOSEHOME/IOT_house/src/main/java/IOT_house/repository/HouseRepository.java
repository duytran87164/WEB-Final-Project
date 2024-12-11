package IOT_house.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import IOT_house.entity.Account;
import IOT_house.entity.Houses;


public interface HouseRepository extends JpaRepository<Houses, String>  {
	List<Houses> findByAcc(Account account);
}
