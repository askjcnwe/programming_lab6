package org;

import org.commands.*;
import org.managers.CollectionManager;
import org.managers.CommandManager;
import org.managers.DumpManager;
import org.managers.NetworkManager;
import org.utility.Console;
import org.utility.ExecutionResponse;
import org.utility.StandartConsole;


public class Server {
    static String[] userCommand = new String[2];
    static byte arr[] = new byte[5069];
    static int len = arr.length;

    public static void main(String[] args) {
        var console = new StandartConsole();
        if (args.length == 0) {
            console.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(0);
        }
        var dumpManager = new DumpManager(args[0], console);
        var collectionManager = new CollectionManager(dumpManager);

        if (!collectionManager.loadCollection()) {
            System.err.println("Не удалось загрузить коллекцию.");
            System.exit(1);
        }
        var networkManager = new NetworkManager(17534, 800);
        if (!networkManager.init()) {
            System.err.println("Не удалось инициализировать сетевое подключение.");
            System.exit(1);
        }
        collectionManager.sort();
        var commandManager = new CommandManager() {{
            register("add", new Add(console, collectionManager));
            register("clear", new Clear(console, collectionManager));
            register("filter_by_description", new FilterByDescription(console, collectionManager));
            register("filter_contains_name", new FilterContainsName(console, collectionManager));
            register("info", new Info(console, collectionManager));
            register("remove_by_description", new RemoveByDescription(console, collectionManager));
            register("remove_id", new RemoveID(console, collectionManager));
            register("remove_index", new RemoveIndex(console, collectionManager));
            register("remove_lower", new RemoveLower(console, collectionManager));
            register("save", new Save(console, collectionManager));
            register("show", new Show(console, collectionManager));
            register("shuffle", new Shuffle(console, collectionManager));
            register("update_id", new UpdateID(console, collectionManager));
        }};

        System.out.println("Сервер готов к приему команд.");
        run(networkManager, console, commandManager);
    }

    public static void run(NetworkManager networkManager, Console console, CommandManager commandManager) {
        while (true) {
            try {
                byte[] receivedData = networkManager.receiveData(len);
                if (receivedData != null) {
                    Container command = NetworkManager.deserialize(receivedData);
                    if (command != null) {
                        userCommand[0] = command.getCommandType().Type();
                        userCommand[1] = command.getArgs();
                        
                        System.out.println("Получена команда: " + userCommand[0]);
                        
                        var commandHandler = commandManager.getCommands().get(userCommand[0]);
                        ExecutionResponse response;
                        
                        if (userCommand[0].equals("")) {
                            response = new ExecutionResponse(false, "Получена пустая команда");
                        } else if (commandHandler == null) {
                            response = new ExecutionResponse(false, 
                                "Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
                        } else {
                            // Если это команда exit, сохраняем коллекцию перед выполнением
                            if (command.getCommandType() == CommandType.Exit) {
                                System.out.println("Получена команда exit от клиента, сохраняем коллекцию...");
                                try {
                                    commandManager.getCommands().get("save").apply(new String[]{"save", ""});
                                    System.out.println("Коллекция успешно сохранена");
                                } catch (Exception e) {
                                    System.err.println("Ошибка при сохранении коллекции: " + e.getMessage());
                                }
                            }
                            
                            response = commandHandler.apply(userCommand);
                            System.out.println("Команда '" + userCommand[0] + "' выполнена");
                        }
                        
                        networkManager.sendData(NetworkManager.serializer(response));
                    }
                }
                // Небольшая задержка для снижения нагрузки на процессор
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.err.println("Работа сервера была прервана");
                break;
            } catch (Exception e) {
                System.err.println("Ошибка при обработке команды: " + e.getMessage());

            }
        }
    }
}