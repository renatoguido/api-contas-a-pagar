package com.rtz.api_contas_a_pagar.exception;

public class ContaNotFoundException extends RuntimeException  {
    public ContaNotFoundException() {
        super("Conta não encontrada.");
    }

    public ContaNotFoundException(String message) {
        super(message);
    }

    public ContaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContaNotFoundException(Throwable cause) {
        super(cause);
    }
}
