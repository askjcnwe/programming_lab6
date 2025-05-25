package org.commands;

import org.models.Dragon;
import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;

/**
 * Класс для команды для обновления значения элемента коллекции по ID
 */
public class UpdateID extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public UpdateID(Console console, CollectionManager collectionManager) {
        super("update <ID> {element}", "обновляет значение элемента коллекции по значению id");
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
            System.out.println("Error: No data provided");
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nФормат: '" + getName() + "'");
        }

        // Разбираем строку данных дракона
        String[] dragonData = args[1].split("/");
        if (dragonData.length < 9) {
            System.out.println("Error: Insufficient dragon data fields");
            return new ExecutionResponse(false, "Недостаточно данных для создания объекта");
        }

        // Парсим ID из первого поля данных
        long id = -1;
        try {
            id = Long.parseLong(dragonData[0].trim());
            System.out.println("Attempting to update dragon with ID: " + id);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid ID format in dragon data");
            return new ExecutionResponse(false, "Неверный формат ID");
        }

        // Проверяем существование дракона
        var previous = collectionManager.byId(id);
        if (previous == null || !collectionManager.getCollection().contains(previous)) {
            System.out.println("Error: Dragon with ID " + id + " not found");
            return new ExecutionResponse(false, "Дракон с указанным ID не найден");
        }

        System.out.println("Processing update data...");
        Dragon dragon = Dragon.fromArray(dragonData);

        if (dragon != null && dragon.validate()) {
            System.out.println("Removing old dragon data...");
            collectionManager.remove(previous.getID());
            
            System.out.println("Adding updated dragon data...");
            collectionManager.add(dragon);
            collectionManager.update();
            
            System.out.println("Dragon with ID " + id + " successfully updated");
            return new ExecutionResponse(true, "Дракон успешно обновлен");
        } else {
            System.out.println("Error: Invalid dragon data provided");
            return new ExecutionResponse(false, "Ошибка при обновлении: некорректные данные");
        }
    }
}
