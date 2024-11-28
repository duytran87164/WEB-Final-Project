package IOT_house.services.user.impl;

import java.util.List;
import java.util.Optional;

import IOT_house.entity.Account;

public interface IUserService {
	List<Account> findAll();
	
	Optional<Account>  findById(long id);
	
	void insert(Account user);
	
	Account UpdateByUser(String user, String fullname, String phone);
	
	void UpdatePswbyEmail(String email,String psw);
	
	boolean CheckEmailExist(String Email);
	
	boolean CheckEmailDuplicate(String Email,String user);
	
	boolean CheckUserExist(String User);
	
	Account findbyUser(String User);
	
	Account findByEmail(String email);
}
