package org.clientCommand;


import org.commands.Command;
import org.utility.Console;
import org.utility.ExecutionResponse;

/**
 * Класс для команды для вывода списка команд из файла
 */
public class ExecuteScript extends Command {
    private final Console console;

    public ExecuteScript(Console console) {
        super("execute_script <file_name>", "Выполнить последовательность команд из файла");
        this.console = console;
    }

    /**
     * Исполнение команды
     * @param args Аргументыты
     * @return выполнена ли команда успешно
     */
    @Override
    public ExecutionResponse apply(String[] args) {
        if (args[1].isEmpty()){
            return new ExecutionResponse(false, "Неправильное количество аргуметов.\nИспользование: '"+getName()+"'");
        }
        return new ExecutionResponse(true, "Выполнение скрипта" + args[1] + "...");
    }
}
