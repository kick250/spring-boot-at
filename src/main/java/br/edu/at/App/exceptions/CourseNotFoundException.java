package br.edu.at.App.exceptions;

public class CourseNotFoundException extends Exception {
    public CourseNotFoundException() {
        super("Um ou mais cursos não foram encontrados.");
    }
}
