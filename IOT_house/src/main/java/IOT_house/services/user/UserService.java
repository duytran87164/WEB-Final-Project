package IOT_house.services.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import IOT_house.entity.Account;
import IOT_house.repository.AccRepository;
import IOT_house.services.user.impl.IUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Service
public class UserService implements IUserService {
	@Autowired
	AccRepository userRepository;
	public UserService() {
	}

	@Override
	public List<Account> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public Optional<Account> findById(long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}

	@Override
	public void insert(Account user) {
		// TODO Auto-generated method stub
		userRepository.save(user);
	}

	@Override
	public Account UpdateByUser(String user, String fullname, String phone) {
		// TODO Auto-generated method stub
		Optional<Account> accountOpt = userRepository.findByUsername(user);
	    if (accountOpt.isPresent()) {
	        Account account = accountOpt.get();
	        account.setFullName(fullname);
	        account.setPhone(phone);
	        return userRepository.save(account); // Lưu thay đổi và trả về đối tượng đã cập nhật
	    }
	    return null;
	}

	@Override
	public void UpdatePswbyUser(String user, String psw) {
		// TODO Auto-generated method stub
		Optional<Account> accountOpt = userRepository.findByUsername(user);
	    if (accountOpt.isPresent()) {
	        Account account = accountOpt.get();
	        account.setPassword(psw);
	        userRepository.save(account); // Lưu mật khẩu mới
	    }
	}

	@Override
	public boolean CheckEmailExist(String email) {
		// TODO Auto-generated method stub
		return userRepository.existsByEmail(email);
	}

	@Override
	public boolean CheckEmailDuplicate(String email, String user) {
		// TODO Auto-generated method stub
		Optional<Account> accountOpt = userRepository.findByEmail(email);
	    if (accountOpt.isPresent()) {
	        Account account = accountOpt.get();
	        return !account.getUsername().equals(user); // Email đã tồn tại và không thuộc người dùng hiện tại
	    }
	    return false;
	}

	@Override
	public boolean CheckUserExist(String user) {
		// TODO Auto-generated method stub
		return userRepository.existsByUsername(user);
	}

	@Override
	public Account findbyUser(String User) {
		// TODO Auto-generated method stub
		Optional<Account> accountOpt = userRepository.findByUsername(User);
	    return accountOpt.orElse(null);
	}
}

