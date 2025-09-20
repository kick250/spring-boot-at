package br.edu.at.App.exceptions;

public class InvalidCoursesQuantity extends Exception {
    public InvalidCoursesQuantity() {
        super("O aluno deve estar matriculado em pelo menos um curso.");
    }
}
