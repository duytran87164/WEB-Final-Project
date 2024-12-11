package IOT_house.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import IOT_house.entity.Roles;
@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
	@Query("SELECT u FROM Roles u WHERE u.name = :name")
	public Roles getUserByName(@Param("name") String name);
	Optional<Roles> findByName(String name);
}
