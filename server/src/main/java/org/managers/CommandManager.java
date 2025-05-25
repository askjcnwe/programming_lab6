package org.managers;

import org.commands.Command;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для управления командами
 */
public class CommandManager {
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final List<String> commandHistory = new ArrayList<>();

    /**
     * Метод для регистрации команды
     * @param commandName имя команды
     * @param command экземпляр команды
     */
    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * Метод для получения отображения всех зарегистрированных команд
     * @return команы
     */
    public Map<String, Command> getCommands() {
        return commands;
    }

    public void addToHistory(String command) {
        commandHistory.add(command);
    }
}