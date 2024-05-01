package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.util.valid.Create;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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

	@NotNull(groups = { Create.class }, message = "The name field is required")
	@JsonProperty
	private String name;

	@NotNull(groups = { Create.class }, message = "The email field is required")
	@Email(groups = { Create.class }, message = "The email field must be a valid email")
	@JsonProperty
	private String email;

	@NotNull(groups = { Create.class }, message = "The password field is required")
	@JsonProperty
	private String password;

	@NotNull(groups = { Create.class }, message = "The privateKey field is required")
	@JsonProperty
	private String privateKey;

}