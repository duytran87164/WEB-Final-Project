package IOT_house.services;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import IOT_house.entity.Account;
import IOT_house.entity.Roles;
import IOT_house.repository.AccRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	private AccRepository accRepository;
	
	public CustomUserDetailsService(AccRepository accRepository) {
		this.accRepository = accRepository;
	}
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accRepository.getUserByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found");
        }
	    return new User(
	        account.getUsername(), 
	        account.getPassword(), 
	        account.getAuthorities()
	        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Roles> roles) {
        return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                    .collect(Collectors.toList());
    }
}
