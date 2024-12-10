package IOT_house.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="Account")
public class Account implements UserDetails {

	/**
	 * 
	 */
	Account acc;
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id_acc")
	private Long id;
	
	@Column(name = "Username",columnDefinition = "VARCHAR(500)")
	@NotEmpty(message = "Khong Duoc Bo Trong")
	private String username;
	
	@Column(name = "Email",columnDefinition = "VARCHAR(500)")
	@NotEmpty(message = "Khong Duoc Bo Trong")
	private String email;
	
	@Column(name = "Fullname",columnDefinition = "VARCHAR(500)")
	private String fullName;
	
	@Column(name = "Image",columnDefinition = "VARCHAR(500)")
	private String image;
	
	@Column(name = "Password",columnDefinition = "VARCHAR(500)")
	private String password;
	
	@Column(name = "Phone",columnDefinition = "VARCHAR(500)")
	private String phone;
	
	@Column(name = "CreateDate")
	@NotNull(message = "Khong Duoc Bo Trong")
	private LocalDate crDate;
	
	@Column(name="status", columnDefinition = "VARCHAR(500)")
	private boolean status;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE,CascadeType.ALL}, fetch = FetchType.EAGER)	
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "Id_acc"),
			inverseJoinColumns = @JoinColumn(name ="role_id")
	)
	private Set<Roles> roles = new HashSet<>();
	
	@OneToMany(mappedBy = "acc", fetch = FetchType.EAGER)
	private List<Houses> houses;

	// Implementing UserDetails methods
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		List<GrantedAuthority> authorities = this.getRoles().stream()
//	            .map(role -> new SimpleGrantedAuthority(role.getName()))
//	            .collect(Collectors.toList());
//		return authorities;
//	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    Set<Roles> roles = this.getRoles();  // lấy các vai trò
	    if (roles.isEmpty()) {
	        System.out.println("No roles assigned to account: " + this.getUsername());
	    } else {
	        roles.forEach(role -> System.out.println("Role: " + role.getName()));
	    }
	    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
	    for (Roles role : roles) {
	        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));  // Thêm tiền tố ROLE_
	    }
	    return authorities;
	}




	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // You can add logic to check expiration
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.status; // Assuming `status` indicates if the account is locked
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // You can add logic to check if credentials are expired
	}

	@Override
	public boolean isEnabled() {
		return this.status; // Assuming `status` indicates if the account is enabled
	}
}
