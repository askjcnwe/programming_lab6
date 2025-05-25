package org.commands;

import org.utility.Console;
import org.utility.ExecutionResponse;
import org.managers.CollectionManager;

/**
 * Класс для команды удаления всех элементов коллекции меньше заданного
 */
public class RemoveLower extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    public RemoveLower(Console console, CollectionManager collectionManager) {
        super("remove_lower {object}", "Удаляет все объекты меньше заданного");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    /**
     * Исполнение команды
     * @param args Аргументы
     * @return результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] args){
        if (args[1].isEmpty())
            return new ExecutionResponse(false, "Неправильное количество аргументов \nФормат: '" + getName() + "'");
        long id = -1;
        try {
            id = Long.parseLong(args[1].trim());
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "wrong id");
        }
        if (collectionManager.byId(id) == null || !collectionManager.getCollection().contains(collectionManager.byId(id))) 
            return new ExecutionResponse(false, "wrong id");
            
        for (var dragon : collectionManager.getCollection()) {
            if (dragon.getID() < id){
                collectionManager.remove(dragon.getID());
                collectionManager.update();
            }
        }
        return new ExecutionResponse(true, "success");
    }

}
