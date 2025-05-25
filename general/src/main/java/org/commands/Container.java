package org.commands;



import java.io.Serializable;

/**
 * Класс контейнер для передачи команд между клиентом и сервером
 */
public class Container implements Serializable {
    private static final long serialVersionUID = 15L;
    private CommandType commandType;
    private String args;

    public Container(CommandType commandType, String args) {
        this.commandType = commandType;
        this.args = args;
    }

    /**
     * Метод для получения типа команды
     * @return тип команды
     */
    public CommandType getCommandType() {
        return commandType;
    }

    /**
     * Метод для получения аргументов команды
     * @return аргументы команды
     */
    public String getArgs() {
        return args;
    }
}
