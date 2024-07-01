package com.devfay.jcodegenerator;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleMenu2 {

    public static void main(String[] args) {
        AnsiConsole.systemInstall();

        Map<String, String> mainMenuOptions = new HashMap<>();
        mainMenuOptions.put("1", "Opción 1");
        mainMenuOptions.put("2", "Opción 2");
        mainMenuOptions.put("3", "Salir");

        Map<String, List<String>> subMenuOptions = new HashMap<>();
        subMenuOptions.put("1", List.of("Subopción 1.1", "Subopción 1.2"));
        subMenuOptions.put("2", List.of("Subopción 2.1", "Subopción 2.2"));

        Map<String, List<String>> dummyResults = new HashMap<>();
        dummyResults.put("1_1", List.of("Resultado de la Subopción 1.1: Dato ficticio A"));
        dummyResults.put("1_2", List.of("Resultado de la Subopción 1.2: Dato ficticio B"));
        dummyResults.put("2_1", List.of("Resultado de la Subopción 2.1: Dato ficticio C"));
        dummyResults.put("2_2", List.of("Resultado de la Subopción 2.2: Dato ficticio D"));

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            printMenu("Menu Principal", mainMenuOptions);
            String mainChoice = scanner.nextLine();

            switch (mainChoice) {
                case "1":
                case "2":
                    List<String> subOptions = subMenuOptions.get(mainChoice);
                    printSubMenu(mainChoice, subOptions, dummyResults, scanner);
                    break;
                case "3":
                    exit = true;
                    System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a("Saliendo del programa...").reset());
                    break;
                default:
                    System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("Opción no válida, por favor intenta de nuevo.").reset());
            }
        }

        scanner.close();
        AnsiConsole.systemUninstall();
    }

    private static void printMenu(String title, Map<String, String> options) {
        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a(title).reset());
        options.forEach((key, value) -> System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a(key + ". " + value).reset()));
        System.out.print(Ansi.ansi().fg(Ansi.Color.MAGENTA).a("Selecciona una opción: ").reset());
    }

    private static void printSubMenu(String mainChoice, List<String> subOptions, Map<String, List<String>> dummyResults, Scanner scanner) {
        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("Submenú Opción " + mainChoice).reset());
        for (int i = 0; i < subOptions.size(); i++) {
            System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a((i + 1) + ". " + subOptions.get(i)).reset());
        }
        System.out.print(Ansi.ansi().fg(Ansi.Color.MAGENTA).a("Selecciona una subopción: ").reset());
        int subChoice = scanner.nextInt();
        scanner.nextLine();  // consume newline

        String key = mainChoice + "_" + subChoice;
        List<String> results = dummyResults.get(key);

        if (results != null) {
            results.forEach(result -> System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a(result).reset()));
        } else {
            System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("Subopción no válida, regresando al menú principal.").reset());
        }
    }
}
