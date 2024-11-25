package IOT_house.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="category-springboot")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(name="id")
	private Long id;
	
	@Column(name="client_name", columnDefinition = "VARCHAR(500)")
	@NotEmpty(message = "Khong Duoc Bo Trong")
	private String client_name;
	@Column(name="id_house", columnDefinition = "VARCHAR(500)")
	private String id_house;
	@Column(name="password", columnDefinition = "VARCHAR(500)")
	private String password;
	
	@Column(name="image", columnDefinition = "VARCHAR(500)")
	private String image;

	@Column(name="description", columnDefinition = "VARCHAR(500)")
	private String description;
	
	@Column(name="status", columnDefinition = "VARCHAR(500)")
	private int status;
	
	private Boolean isEdit = false;
}
