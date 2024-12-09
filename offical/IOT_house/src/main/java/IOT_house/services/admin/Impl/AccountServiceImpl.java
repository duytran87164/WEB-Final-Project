package IOT_house.services.admin.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import IOT_house.entity.Account;

import IOT_house.repository.AccRepository;

import IOT_house.services.admin.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	AccRepository accRepository;

	
	public AccountServiceImpl(AccRepository accRepository) {
		this.accRepository = accRepository;
	}


	@Override
	public <S extends Account> S save(S entity) {
		// TODO Auto-generated method stub
		return accRepository.save(entity);
	}


	@Override
	public List<Account> findAll() {
		// TODO Auto-generated method stub
		return accRepository.findAll();
	}


	@Override
	public long count() {
		// TODO Auto-generated method stub
		return accRepository.count();
	}


	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		accRepository.deleteById(id);
	}


	@Override
	public Optional<Account> findById(long id) {
		// TODO Auto-generated method stub
		return accRepository.findById(id);
	}


	@Override
	public Page<Account> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return accRepository.findAll(pageable);
	}


	@Override
	public Optional<Account> findByUsername(String userName) {
		// TODO Auto-generated method stub
		return accRepository.findByUsername(userName);
	}

	

}
