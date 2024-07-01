package com.devfay.jcodegenerator;

import java.util.Scanner;

public class ConsoleMenu1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Menu Principal");
            System.out.println("1. Opción 1");
            System.out.println("2. Opción 2");
            System.out.println("3. Salir");
            System.out.print("Selecciona una opción: ");
            int mainChoice = scanner.nextInt();

            switch (mainChoice) {
                case 1:
                    opcion1(scanner);
                    break;
                case 2:
                    opcion2(scanner);
                    break;
                case 3:
                    exit = true;
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida, por favor intenta de nuevo.");
            }
        }

        scanner.close();
    }

    private static void opcion1(Scanner scanner) {
        System.out.println("Submenú Opción 1");
        System.out.println("1. Subopción 1.1");
        System.out.println("2. Subopción 1.2");
        System.out.print("Selecciona una subopción: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Resultado de la Subopción 1.1: Dato ficticio A");
                break;
            case 2:
                System.out.println("Resultado de la Subopción 1.2: Dato ficticio B");
                break;
            default:
                System.out.println("Subopción no válida, regresando al menú principal.");
        }
    }

    private static void opcion2(Scanner scanner) {
        System.out.println("Submenú Opción 2");
        System.out.println("1. Subopción 2.1");
        System.out.println("2. Subopción 2.2");
        System.out.print("Selecciona una subopción: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Resultado de la Subopción 2.1: Dato ficticio C");
                break;
            case 2:
                System.out.println("Resultado de la Subopción 2.2: Dato ficticio D");
                break;
            default:
                System.out.println("Subopción no válida, regresando al menú principal.");
        }
    }
}
