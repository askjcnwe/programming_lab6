package org.managers;

import org.models.Coordinates;
import org.models.Dragon;
import org.models.DragonCave;
import org.models.DragonCharacter;
import org.utility.Console;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для чтения и записи в файл
 */
public class DumpManager implements Serializable {
    private final Console console;
    private final String filePath;

    public DumpManager(String filePath, Console console){
        this.filePath = filePath;
        this.console = console;
        
    }

    /**
     * Метод для чтения коллекции из файла
     * @return коллекция
     * @throws FileNotFoundException если файл не найден
     * @throws XMLStreamException исключение
     * @throws NumberFormatException неверный формат входных данных
     * @throws NullPointerException ссылка на null
     * @throws IllegalArgumentException запрещенный аргумент
     */
    public ArrayList<Dragon> readCollection() throws FileNotFoundException, XMLStreamException
    {

        ArrayList<Dragon> dragons = new ArrayList<>();
        long id = -1;
        String name = null;
        Coordinates coordinates = null;
        Date creationDate = null;
        long age = -70;
        String description= null;
        Double wingspan = null;
        DragonCharacter character= null;
        DragonCave cave= null;
        int iter = 0;
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader reader = factory.createXMLEventReader(new FileInputStream(filePath));
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case "id":
                            iter++;
                            event = reader.nextEvent();
                            try {
                                id = Long.parseLong(event.asCharacters().getData());
                                break;
                            } catch (NumberFormatException e) {
                            }
                            console.println("ID: " + id);
                            break;
                        case "name":
                            iter++;
                            event = reader.nextEvent();
                            name = event.asCharacters().getData();
                            console.println("Name: " + name);
                            break;
                        case "coordinates":
                            iter++;
                            event = reader.nextEvent();
                            try {
                                String value = event.asCharacters().getData();
                                String[] cords = value.split(";");
                                coordinates = new Coordinates(Integer.parseInt(cords[0]), Float.parseFloat(cords[1]));
                            } catch (NumberFormatException e) {
                            }
                            console.println("Coordinates: " + coordinates);
                            break;
                        case "creationDate":
                            iter++;
                            event = reader.nextEvent();
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                                creationDate = sdf.parse(event.asCharacters().getData());
                            } catch (Exception e) {
                                creationDate = null;
                            }
                            console.println("CreationDate: " + creationDate);
                            break;
                        case "age":
                            iter++;
                            event = reader.nextEvent();
                            try {
                                age = Long.parseLong(event.asCharacters().getData());
                            } catch (NumberFormatException e) {
                            }
                            console.println("Age: " + age);
                            break;
                        case "description":
                            iter++;
                            event = reader.nextEvent();
                            description = event.asCharacters().getData();
                            console.println("Description: " + description);
                            break;
                        case "wingspan":
                            iter++;
                            event = reader.nextEvent();
                            try {
                                wingspan = Double.parseDouble(event.asCharacters().getData());
                            } catch (NumberFormatException e) {
                            }
                            console.println("Wingspan: " + wingspan);
                            break;
                        case "character":
                            iter++;
                            event = reader.nextEvent();
                            try {
                                character = DragonCharacter.valueOf(event.asCharacters().getData());
                                console.println("Character: " + character);
                            } catch (NullPointerException | IllegalArgumentException e) {
                            }
                            break;
                        case "cave":
                            iter++;
                            event = reader.nextEvent();
                            try {
                                cave = new DragonCave(Double.parseDouble(event.asCharacters().getData()));
                                console.println("Cave: " + cave);
                                break;
                            } catch (NumberFormatException e) {
                            }
                            break;
                    }

                }
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals("object")) {
                        if (iter == 9) {
                            console.println("Add dragon");
                            dragons.add(new Dragon(id, name, coordinates, creationDate, age, description, wingspan, character, cave));
                            iter =0;
                        } else {
                            console.printError("Проверьте структуру вводимого файла");
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }
        } catch (FileNotFoundException | XMLStreamException e) {console.printError("Ошибка при чтении файла");}
        return dragons;

    }

    /**
     * Метод для записи коллекции в файл
     * @param dragons коллекция
     * @throws IOException исключение
     */
    public void writeCollection(ArrayList<Dragon> dragons) throws IOException{
          try {
              FileOutputStream fos = new FileOutputStream(filePath);
              String startLine = "<?xml version=\"1.0\"?>\n";

              var openTag = "\t<object>\n".getBytes();
              var closeTag = "\t</object>\n".getBytes();
              fos.flush();
              fos.write(startLine.getBytes());
              fos.write("<dragons>\n".getBytes());
              for (Dragon dragon : dragons) {
                  fos.write(openTag);
                  fos.write(("\t\t<id>" + dragon.getID() + "</id>\n").getBytes());
                  fos.write(("\t\t<name>" + dragon.getName() + "</name>\n").getBytes());
                  fos.write(("\t\t<coordinates>" + dragon.getCoordinates().toString() + "</coordinates>\n").getBytes());
                  fos.write(("\t\t<creationDate>" + dragon.getCreationDate() + "</creationDate>\n").getBytes());
                  fos.write(("\t\t<age>" + dragon.getAge() + "</age>\n").getBytes());
                  fos.write(("\t\t<description>" + dragon.getDescription() + "</description>\n").getBytes());
                  fos.write(("\t\t<wingspan>" + dragon.getWingspan() + "</wingspan>\n").getBytes());
                  fos.write(("\t\t<character>" + dragon.getCharacter().toString() + "</character>\n").getBytes());
                  fos.write(("\t\t<cave>" + dragon.getCave().getTreasures() + "</cave>\n").getBytes());
                  fos.write(closeTag);
              }
              fos.write("</dragons>".getBytes());
              fos.close();

          } catch (IOException e) {console.printError(e);}
    }
}

    