package fr.eseo.tauri.model;

import fr.eseo.tauri.model.idClass.UserRolesId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "user_roles")
@IdClass(UserRolesId.class)
@Getter
@Setter
public class UserRoles {

    @Id
    @OneToOne
    @JoinColumn(name = "role_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Role role;

    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

}

