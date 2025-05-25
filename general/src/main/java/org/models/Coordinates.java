package org.models;

import org.utility.Validatable;
import java.io.Serializable;

/**
 * Класс координат
 */
public class Coordinates implements Serializable {
    private int x;
    private float y;

    public Coordinates(int x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Получить координату по x
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Получить координату по y
     * @return y
     */
    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return x + ";" + y;
    }


}