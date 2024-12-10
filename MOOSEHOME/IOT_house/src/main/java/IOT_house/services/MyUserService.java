package IOT_house.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import IOT_house.entity.Account;
import IOT_house.entity.Roles;

public class MyUserService implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Account acc;
	public MyUserService(Account acc) {
		// TODO Auto-generated constructor stub
		this.acc = acc;
	}
		
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Set<Roles> roles = acc.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Roles role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return acc.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return acc.getUsername();
	}

}
