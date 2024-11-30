package IOT_house.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
public class Account implements Serializable {/**
	 * 
	 */
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
	@NotEmpty(message = "Khong Duoc Bo Trong")
	private String password;
	
	@Column(name = "Phone",columnDefinition = "VARCHAR(500)")
	private String phone;
	
	@Column(name = "CreateDate")
	@NotNull(message = "Khong Duoc Bo Trong")
	private LocalDate crDate;
	
	@Column(name="status", columnDefinition = "VARCHAR(500)")
	private int status;
	
	@Column(name = "is_admin", nullable = false)
	private Boolean isAdmin = false;

	
	@OneToMany(mappedBy = "acc")
	private List<Houses> houses;
	
}
