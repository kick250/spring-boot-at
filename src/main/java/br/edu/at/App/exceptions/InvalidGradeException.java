package br.edu.at.App.exceptions;

public class InvalidGradeException extends Exception {
    public InvalidGradeException() {
        super("Nota inválida.");
    }
}
