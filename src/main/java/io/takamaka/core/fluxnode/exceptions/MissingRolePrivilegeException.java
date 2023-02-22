/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.takamaka.core.fluxnode.exceptions;

/**
 *
 * @author Giovanni Antino giovanni.antino@takamaka.io
 */
public class MissingRolePrivilegeException extends FNUException {

    private static final long serialVersionUID = 1L;

    public MissingRolePrivilegeException() {
        super();
    }

    public MissingRolePrivilegeException(String message) {
        super(message);
    }

    public MissingRolePrivilegeException(Throwable err) {
        super(err);
    }

    public MissingRolePrivilegeException(String message, Throwable err) {
        super(message, err);
    }

}
