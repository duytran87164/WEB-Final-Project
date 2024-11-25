package IOT_house.services.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import IOT_house.entity.Category;

public interface CategoryService {

//	Optional<Category> findByName(String name);
//
//	Page<Category> findByNameContaining(String name, Pageable pageable);

	<S extends Category> S save(S entity);

	List<Category> findAll();

	long count();

	void deleteById(Long id);

	Optional<Category> findById(long id);

	Page<Category> findAll(Pageable pageable);

}
