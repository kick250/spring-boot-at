package br.edu.at.App.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Entity(name = "Course")
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
    private String code;
    @OneToMany
    @JoinColumn(name = "course_id")
    @Getter
    private List<Enrollment> enrollments;

    private final double PASSING_GRADE = 7.0;

    public Course(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public List<Student> getStudents() {
        return enrollments.stream().map(Enrollment::getStudent).toList();
    }

    public List<Student> getApprovedStudents() {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getGrade() >= PASSING_GRADE)
                .map(Enrollment::getStudent)
                .toList();
    }

    public List<Student> getFailedStudents() {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getGrade() < PASSING_GRADE)
                .map(Enrollment::getStudent)
                .toList();
    }
}
