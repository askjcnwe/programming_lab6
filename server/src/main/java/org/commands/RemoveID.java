package org.commands;

import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;

/**
 * Класс команды для удаления элемента коллекции по ID
 */
public class RemoveID extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    public RemoveID(Console console, CollectionManager collectionManager) {
        super("remove_id", "удаляет элемент с заданным id");
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
        if (args[1].isEmpty()) {
            System.out.println("Error: No ID provided");
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nФормат: '" + getName() + "'");
        }
        
        long id = -1;
        try {
            id = Long.parseLong(args[1].trim());
            System.out.println("Attempting to remove dragon with ID: " + id);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid ID format");
            return new ExecutionResponse(false, "Неверный формат ID");
        }

        if (collectionManager.byId(id) == null || !collectionManager.getCollection().contains(collectionManager.byId(id))) {
            System.out.println("Error: Dragon with ID " + id + " not found");
            return new ExecutionResponse(false, "Дракон с указанным ID не найден");
        }
            
        collectionManager.remove(id);
        collectionManager.update();
        
        System.out.println("Successfully removed dragon with ID: " + id);
        return new ExecutionResponse(true, "Дракон успешно удален");
    }
}
