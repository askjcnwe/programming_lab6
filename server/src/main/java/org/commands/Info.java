package org.commands;

import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;

import java.time.LocalDateTime;

/**
 * Класс команды для получения информации о коллекции
 */
public class Info extends Command {
    private final Console console;
    public final CollectionManager collectionManager;

    public Info(Console console, CollectionManager collectionManager) {
        super("info", "display info about collection");
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
            return new ExecutionResponse(false, "Неправильное количество аргументов \nФормат: '" + getName() + "'");
        }

        System.out.println("Retrieving collection information...");

        LocalDateTime lastInitTime = collectionManager.getLastInitTime();
        String lastInitTimeString = (lastInitTime == null) ? "not initialized yet" :
                lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

        LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
        String lastSaveTimeString = (lastSaveTime == null) ? "not saved yet" : 
                lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

        System.out.println("Collection type: " + collectionManager.getCollection().getClass().toString());
        System.out.println("Elements count: " + collectionManager.getCollection().size());
        System.out.println("Last save time: " + lastSaveTimeString);
        System.out.println("Last init time: " + lastInitTimeString);

        // Формируем ответ для клиента на русском
        String clientResponse = "Сведения о коллекции:\n" +
            " Тип: " + collectionManager.getCollection().getClass().toString() + "\n" +
            " Количество элементов: " + collectionManager.getCollection().size() + "\n" +
            " Дата последнего сохранения: " + (lastSaveTime == null ? "пока сохранений не происходило" : 
                lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString()) + "\n" +
            " Дата последней инициализации: " + (lastInitTime == null ? "инициализаций еще не было" : 
                lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString());

        return new ExecutionResponse(clientResponse);
    }
}
