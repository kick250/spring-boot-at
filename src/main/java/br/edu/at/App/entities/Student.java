package br.edu.at.App.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "Student")
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
    private String cpf;
    @Getter
    private String email;
    @Getter
    private String phone;
    @Getter
    private String address;
    @OneToMany
    @JoinColumn(name = "student_id")
    @Getter
    private List<Enrollment> enrollments;

    public Student(String name, String cpf, String email, String phone, String address) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
