package fr.eseo.tauri.model;

import fr.eseo.tauri.model.enumeration.PermissionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private PermissionType type;

    @ManyToOne(cascade = cascadeType.REMOVE)
    @JoinColumn(name = "role_id")
    private Role role;
}
