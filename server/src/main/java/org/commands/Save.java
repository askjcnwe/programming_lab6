package org.commands;

import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;

import java.io.IOException;

/**
 * Класс для команды сохранения коллекции в заданный файл
 */
public class Save extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Save(Console console, CollectionManager collectionManager) {
        super("save", "load collection into a file");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнение команды
     * @param arguments Аргументы
     * @return результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            System.out.println("Error: Command doesn't accept any arguments");
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nФормат: '" + getName() + "'");
        }

        System.out.println("Attempting to save collection to file...");

        try {
            collectionManager.saveCollection();
            System.out.println("Collection successfully saved to file");
            return new ExecutionResponse(true, "Коллекция успешно сохранена");
        } catch (IOException e) {
            System.out.println("Error occurred while saving collection: " + e.getMessage());
            return new ExecutionResponse(false, "Произошла ошибка при сохранении коллекции");
        }
    }

}
