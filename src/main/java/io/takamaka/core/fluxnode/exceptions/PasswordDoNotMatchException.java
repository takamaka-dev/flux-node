/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.takamaka.core.fluxnode.exceptions;

/**
 *
 * @author Giovanni Antino giovanni.antino@takamaka.io
 */
public class PasswordDoNotMatchException extends FNUException {

    private static final long serialVersionUID = 1L;

    public PasswordDoNotMatchException() {
        super();
    }

    public PasswordDoNotMatchException(String message) {
        super(message);
    }

    public PasswordDoNotMatchException(Throwable err) {
        super(err);
    }

    public PasswordDoNotMatchException(String message, Throwable err) {
        super(message, err);
    }

}
