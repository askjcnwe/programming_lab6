package org.commands;

import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;

/**
 * Класс для команды удаления элемента коллекции с заданным порядковым номером
 */
public class RemoveIndex extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveIndex(Console console, CollectionManager collectionManager) {
        super("remove_index", "Удаляет элемент с заданным индексом");
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
            System.out.println("Error: No index provided");
            return new ExecutionResponse(false, "Неправильное количество аргументов \nФормат: '" + getName() + "'");
        }

        int index = -1;
        try {
            index = Integer.parseInt(args[1]);
            System.out.println("Attempting to remove dragon at index: " + index);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid index format");
            return new ExecutionResponse(false, "Неверный формат индекса");
        }

        if (collectionManager.getCollection().size()-1 < index) {
            System.out.println("Error: Index " + index + " is out of bounds (collection size: " + collectionManager.getCollection().size() + ")");
            return new ExecutionResponse(false, "Индекс выходит за пределы коллекции");
        }

        int currentIndex = 0;
        for (var dragon : collectionManager.getCollection()) {
            if (currentIndex == index) {
                System.out.println("Removing dragon with ID " + dragon.getID() + " at index " + index);
                collectionManager.remove(dragon.getID());
                collectionManager.update();
                return new ExecutionResponse(true, "Дракон успешно удален");
            }
            currentIndex++;
        }

        System.out.println("Error: Unexpected error while removing dragon at index " + index);
        return new ExecutionResponse(false, "Произошла ошибка при удалении дракона");
    }
}
