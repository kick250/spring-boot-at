package br.edu.at.App.exceptions;

public class StudentNotFoundException extends Exception {
    public StudentNotFoundException() {
        super("Aluno não encontrado.");
    }
}
