package br.edu.at.App.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "Teacher")
@Table(name = "teachers")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

}
