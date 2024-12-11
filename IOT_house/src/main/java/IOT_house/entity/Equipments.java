package IOT_house.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "equipments")
public class Equipments implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_house")
	private Houses house;

	@Column(name = "sensor")
	private String sensor;
	
	@Column(name = "NSX")
	private String nsx;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "image")
	private String image;
	
	@Transient
	private Boolean isEdit = false;

}
