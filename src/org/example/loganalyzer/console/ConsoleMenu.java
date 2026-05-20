package org.example.loganalyzer.console;

import org.example.loganalyzer.analyzer.LogAnalyzer;
import org.example.loganalyzer.filter.LogFilter;
import org.example.loganalyzer.model.LogEntry;
import org.example.loganalyzer.model.LogLevel;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class ConsoleMenu {

    private final LogAnalyzer analyzer;
    private final List<LogEntry> originalLogs; // неизменяемый список всех логов
    private final Scanner scanner = new Scanner(System.in);
    private final LogFilter filter = new LogFilter();

    public ConsoleMenu(LogAnalyzer analyzer, List<LogEntry> originalLogs) {
        this.analyzer = analyzer;
        this.originalLogs = Collections.unmodifiableList(originalLogs);
    }

    public void start() {
        System.out.println("=== Log Analyzer Console ===");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose action: ");
            switch (choice) {
                case 1: filterByDate(); break;
                case 2: filterByLevel(); break;
                case 3: setSorting(); break;
                case 4: showFiltered(); break;
                case 5: resetFilters(); break;
                case 6: running = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n=== Actions ===");
        System.out.println("1. Filter by date range");
        System.out.println("2. Filter by log level");
        System.out.println("3. Set sorting order");
        System.out.println("4. Show filtered logs");
        System.out.println("5. Reset filters");
        System.out.println("6. Exit");
    }

    private void filterByDate() {
        System.out.print("Enter start date (YYYY-MM-DD) or leave empty: ");
        String start = scanner.nextLine().trim();
        if (!start.isEmpty()) {
            try {
                filter.setFrom(LocalDate.parse(start));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
                return;
            }
        }
        System.out.print("Enter end date (YYYY-MM-DD) or leave empty: ");
        String end = scanner.nextLine().trim();
        if (!end.isEmpty()) {
            try {
                filter.setTo(LocalDate.parse(end));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
                return;
            }
        }
    }

    private void filterByLevel() {
        System.out.println("Choose level(s) (comma separated): TRACE, DEBUG, INFO, WARN, ERROR");
        System.out.print("Levels: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            filter.setLevels(null);
            return;
        }
        Set<LogLevel> levels = new HashSet<>();
        for (String part : input.split(",")) {
            try {
                levels.add(LogLevel.valueOf(part.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown level: " + part.trim());
                return;
            }
        }
        filter.setLevels(levels.isEmpty() ? null : levels);
    }

    private void setSorting() {
        System.out.println("Select sorting: 1. Ascending (old first), 2. Descending (new first)");
        int sortChoice = readInt("Sorting: ");
        if (sortChoice == 1) {
            filter.setOrder(LogFilter.Order.ASC);
        } else if (sortChoice == 2) {
            filter.setOrder(LogFilter.Order.DESC);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void showFiltered() {
        List<LogEntry> filtered = originalLogs.stream()
                .filter(entry -> filter.matches(entry))
                .sorted((a, b) -> {
                    if (filter.getOrder() == LogFilter.Order.ASC) {
                        return a.getTimestamp().compareTo(b.getTimestamp());
                    } else {
                        return b.getTimestamp().compareTo(a.getTimestamp());
                    }
                })
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("No logs match the criteria.");
        } else {
            filtered.forEach(System.out::println);
        }
    }

    private void resetFilters() {
        filter.setFrom(null);
        filter.setTo(null);
        filter.setLevels(null);
        filter.setOrder(LogFilter.Order.ASC);
        System.out.println("Filters reset.");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }
}