package IOT_house.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "equipments")
public class Equipments implements Serializable {
	/**
	* 
	*/
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

	@Column(name = "value", columnDefinition = "VARCHAR(500)")
	private String value;
	
	@Transient
	private Boolean isEdit = false;

}
