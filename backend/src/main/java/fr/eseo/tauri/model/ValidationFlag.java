package fr.eseo.tauri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "validation_flags")
@Getter
@Setter
public class ValidationFlag {


    private boolean confirmed;
}
