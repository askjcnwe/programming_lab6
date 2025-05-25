package org.commands;

import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;

/**
 * Класс для фильтрции коллекции по описанию
 */
public class FilterByDescription extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    public FilterByDescription(Console console, CollectionManager collectionManager) {
        super("filter_by_description", "выводит все элементы с заданным описанием");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнение команды
     * @param args Аргументы
     * @return результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] args) {
        if (args.length < 2 || args[1] == null || args[1].isEmpty()) {
            System.out.println("Error: Invalid number of arguments or empty description");
            return new ExecutionResponse(false, "Неправильное количество аргументов \nФормат: '" + getName() + " {description}'");
        }

        System.out.println("Filtering collection by description: '" + args[1].trim() + "'");
        
        var str = "";
        int matchCount = 0;
        for(var dragon : collectionManager.getCollection()) {
            if (dragon.getDescription().equals(args[1].trim())) {
                str += dragon.toFormattedString() + "\n";
                matchCount++;
            }
        }

        System.out.println("Found " + matchCount + " dragons matching the description");
        
        if (matchCount == 0) {
            return new ExecutionResponse(true, "Драконы с таким описанием не найдены");
        }
        return new ExecutionResponse(true, str);
    }

}
