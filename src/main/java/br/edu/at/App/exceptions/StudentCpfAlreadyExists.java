package br.edu.at.App.exceptions;

public class StudentCpfAlreadyExists extends Exception {
    public StudentCpfAlreadyExists() {
        super("Já existe um aluno cadastrado com este CPF.");
    }
}
