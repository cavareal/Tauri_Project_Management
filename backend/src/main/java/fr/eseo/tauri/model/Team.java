package fr.eseo.tauri.model;

import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.service.TeamService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Entity
@Table(name = "teams")
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project projectId;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User leaderId;

}

