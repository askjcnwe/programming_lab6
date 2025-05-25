package org.commands;

import java.io.Serializable;

/**
 * Перечисление для хранения всех типов команд
 */
public enum CommandType implements Serializable {
    Add("add"),
    Clear("clear"),
    Exit("exit"),
    FilterByDescription("filter_by_description"),
    FilterContainsName("filter_contains_name"),
    Help("help"),
    Info("info"),
    RemoveByDescription("remove_by_description"),
    RemoveId("remove_id"),
    RemoveIndex("remove_index"),
    RemoveLower("remove_lower"),
    Save("save"),
    Show("show"),
    Shuffle("shuffle"),
    UpdateId("update_id");

    private String type;

    private CommandType(String type) {
        this.type = type;
    }

    public String Type(){
        return type;
    }

    public static final long serialVersionUID = 14L;

    /**
     * Метод для выделения константы из строки
     * @param string строка с командой
     * @return соответствующяя константа
     */
    public static CommandType getByString(String string) {
        try {
            // Преобразуем строку в формат для enum
            // Например: "remove_index" -> "RemoveIndex"
            String[] parts = string.split("_");
            StringBuilder enumName = new StringBuilder();
            for (String part : parts) {
                if (!part.isEmpty()) {
                    enumName.append(part.substring(0, 1).toUpperCase())
                           .append(part.substring(1).toLowerCase());
                }
            }
            return CommandType.valueOf(enumName.toString());
        } catch (NullPointerException | IllegalArgumentException e) {
            return null;
        }
    }
}
