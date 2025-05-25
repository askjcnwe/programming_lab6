package org.managers;

import org.models.Dragon;
import org.utility.Sortable;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс для управления коллекцией
 */
public class CollectionManager implements Serializable, Sortable {
    private long currentId = 1;
    private Map<Long, Dragon> dadada = new HashMap<>();
    private ArrayList<Dragon> collection = new ArrayList<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final DumpManager dumpManager;


    public CollectionManager(DumpManager dumpManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dumpManager = dumpManager;
    }

    /**
     * Метод для сброса счетчика ID
     */
    public void resetIdCounter() {
        currentId = 1;
    }

    /**
     * Метод для очистки коллекции
     */
    public void clear() {
        collection = new ArrayList<>();
        dadada = new HashMap<>();
        currentId = 1;
    }

    /**
     * Метод для получения времени последней инициализации
     * @return время
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * Метод для получения времени последнего сохранения коллекции
     * @return время
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * Метод для получения коллекции
     * @return коллекция
     */
    public ArrayList<Dragon> getCollection() {
        return collection;
    }

    /**
     * Метод для получения следующего свободного  ID
     * @return id
     */
    public long getNextId() {
        // Находим максимальный ID в коллекции используя Stream API
        return collection.stream()
                .mapToLong(Dragon::getID)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Метод для получчения элемента коллекции по ID
     * @param id ID
     * @return элемент
     */
    public Dragon byId(long id) { 
        return dadada.get(id); 
    }

    /**
     * Метод для проверки на наличие объекта в коллекции
     * @param e объект
     * @return true, если объект содержится в коллекции
     */
    public boolean isContain(Dragon e) { 
        return e != null && byId(e.getID()) != null;
    }

    /**
     * Метод для установки в текущее id следующего свободного
     * @return текущее id
     */
    public long getFreeId() {
        currentId = getNextId();
        return currentId;
    }

    /**
     * Метод для добавления объекта в коллекцию
     * @param dragon объект
     * @return результат о добавлении объекта
     */
    public boolean add(Dragon dragon) {
        if (dragon == null) return false;
        
        long nextId = getFreeId();
        
        Dragon newDragon = new Dragon(
            nextId,
            dragon.getName(),
            dragon.getCoordinates(),
            dragon.getCreationDate(),
            dragon.getAge(),
            dragon.getDescription(),
            dragon.getWingspan(),
            dragon.getCharacter(),
            dragon.getCave()
        );
        
        if (isContain(newDragon)) return false;
        
        dadada.put(nextId, newDragon);
        collection.add(newDragon);
        currentId = nextId + 1;
        update();
        return true;
    }

    /**
     * Метод для удаления элемента их коллекции по id
     * @param id ID
     * @return результат попытки удаления
     */
    public boolean remove(long id) {
        var a = byId(id);
        if (a == null) return false;

        collection = collection.stream()
            .filter(dragon -> dragon.getID() != id)
            .collect(Collectors.toCollection(ArrayList::new));
            
        dadada.remove(id);
        
        if (collection.isEmpty()) {
            resetIdCounter();
        }
        
        update();
        return true;
    }

    /**
     * Метод для обновления порядка элементов в коллекции
     */
    public void update() {
        sort();
    }

    /**
     * Метод для инициализации коллекции из файла
     * @return результат
     * @throws FileNotFoundException если файл не найден
     * @throws IOException исключение
     */
    public boolean init() throws FileNotFoundException, XMLStreamException {
        collection.clear();
        dadada.clear();
        dumpManager.readCollection();
        lastInitTime = LocalDateTime.now();
        
        // Используем Stream API для проверки и добавления элементов
        boolean hasConflict = collection.stream()
            .anyMatch(e -> byId(e.getID()) != null);
            
        if (hasConflict) {
            collection.clear();
            dadada.clear();
            return false;
        }
        
        collection.forEach(e -> dadada.put(e.getID(), e));
        currentId = getNextId();
        update();
        return true;
    }

    /**
     * Метод для сохранения коллекции в файл
     * @throws IOException исключение
     */
    public void saveCollection() throws IOException {
        sortById();
        dumpManager.writeCollection(collection);
        lastSaveTime = LocalDateTime.now();
    }

    /**
     * Метод для загрузки колллекции из файла
     * @return результат
     */
    public boolean loadCollection() {
        dadada.clear();
        try {
            collection = dumpManager.readCollection();
            lastInitTime = LocalDateTime.now();
            
            // Используем Stream API для валидации и загрузки элементов
            boolean allValid = collection.stream().allMatch(Dragon::validate);
                
            if (!allValid) {
                collection.clear();
                return false;
            }
            
            collection.forEach(e -> dadada.put(e.getID(), e));
            currentId = getNextId();
            update();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Метод для сортировки коллекции в соответствии с размером объектов
     */
    @Override
    public void sort() {
        collection = collection.stream()
            .sorted((d1, d2) -> Integer.compare(d1.toString().length(), d2.toString().length()))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Метод для сортировки коллекции по ID
     */
    public void sortById(){
        collection = collection.stream().sorted((d1, d2) -> Long.compare(d1.getID(), d2.getID())).collect(Collectors.toCollection(ArrayList::new));

    }

    /**
     * Метод для преобразования коллекции в строку
     * @return строка
     */
    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        return collection.stream()
            .map(Dragon::toDisplayString)
            .collect(Collectors.joining("\n\n"))
            .trim();
    }
}




