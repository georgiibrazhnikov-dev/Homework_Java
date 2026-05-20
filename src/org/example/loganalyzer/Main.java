package org.example.loganalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import org.example.loganalyzer.analyzer.LogAnalyzer;
import org.example.loganalyzer.console.ConsoleMenu;
import org.example.loganalyzer.model.LogEntry;
import org.example.loganalyzer.parser.LogParser;
import org.example.loganalyzer.reader.FileLogReader;
import org.example.loganalyzer.reader.LogReader;
import org.example.loganalyzer.reader.ZipLogReader;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter path to log file (.log or .zip): ");
        String filePath = scanner.nextLine().trim();

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.out.println("File not found: " + filePath);
            scanner.close();
            return;
        }

        System.out.println("Reading file: " + path.toAbsolutePath());

        LogParser parser = new LogParser();
        LogReader reader;

        if (filePath.toLowerCase().endsWith(".zip")) {
            reader = new ZipLogReader(path, parser);
        } else {
            reader = new FileLogReader(path, parser);
        }

        List<LogEntry> logs;
        try {
            logs = reader.readAll();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            scanner.close();
            return;
        }

        if (logs.isEmpty()) {
            System.out.println("No log entries found. Check the file format.");
            scanner.close();
            return;
        }

        LogAnalyzer analyzer = new LogAnalyzer(logs);
        String report = analyzer.generateReport();
        System.out.println(report);

        try {
            Path reportPath = Paths.get("report.txt");
            Files.writeString(reportPath, report);
            System.out.println("Report saved to: " + reportPath.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Failed to save report: " + e.getMessage());
        }

        ConsoleMenu menu = new ConsoleMenu(analyzer, logs);
        menu.start();

        scanner.close();
    }
}