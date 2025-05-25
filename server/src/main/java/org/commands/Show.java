package org.commands;

import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;

/**
 * Класс для команды для вывода всех элементов коллекции в консоль
 */
public class Show extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Show(Console console, CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
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
        if (!args[1].isEmpty()) {
            System.out.println("Error: Command doesn't accept any arguments");
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nФормат: '" + getName() + "'");
        }

        System.out.println("Retrieving collection contents...");
        
        if (collectionManager.getCollection().isEmpty()) {
            System.out.println("Collection is empty");
            return new ExecutionResponse(true, "Коллекция пуста!");
        }

        System.out.println("Sending collection contents to client (" + 
            collectionManager.getCollection().size() + " dragons)");
            
        return new ExecutionResponse(collectionManager.toString());
    }
}
