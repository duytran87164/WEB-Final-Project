package IOT_house.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="House-List")
public class Houses implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id_house", columnDefinition = "VARCHAR(500)")
	private String idHouse;
	
	@Column(name="image", columnDefinition = "VARCHAR(500)")
	private String image;

	@Column(name="description", columnDefinition = "VARCHAR(500)")
	private String description;
	
	@Column(name="status", columnDefinition = "VARCHAR(500)")
	private int status;
	
	@Transient
	private Boolean isEdit = false;
	
	@ManyToOne
	@JoinColumn(name = "Id_acc")
	private Account acc;
	
	@OneToMany(mappedBy = "house")
	private List<Equipments> equipments;
}
