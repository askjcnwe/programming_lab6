package org.commands;

import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;

/**
 * Класс команды для удаления элемента коллекции по описанию
 */
public class RemoveByDescription extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    public RemoveByDescription(Console console, CollectionManager collectionManager) {
        super("remove_by_description", "удаляет все элементы с заданным описанием");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнения команды
     * @param args Аргументы
     * @return результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] args) {
        if (args.length < 2 || args[1] == null || args[1].isEmpty()) {
            System.out.println("Error: Invalid number of arguments or empty description");
            return new ExecutionResponse(false, "Неправильное количество аргументов \nФормат: '" + getName() + " {description}'");
        }

        System.out.println("Removing dragons with description: '" + args[1].trim() + "'");
        
        var description = args[1];
        int removedCount = 0;
        for (var dragon : collectionManager.getCollection()) {
            if (dragon.getDescription().equals(description)) {
                collectionManager.remove(dragon.getID());
                removedCount++;
            }
        }
        
        System.out.println("Removed " + removedCount + " dragons with matching description");
        
        collectionManager.update();
        if (removedCount > 0) {
            return new ExecutionResponse(true, "Удалено драконов: " + removedCount);
        } else {
            return new ExecutionResponse(true, "Драконы с таким описанием не найдены");
        }
    }
}
