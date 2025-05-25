package org.clientCommand;

import org.commands.Command;
import org.commands.CommandType;
import org.utility.Console;
import org.utility.ExecutionResponse;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * класс команды для вывода справки по всем доступным командам
 */
public class Help extends Command {
    private final Console console;
    private Map<CommandType, String[]> commands ;

    public Help(Console console, Map<CommandType, String[]> commands) {
        super("help", "Вывести справку по доступным командам");
        this.console = console;
        this.commands = commands;
    }

    /**
     * Исполнение команды
     * @param args Аргументы
     * @return результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] args) {
        if (!args[1].isEmpty())
            return new ExecutionResponse(false, "Неправильное количество аргументов.\nИспользование: '"+getName()+"'");
        return new ExecutionResponse(commands.keySet().stream().map(comand -> String.format(" %35s%-1s%n", commands.get(comand)[0], commands.get(comand)[1])).collect(Collectors.joining("\n")));
    }
}
