package org.commands;

/**
 * Абстрактная команда, содержащая имя и описание
 */
public abstract class Command implements Executable, Descriable {
    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * получить имя команды
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * получить описание команды
     * @return description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Command [name = " + name + ", description = " + description + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Command command = (Command) obj;
        return name.equals(command.name) && description.equals(command.description);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }
}
