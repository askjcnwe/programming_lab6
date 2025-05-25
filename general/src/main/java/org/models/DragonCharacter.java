package org.models;

/**
 * Характер дракона
 */
public enum DragonCharacter {
    CUNNING,
    WISE,
    EVIL,
    GOOD,
    CHAOTIC;

    /**
     * Метод для преобразования всех констант в строчный вид
     * @return строка из всех констант
     */
    public static String names(){
        StringBuilder nameList = new StringBuilder();
        for (var dragonCharacter : values()) {
            nameList.append(dragonCharacter.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}