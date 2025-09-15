package br.edu.at.App.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Enrollment")
@Table(name = "enrollments")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    @Getter
    private Student student;
    @ManyToOne
    @JoinColumn(name = "course_id")
    @Getter
    private Course course;
    @Getter
    @Setter
    private Double grade;

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

}
