package org.utility;

import org.clientCommand.ExecuteScript;
import org.clientCommand.Exit;
import org.clientCommand.Help;
import org.managers.NetworkManager;
import org.commands.CommandType;
import org.commands.Container;
import org.managers.Ask;
import org.models.Dragon;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Класс исполнения программы
 */
public class Runner {

    private Console console;
    private Map<CommandType,String[]> commands;
    private final List<String> scriptStack = new ArrayList<>();
    private int lengthRecursion = -1;
    private NetworkManager networkManager;


    public Runner(NetworkManager networkManager, Console console, Map<CommandType,String[]> commands) {
        this.console = console;
        this.networkManager = networkManager;
        this.commands=commands;
    }

    /**
     * Интерактивный режим
     */
    public void interactiveMode() {
        try {
            ExecutionResponse commandStatus;
            String[] userCommand = {"", ""};

            while (true) {
                try {
                    console.prompt();
                    userCommand = (console.readln().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                    console.println(userCommand[0]);
                    commandStatus = launchCommand(userCommand);
                    if (commandStatus.getMessage().equals("exit")) break;
                    console.println(commandStatus.getMessage());
                } catch (NoSuchElementException e) {
                    console.printError("Ошибка при чтении команды. Попробуйте снова.");
                    console.selectConsoleScanner();
                } catch (IllegalStateException e) {
                    console.printError("Непредвиденная ошибка при выполнении команды. Попробуйте снова.");
                    console.selectConsoleScanner();
                }
            }
        } catch (Exception e) {
            console.printError("Критическая ошибка. Завершение работы.");
            System.exit(1);
        }
    }

    /**
     * Функция проверки скрипта на наличие рекурсии
     *
     * @param argument      название запускаемого скрипта
     * @param scriptScanner сканер скрипта
     * @return true, если скрипт можно запускать
     */
    private boolean checkRecursion(String argument, Scanner scriptScanner) {
        var recStart = -1;
        var i = 0;
        for (String script : scriptStack) {
            i++;
            if (argument.equals(script)) {
                if (recStart < 0) recStart = i;
                if (lengthRecursion < 0) {
                    console.selectConsoleScanner();
                    console.println("Была замечена рекурсия.\nВведите максимальную глубину рекурсии (0..500)");
                    while (lengthRecursion < 0 || lengthRecursion > 500) {
                        try {
                            console.print("> ");
                            lengthRecursion = Integer.parseInt(console.readln().trim());
                            if (lengthRecursion < 0 || lengthRecursion > 500) {
                                console.println("Длина не распознана");
                            }
                        } catch (NumberFormatException e) {
                            console.println("Длина не распознана");
                        }
                    }
                    console.selectFileScanner(scriptScanner);
                }
                if (i > recStart + lengthRecursion || i > 500)
                    return false;
            }
        }
        return true;
    }

    /**
     * Режим работы скрипта
     * @param argument название файла со скриптом
     * @return возвращает ответ о выполнении скрипта
     */
    private ExecutionResponse scriptMode(String argument) {
        String[] userCommand = {"", ""};
        StringBuilder executionOutput = new StringBuilder();

        if (!new File(argument).exists()) return new ExecutionResponse(false, "Файл не существет!");
        if (!Files.isReadable(Paths.get(argument))) return new ExecutionResponse(false, "Прав для чтения нет!");

        scriptStack.add(argument);
        try (Scanner scriptScanner = new Scanner(new File(argument))) {

            ExecutionResponse commandStatus;

            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            console.selectFileScanner(scriptScanner);
            do {
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (console.isCanReadln() && userCommand[0].isEmpty()) {
                    userCommand = (console.readln().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                executionOutput.append(console.getPrompt() + String.join(" ", userCommand) + "\n");
                var needLaunch = true;
                if (userCommand[0].equals("execute_script")) {
                    needLaunch = checkRecursion(userCommand[1], scriptScanner);
                }
                commandStatus = needLaunch ? launchCommand(userCommand) : new ExecutionResponse("Превышена максимальная глубина рекурсии");
                if (userCommand[0].equals("execute_script")) console.selectFileScanner(scriptScanner);
                executionOutput.append(commandStatus.getMessage() + "\n");
            } while (commandStatus.getExitCode() && !commandStatus.getMessage().equals("exit") && console.isCanReadln());

            console.selectConsoleScanner();
            if (!commandStatus.getExitCode() && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                executionOutput.append("Проверьте скрипт на корректность введенных данных\n");
            }

            return new ExecutionResponse(commandStatus.getExitCode(), executionOutput.toString());
        } catch (FileNotFoundException exception) {
            return new ExecutionResponse(false, "Файл со скриптом не найден");
        } catch (NoSuchElementException exception) {
            return new ExecutionResponse(false, "Файл со скриптом пуст");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка");
            System.exit(0);
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return new ExecutionResponse("");
    }

    /**
     * Функиция загрузки команды
     * @param userCommand загружаемая команда
     * @return возвращает ответ о выполнении программы
     */
    private ExecutionResponse launchCommand(String[] userCommand) {
        if (userCommand[0].equals("")) return new ExecutionResponse("");
        var command = CommandType.getByString(userCommand[0]);
        if(!commands.containsKey(command)) {
            command=null;
        }

        if (command == null)
            return new ExecutionResponse(false, "Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");

        try {
            switch (userCommand[0]) {
                case "execute_script" -> {
                    ExecutionResponse tmp = new ExecuteScript(console).apply(userCommand);
                    if (!tmp.getExitCode()) return tmp;
                    ExecutionResponse tmp2 = scriptMode(userCommand[1]);
                    return new ExecutionResponse(tmp2.getExitCode(), tmp.getMessage() + "\n" + tmp2.getMessage().trim());
                }
                default -> {
                    byte[] bytes;
                    if (command == CommandType.Add) {
                        try {
                            Dragon dragon = Ask.askDragon(console, 0L);
                            if (dragon == null) {
                                return new ExecutionResponse(false, "Ошибка при создании объекта. Проверьте введенные данные.");
                            }
                            bytes = NetworkManager.serializeData(new Container(command, dragon.toString()));
                        } catch (Ask.AskBreak e) {
                            console.println("Отмена...");
                            return new ExecutionResponse(false, "Операция отменена");
                        } catch (NoSuchElementException e) {
                            console.selectConsoleScanner();
                            return new ExecutionResponse(false, "Ошибка при чтении данных. Попробуйте снова.");
                        } catch (IllegalStateException e) {
                            console.selectConsoleScanner();
                            return new ExecutionResponse(false, "Непредвиденная ошибка при вводе данных. Попробуйте снова.");
                        }
                    } else if (command == CommandType.UpdateId) {
                        try {
                            Dragon dragon = Ask.askDragon(console, Long.parseLong(userCommand[1]));
                            if (dragon == null) {
                                return new ExecutionResponse(false, "Ошибка при создании объекта. Проверьте введенные данные.");
                            }
                            bytes = NetworkManager.serializeData(new Container(command, dragon.toString()));
                        } catch (Ask.AskBreak e) {
                            console.println("Отмена...");
                            return new ExecutionResponse(false, "Операция отменена");
                        } catch (NoSuchElementException e) {
                            console.selectConsoleScanner();
                            return new ExecutionResponse(false, "Ошибка при чтении данных. Попробуйте снова.");
                        } catch (IllegalStateException e) {
                            console.selectConsoleScanner();
                            return new ExecutionResponse(false, "Непредвиденная ошибка при вводе данных. Попробуйте снова.");
                        } catch (NumberFormatException e) {
                            return new ExecutionResponse(false, "Неверный формат ID");
                        }
                    } else if (command == CommandType.Help) {
                        console.println(new Help(console,commands).apply(userCommand).getMessage());
                        return new ExecutionResponse(false,"");
                    } else if (command == CommandType.Exit) {
                        bytes = NetworkManager.serializeData(new Container(CommandType.Save, ""));
                        networkManager.sendData(bytes);
                        return new Exit(console).apply(userCommand);
                    } else {
                        bytes = NetworkManager.serializeData(new Container(command, userCommand[1]));
                    }
                    
                    networkManager.sendData(bytes);
                    var data = networkManager.receiveData(5069);
                    return NetworkManager.deserializeData(data);
                }
            }
        } catch (Exception e) {
            console.printError("Произошла ошибка при выполнении команды: " + e.getMessage());
            return new ExecutionResponse(false, "Ошибка выполнения команды. Попробуйте снова.");
        }
    }
}