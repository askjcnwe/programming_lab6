package org.commands;

import org.models.Dragon;
import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;

/**
 * Класс команды для добавления элемента в коллекцию
 */
public class Add extends Command {
    private final Console console;
    public final CollectionManager collectionManager;

    public Add(Console console, CollectionManager collectionManager) {
        super("add {elem}", "добавляет элемент {elem} в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнение команды
     * @param args Массив с аргументами команды
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] args) {
        try {
            if(args.length < 2 || args[1].isEmpty()) {
                System.out.println("Error: Empty arguments provided");
                return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
            }

            System.out.println("Processing new element addition...");
            
            // Split input data
            String[] dragonData = args[1].split("/");
            if (dragonData.length < 9) {
                System.out.println("Error: Insufficient data fields received: " + dragonData.length + " (required: 9)");
                return new ExecutionResponse(false, "Недостаточно данных для создания объекта");
            }

            // Временно установим ID в 1, он будет заменен в методе add коллекции
            dragonData[0] = "1";
            
            System.out.println("Creating dragon object from provided data...");
            
            // Create dragon with temporary ID
            Dragon dragon = Dragon.fromArray(dragonData);
            if (dragon == null) {
                System.out.println("Error: Failed to create Dragon object. Invalid data format");
                return new ExecutionResponse(false, "Не удалось создать объект. Проверьте корректность данных.");
            }

            if (!dragon.validate()) {
                System.out.println("Error: Dragon object validation failed");
                return new ExecutionResponse(false, "Объект не прошел валидацию. Проверьте обязательные поля.");
            }

            System.out.println("Attempting to add dragon to collection...");
            
            // Добавляем дракона в коллекцию (ID будет установлен автоматически)
            if (collectionManager.add(dragon)) {
                System.out.println("Dragon successfully added to collection");
                return new ExecutionResponse(true, "Объект успешно добавлен в коллекцию");
            } else {
                System.out.println("Error: Failed to add dragon to collection");
                return new ExecutionResponse(false, "Не удалось добавить объект в коллекцию");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while adding element: " + e.getMessage());
            return new ExecutionResponse(false, "Произошла ошибка при добавлении объекта: " + e.getMessage());
        }
    }
}
