package IOT_house.services.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import IOT_house.entity.Category;
import IOT_house.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{
@Autowired
CategoryRepository categoryRepository;

public CategoryServiceImpl(CategoryRepository categoryRepository) {
	this.categoryRepository = categoryRepository;
}

@Override
public void deleteById(Long id) {
	// TODO Auto-generated method stub
	categoryRepository.deleteById(id);
	
}

@Override
public long count() {
	// TODO Auto-generated method stub
	return categoryRepository.count();
}

@Override
public List<Category> findAll() {
	// TODO Auto-generated method stub
	return categoryRepository.findAll();
}

@Override
public Page<Category> findAll(Pageable pageable) {
	return categoryRepository.findAll(pageable);
}

@Override
public <S extends Category> S save(S entity) {
	// TODO Auto-generated method stub
	return categoryRepository.save(entity);
}

//@Override
//public Page<Category> findByNameContaining(String name, Pageable pageable) {
//	// TODO Auto-generated method stub
//	return categoryRepository.findByClient_nameContaining(name, pageable);
//}
//
//@Override
//public Optional<Category> findByName(String name) {
//	// TODO Auto-generated method stub
//	return categoryRepository.findByClient_name(name);
//}

@Override
public Optional<Category> findById(long id) {
	// TODO Auto-generated method stub
	return categoryRepository.findById(id);
}




	
	
}
