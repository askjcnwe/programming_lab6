package org.clientCommand;

import org.commands.Command;
import org.utility.Console;
import org.utility.ExecutionResponse;

/**
 * Класс для команды завершения работы программы
 */
public class Exit extends Command {
    private final Console console;

    public Exit(Console console) {
        super("exit", "Завершить выполнение программы (без сохранения коллекции)");
        this.console = console;
    }

    /**
     * Исполнение команды
     * @param args Аргументы
     * @return выполнена ли команда успешно
     */
    @Override
    public ExecutionResponse apply(String[] args) {
        if (!args[1].isEmpty()){
            return new ExecutionResponse(false, "Неправильное количество аргументов. \nИспользование: '"+ getName()+"'");
        }
        return new ExecutionResponse("exit");
    }

}
