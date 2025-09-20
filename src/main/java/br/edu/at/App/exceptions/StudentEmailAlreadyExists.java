package br.edu.at.App.exceptions;

public class StudentEmailAlreadyExists extends Exception {
    public StudentEmailAlreadyExists() {
        super("O email desse aluno já está em uso.");
    }
}
