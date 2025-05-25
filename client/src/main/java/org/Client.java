package org;

import org.managers.NetworkManager;
import org.utility.Runner;
import org.commands.CommandType;
import org.managers.Ask;
import org.utility.StandartConsole;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Client {
    public static void main(String[] args) throws Ask.AskBreak, IOException {

        var console = new StandartConsole();
        NetworkManager networkManager = new NetworkManager(8000);

        while (!networkManager.init(args)){}
        Map<CommandType,String[]> commands = new HashMap<>();
        commands.put(CommandType.Add, new String[]{"add", "Добавить новый елемент в коллекцию"});
        commands.put(CommandType.Clear, new String[]{"clear", "Очистить коллекцию"});
        commands.put(CommandType.Exit, new String[]{"exit", "Завершить выполнение программы"});
        commands.put(CommandType.FilterByDescription, new String[]{"filter_by_description {description}", "Вывести элементы коллекции с заданным описанием"});
        commands.put(CommandType.FilterContainsName, new String[]{"filter_contains_name {name}", "Вывести элементы коллекции, имя которых совпадает с заданным"});
        commands.put(CommandType.Help, new String[]{"help", "Вывести справку по доступным командам"});
        commands.put(CommandType.Info, new String[]{"info", "Вывести информацию о коллекции"});
        commands.put(CommandType.RemoveByDescription, new String[]{"remove_by_description {description}", "Удалить все элементы с заданным описанием"});
        commands.put(CommandType.RemoveId, new String[]{"remove_id {ID}", "Удалить из коллекции элемент с заданным ID"});
        commands.put(CommandType.RemoveIndex, new String[]{"remove_index {index}", "Удалить из коллекции элемент с заданным порядковым номером"});
        commands.put(CommandType.RemoveLower, new String[]{"remove_lower {object}", "Удаляет из коллекции все объекты меньше заданного"});
        commands.put(CommandType.Show, new String[]{"show", "Вывести текущую коллекцию в консоль"});
        commands.put(CommandType.Shuffle, new String[]{"shuffle", "Перемешать элементы коллекции"});
        commands.put(CommandType.UpdateId, new String[]{"update_id {ID}", "Обновить поля элемента коллекции с заданным ID"});

        new Runner(networkManager, console, commands).interactiveMode();
    }
}
