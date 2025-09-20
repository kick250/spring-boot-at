package br.edu.at.App.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Ocorreu um erro ao gerar um token.");
    }
}
