package org.commands;

import org.models.Dragon;
import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Класс команды для очистки коллекции
 */
public class Clear extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Clear(Console console, CollectionManager collectionManager) {
        super("clear", "clears collection");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнение команды
     * @param arguments Аргументы
     * @return результат исполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        if (!arguments[1].isEmpty())
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nФормат: '" + getName() + "'");
        
        // Server-side logging (in English)
        System.out.println("Collection is ready to be cleared");
        
        // Clear the collection
        collectionManager.clear();
        
        // Server-side confirmation (in English)
        System.out.println("Collection has been cleared successfully");
        
        // Return response to client (in Russian)
        return new ExecutionResponse(true, "Коллекция очищена");
    }
}
