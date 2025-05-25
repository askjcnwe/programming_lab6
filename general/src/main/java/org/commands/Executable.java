package org.commands;


import org.utility.ExecutionResponse;

/**
 * Интерфейс для всех выполняемых команд
 */
public interface Executable {

    /**
     * Выполнить какое-либо действие
     * @param args Аргументы
     * @return результат выполнения
     */
    ExecutionResponse apply(String[] args);
}
