package IOT_house.services.user.impl;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import IOT_house.entity.Account;

import IOT_house.repository.AccRepository;
import IOT_house.services.MyUserService;
import IOT_house.services.user.IUserService;
import jakarta.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
public class UserService implements IUserService,UserDetailsService {
	@Autowired
	AccRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
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
	public void UpdatePswbyEmail(String email, String psw) {
		// TODO Auto-generated method stub
		Optional<Account> accountOpt = userRepository.findByEmail(email);
	    if (accountOpt.isPresent()) {
	        Account account = accountOpt.get();
	        String hashedPassword =passwordEncoder.encode(psw);
	        account.setPassword(hashedPassword);
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
	@Override
	public Account findByEmail(String email) {
		// TODO Auto-generated method stub
		Optional<Account> accountOpt = userRepository.findByEmail(email);
	    return accountOpt.orElse(null);
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account user = userRepository.getUserByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
		return user;
	}


}

