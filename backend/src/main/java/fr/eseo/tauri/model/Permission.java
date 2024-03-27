package fr.eseo.tauri.model;

import fr.eseo.tauri.model.enumeration.PermissionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Team Creation',Team Suppression','Rate','Attribution Bonus/Malus','Manage Sprints','View Other Team Mark','Role Creation','View Own Team Mark','View Mark for All Students','View Mark')")
    private PermissionType type;

    @OneToOne
    @JoinColumn(name = "role_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Role role;
}
