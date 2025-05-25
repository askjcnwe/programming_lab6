package org.commands;

import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;
import org.models.Dragon;

import java.util.ArrayList;
import java.util.Collections;
/**
 * Класс для команды для перемешивания элементов коллекции
 */
public class Shuffle extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public Shuffle(Console console, CollectionManager collectionManager) {
        super("shuffle", "перемешивает элементы коллекции в выводе");
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
        if (!arguments[1].isEmpty())
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        
        // Создаем копию коллекции для перемешивания
        ArrayList<Dragon> shuffledCollection = new ArrayList<>(collectionManager.getCollection());
        
        // Перемешиваем копию
        Collections.shuffle(shuffledCollection);
        
        // Формируем строку с перемешанной коллекцией
        if (shuffledCollection.isEmpty()) return new ExecutionResponse(true, "Коллекция пуста!");
        
        StringBuilder info = new StringBuilder();
        info.append("Перемешанная коллекция:\n\n");
        for (var dragon : shuffledCollection) {
            info.append(dragon.toDisplayString()).append("\n\n");
        }
        
        return new ExecutionResponse(true, info.toString().trim());
    }
}