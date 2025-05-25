package org.models;

import org.utility.Element;
import org.utility.Validatable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Класс Дракона
 */
public class Dragon extends Element implements Validatable, Serializable{
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long age; //Значение поля должно быть больше 0
    private String description; //Поле не может быть null
    private Double wingspan; //Значение поля должно быть больше 0, Поле может быть null
    private DragonCharacter character; //Поле может быть null
    private DragonCave cave; //Поле может быть null
    
    private static long lastID;

    public Dragon(long id ,String name, Coordinates coordinates, Date creationDate, long age, String description, Double wingspan, DragonCharacter character, DragonCave cave){
        this.id = id;
        this.name= name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.age = age;
        this.description = description;
        this.wingspan = wingspan;
        this.character = character;
        this.cave = cave;

    }

    public Dragon(long id,String name, Coordinates coordinates, long age, String description, Double wingspan, DragonCharacter character, DragonCave cave){
        this(id, name, coordinates, new Date(), age, description, wingspan, character, cave);

    }
    /**
     * Метод для получения объекта из массива строк
     * @param data Массив строк, в котором содержатся значения для полей объекта Dragon
     * @return Объект класса Dragon, созданный из элементов массива data
     */
    public static Dragon fromArray(String[] data){
        try {
            if (data == null || data.length < 9) {
                System.out.println("Недостаточно данных");
                return null;
            }

            Long id;
            try {
                id = Long.parseLong(data[0]);
                if (id <= 0) {
                    System.out.println("ID должен быть положительным");
                    return null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка парсинга ID: " + e.getMessage());
                return null;
            }

            String name = data[1];
            if (name == null || name.isEmpty()) {
                System.out.println("Имя не может быть пустым");
                return null;
            }

            Coordinates coordinates = null;
            try {
                String[] coords = data[2].split(";");
                if (coords.length != 2) {
                    System.out.println("Неверный формат координат. Ожидается: x;y");
                    return null;
                }
                int x = Integer.parseInt(coords[0].trim());
                float y = Float.parseFloat(coords[1].trim());
                coordinates = new Coordinates(x, y);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка парсинга координат: " + e.getMessage());
                return null;
            }

            Date creationDate = new Date();
            if (data[3] != null && !data[3].isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                    Date parsedDate = sdf.parse(data[3]);
                    if (parsedDate != null) {
                        creationDate = parsedDate;
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка парсинга даты, используется текущая дата: " + e.getMessage());
                }
            }

            Long age;
            try {
                age = Long.parseLong(data[4]);
                if (age <= 0) {
                    System.out.println("Возраст должен быть положительным");
                    return null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка парсинга возраста: " + e.getMessage());
                return null;
            }

            String description = data[5];
            if (description == null || description.isEmpty()) {
                System.out.println("Описание не может быть пустым");
                return null;
            }

            Double wingspan = null;
            if (!data[6].isEmpty()) {
                try {
                    wingspan = Double.parseDouble(data[6]);
                    if (wingspan <= 0) {
                        System.out.println("Размах крыльев должен быть положительным");
                        wingspan = null;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка парсинга размаха крыльев: " + e.getMessage());
                }
            }

            DragonCharacter character = null;
            if (!data[7].isEmpty()) {
                try {
                    character = DragonCharacter.valueOf(data[7].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка парсинга характера: " + e.getMessage());
                }
            }

            DragonCave cave = null;
            if (!data[8].isEmpty()) {
                try {
                    Double treasures = Double.parseDouble(data[8]);
                    if (treasures > 0) {
                        cave = new DragonCave(treasures);
                    } else {
                        System.out.println("Количество сокровищ должно быть положительным");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка парсинга количества сокровищ: " + e.getMessage());
                }
            }

            Dragon dragon = new Dragon(id, name, coordinates, creationDate, age, description, wingspan, character, cave);

            if (!dragon.validate()) {
                System.out.println("Созданный дракон не прошел валидацию");
                return null;
            }
            
            return dragon;

        } catch (Exception e) {
            System.out.println("Неожиданная ошибка при создании дракона: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Проверяет валдиность полей объекта
     * @return true, если поля объекта прошли проверку
     */
    @Override
    public boolean validate(){
        try {
            if (id <= 0) return false;
            if (name == null || name.isEmpty()) return false;
            if (coordinates == null) return false;
            if (creationDate == null) return false;
            if (age <= 0) return false;
            if (description == null) return false;
            if (wingspan != null && wingspan <= 0) return false;
            if (!cave.validate()) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Метод для преобразования объкта в строку
     * @return строка из полей объекта, разделенных символом '/'
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(Long.toString(this.id));
        str.append("/");
        str.append(name);
        str.append("/");
        str.append(this.coordinates != null ? this.coordinates.toString() : "");
        str.append("/");
        str.append(this.creationDate != null ? this.creationDate.toString() : new Date().toString());
        str.append("/");
        str.append(Long.toString(this.age));
        str.append("/");
        str.append(this.description != null ? this.description : "");
        str.append("/");
        str.append(this.wingspan != null ? this.wingspan.toString() : "");
        str.append("/");
        str.append(this.character != null ? this.character.toString() : "");
        str.append("/");
        str.append(this.cave != null ? this.cave.toString() : "");
        return str.toString();
    }

    /**
     * Метод для отображения объекта в читаемом виде
     * @return строка из полей объекта, разделенных символом переноса строки
     */
    public String toDisplayString(){
        StringBuilder str = new StringBuilder();
        str.append(Long.toString(this.id));
        str.append("\n");
        str.append(name);
        str.append("\n");
        str.append(this.coordinates != null ? this.coordinates.toString() : "");
        str.append("\n");
        str.append(this.creationDate != null ? this.creationDate.toString() : new Date().toString());
        str.append("\n");
        str.append(Long.toString(this.age));
        str.append("\n");
        str.append(this.description != null ? this.description : "");
        str.append("\n");
        str.append(this.wingspan != null ? this.wingspan.toString() : "");
        str.append("\n");
        str.append(this.character != null ? this.character.toString() : "");
        str.append("\n");
        str.append(this.cave != null ? this.cave.toString() : "");
        return str.toString();
    }

    /**
     * Метод для перевода объекта в массив строк
     * @param dragon Объект класса Dragon
     * @return массив строк
     */
    public static String[] toArray(Dragon dragon){
        var list = new ArrayList<String>();
        list.add(Long.toString(dragon.getID()));
        list.add(dragon.getName());
        list.add(dragon.getCoordinates() != null ? dragon.getCoordinates().toString() : "");
        list.add(dragon.getCreationDate() != null ? dragon.getCreationDate().toString() : new Date().toString());
        list.add(Long.toString(dragon.getAge()));
        list.add(dragon.getDescription() != null ? dragon.getDescription() : "");
        list.add(dragon.getWingspan() != null ? dragon.getWingspan().toString() : "");
        list.add(dragon.getCharacter() != null ? dragon.getCharacter().toString() : "");
        list.add(dragon.getCave() != null ? dragon.getCave().toString() : "");
        return list.toArray(new String[0]);
    }

    /**
     * Метод для вывода объекта в форматированном виде
     * @return форматированная строка
     */
    public String toFormattedString(){
        return "Dragon " + Long.toString(this.getID()) + "\n" +
                "name: " + this.getName() + ", \n" +
                "coordinates: " + this.getCoordinates().toString() + ", \n" +
                "creation date: " + this.getCreationDate().toString() + ", \n" +
                "age: " + Long.toString(this.getAge()) + ", \n" +
                "description: " + this.getDescription() + ", \n" +
                "wingspan: " + (this.getWingspan() != null ? this.getWingspan().toString() : "не задано") + ", \n" +
                "character: " + (this.getCharacter() != null ? this.getCharacter().toString() : "не задано") + ", \n" +
                "cave: " + (this.getCave() != null ? this.getCave().toString() : "не задано");
    }
    @Override
    public long getID(){
        return id;
    }

    
    public String getName(){
        return name;
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }

    public Date getCreationDate(){
        return creationDate;
    }

    public long getAge(){
        return age;
    }

    public String getDescription(){
        return description;
    }

    public Double getWingspan(){
        return wingspan;
    }

    public DragonCharacter getCharacter(){
        return character;
    }
    public DragonCave getCave(){
        return cave;
    }


    @Override
    public int compareTo(Element o) {
        return (int) (this.id - o.getID());
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dragon that = (Dragon) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, name, coordinates, creationDate, age, description, wingspan, character, cave);
    }
}