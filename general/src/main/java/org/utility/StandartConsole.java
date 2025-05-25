package org.utility;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Стандартная консоль для ввода команд и вывода результата их выполнения
 */
public class StandartConsole implements Console {
    private static final String P = "Ы ";
    private static Scanner fileScanner = null;
    private static Scanner defScanner = new Scanner(System.in);

    /**
     * Выводит obj.toString() в консоль
     * @param obj Объект на вывод
     */
    public void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Выводит obj.toString() в консоль и переходит на новую строку
     * @param obj Объект для вывода
     */
    public void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Выводит Error: + obj.toString() в консоль
     * @param obj Объект для вывода
     */
    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }

    public String readln() throws NoSuchElementException, IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).nextLine();
    }

    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).hasNextLine();
    }

    /**
     * выводит 2 колонки
     * @param elementLeft Элемент из левой колонки
     * @param elementRight Элемент из правой колонки
     */
    public void printTable(Object elementLeft, Object elementRight) {
        System.out.printf(" %-35s%-1s%n", elementLeft, elementRight);
    }

    /**
     * Выводит промпт используемой консоли
     */
    public void prompt() {
        print(P);
    }

    /**
     * @return P промпт используемой консоли
     */
    public String getPrompt() {
        return P;
    }

    public void selectFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    public void selectConsoleScanner() {
        fileScanner = null;
    }
}
