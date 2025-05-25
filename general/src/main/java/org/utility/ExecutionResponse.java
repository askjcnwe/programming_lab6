package org.utility;

import java.io.Serializable;

/**
 * Класс для отслеживания результа выполнения команды
 */
public class ExecutionResponse implements Serializable {

    private static final long serialVersionUID = 12L;
    private boolean exitCode;
    private String message;

    public ExecutionResponse(boolean exitCode, String message) {
        this.exitCode = exitCode;
        this.message = message;
    }

    public ExecutionResponse(String message) {
        this(true, message);
    }


    public boolean getExitCode() {
        return exitCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.valueOf(exitCode) + ";" + message;
    }
}