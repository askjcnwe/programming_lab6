package org.models;
import org.utility.Validatable;
import java.io.Serializable;

/**
 * Класс пещеры дракона
 */
public class DragonCave implements Validatable, Serializable {
    private Double numberOfTreasures; //Поле может быть null, Значение поля должно быть больше 0

    public DragonCave(Double numberOfTreasures){
        this.numberOfTreasures = numberOfTreasures;
    }

    public DragonCave(){
        this(null);
    }

    @Override
    public String toString(){
        return numberOfTreasures != null ? numberOfTreasures.toString() : "";
    }

    /**
     * Класс для проверки валидности объекта
     * @return true, если объект поршел валидацию
     */
    @Override
    public boolean validate(){
        return (numberOfTreasures == null || numberOfTreasures > 0);
    }

    public Double getTreasures(){
        return numberOfTreasures;
    }
}