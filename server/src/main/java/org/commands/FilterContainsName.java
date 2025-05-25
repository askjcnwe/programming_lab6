package org.commands;

import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;

/**
 * Класс для фильтрации элементов коллекции по имени
 */
public class FilterContainsName extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public FilterContainsName(Console console, CollectionManager collectionManager) {
        super("filter_contains_name {name}", "вывести элементы, имя которых совпадает с заданным");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнение команды
     * @param args Аргументы
     * @return результат выполнения
     */
    @Override
    public ExecutionResponse apply(String[] args) {
        if (args.length < 2 || args[1] == null || args[1].isEmpty()) {
            System.out.println("Error: Invalid number of arguments or empty name");
            return new ExecutionResponse(false, "Неправильное количество аргументов \nФормат: '" + getName() + " {name}'");
        }

        System.out.println("Filtering collection by name: '" + args[1].trim() + "'");
        
        var str = "";
        int matchCount = 0;
        for(var dragon : collectionManager.getCollection()) {
            if (dragon.getName().equals(args[1].trim())) {
                str += dragon.toFormattedString() + "\n";
                matchCount++;
            }
        }

        System.out.println("Found " + matchCount + " dragons matching the name");
        
        if (matchCount == 0) {
            return new ExecutionResponse(true, "Драконы с таким именем не найдены");
        }
        return new ExecutionResponse(true, str);
    }
}
