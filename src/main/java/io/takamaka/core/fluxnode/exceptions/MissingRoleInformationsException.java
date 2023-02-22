/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.takamaka.core.fluxnode.exceptions;

/**
 *
 * @author Giovanni Antino giovanni.antino@takamaka.io
 */
public class MissingRoleInformationsException extends FNUException {

    private static final long serialVersionUID = 1L;

    public MissingRoleInformationsException() {
        super();
    }

    public MissingRoleInformationsException(String message) {
        super(message);
    }

    public MissingRoleInformationsException(Throwable err) {
        super(err);
    }

    public MissingRoleInformationsException(String message, Throwable err) {
        super(message, err);
    }

}
