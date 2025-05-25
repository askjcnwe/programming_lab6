package org.managers;


import org.models.Coordinates;
import org.models.Dragon;
import org.models.DragonCave;
import org.models.DragonCharacter;
import org.utility.Console;

import java.util.NoSuchElementException;

/**
 * Класс для запроса данных из консоли
 */
public class Ask {
    public static class AskBreak extends Exception {}

    /**
     * Запрос объекта Dragon
     * @param console консоль
     * @param id уникальный номер дракона
     * @return Возвращает объект Dragon
     * @throws AskBreak Исключение, возникающее при принудительном выходе из запроса
     */
    public static Dragon askDragon(Console console, long id) throws AskBreak{
        try {
            String name;
            while (true) { 
                console.print("name: ");
                name = console.readln().trim();
                if (name.equals("exit")) throw new AskBreak();
                if (!name.isEmpty()) break;
            }
            var coordinates = askCoordinates(console);
            var age = askAge(console); 
            var description = askDescription(console);
            var wingspan = askWingspan(console); 
            var character = askCharacter(console); 
            var cave = askCave(console); 
            
            // Для нового дракона используем ID = 1, сервер его перезапишет
            long dragonId = id == 0 ? 1 : id;
            return new Dragon(dragonId, name, coordinates, age, description, wingspan, character, cave);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка при чтении");
            return null;
        }
    }

    /**
     * Метод для чтения координат объекта
     * @param console консоль
     * @return координаты объекта
     * @throws AskBreak Исключение, возникающее при принудительном выходе из запроса
     */
    public static Coordinates askCoordinates(Console console) throws AskBreak {
        try {
            int x;
            while (true) {
                console.print("coords x: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {x = Integer.parseInt(line); break; } catch(NumberFormatException e){}
                }
            }

            float y;
            while (true) {
                console.print("coords y: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try { y = Float.parseFloat(line); break; } catch(NumberFormatException e) {}
                }
            }
            return new Coordinates(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка при чтении");
            return null;
        }
    }

    /**
     * Метод для запроса возраста объекта
     * @param console консоль
     * @return возраст объекта
     * @throws AskBreak Исключение, возникающее при принудительном выходе из запроса
     */
    public static Long askAge(Console console) throws AskBreak{
        try {
            long age;
            while (true) { 
                console.print("age: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if(!line.equals("")) {
                    try {age = Long.parseLong(line); if (age > 0) break; } catch (NumberFormatException e){}
                } else return null;
            }
            return age;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка при чтении");
            return null;
        }
    }

    /**
     * Метод для запроса описания объекта
     * @param console консоль
     * @return описание объекта
     * @throws AskBreak Исключение, возникающее при принудительном выходе из запроса
     */
    public static String askDescription(Console console) throws AskBreak {
        try {
            String desc;
            while (true) { 
                console.print("description: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")){
                    try{ 
                        desc = String.valueOf(line);
                        break;
                    } catch (NullPointerException | IllegalArgumentException e) {}
                }
            }
            return desc;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка при чтении");
            return null;
            }
    }

    /**
     * Метод для запроса размаха крыльев обхекта
     * @param console консоль
     * @return размах крыльев объекта
     * @throws AskBreak Исключение, возникающее при принудительном выходе из запроса
     */
    public static Double askWingspan(Console console) throws AskBreak {
        try {
            Double ws;
            while (true) { 
                console.print("wingspan: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")){
                    try {ws = Double.parseDouble(line); if (ws > 0) break;} catch (NumberFormatException e) {}
                } else return null;
            }
            return ws;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибкка при чтении");
            return null;
        }
    }

    /**
     * Метод для запроса типа объекта
     * @param console консоль
     * @return тип объекта
     * @throws AskBreak Исключение, возникающее при принудительном выходе из запроса
     */
    public static DragonCharacter askCharacter(Console console) throws AskBreak{
        try {
            DragonCharacter character;
            while (true){
                console.print("character from '" + DragonCharacter.names() + "': " );
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")){
                    try {
                        character = DragonCharacter.valueOf(line); break;
                    } catch (NullPointerException | IllegalArgumentException e) {}
                } else return null;
            }    
            return character;        
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    /**
     * Метод для запроса информации о пещере объекта
     * @param console консоль
     * @return информация о пещере объекта
     * @throws AskBreak Исключение, возникающее при принудительном выходе из запроса
     */
    public static DragonCave askCave(Console console) throws AskBreak{
        try {
            Double cave;
            while (true) {
                console.print("number of treasures: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")){
                    try {cave = Double.parseDouble(line); break ;} catch(NumberFormatException e) {} 
                } 
            }
            return new DragonCave(cave);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка при чтении");
            return null;
        }
    }

}
