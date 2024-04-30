package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private Integer id;

	@JsonProperty
	private String name;

	@JsonProperty
	private String email;

	@JsonProperty
	private String password;

	@JsonProperty
	private String privateKey;

}