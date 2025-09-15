package br.edu.at.App.exceptions;

public class EnrollmentNotFoundException extends Exception {
    public EnrollmentNotFoundException() {
        super("Matrícula não encontrada.");
    }
}
