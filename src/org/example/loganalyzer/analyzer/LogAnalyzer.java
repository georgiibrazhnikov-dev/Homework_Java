package org.example.loganalyzer.analyzer;

import org.example.loganalyzer.model.LogEntry;
import org.example.loganalyzer.model.LogLevel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class LogAnalyzer {

    private final List<LogEntry> logs;

    public LogAnalyzer(List<LogEntry> logs) {
        this.logs = new ArrayList<>(logs);
    }

    // 1. Общее количество логов
    public int totalCount() {
        return logs.size();
    }

    // 2. Количество по уровням
    public Map<LogLevel, Long> countByLevel() {
        return logs.stream()
                .collect(Collectors.groupingBy(LogEntry::getLevel, Collectors.counting()));
    }

    // 3. Временной промежуток (возвращает массив {min, max}, либо null, если логов нет)
    public LocalDateTime[] getTimeRange() {
        if (logs.isEmpty()) return null;
        LocalDateTime min = logs.stream().map(LogEntry::getTimestamp).min(LocalDateTime::compareTo).get();
        LocalDateTime max = logs.stream().map(LogEntry::getTimestamp).max(LocalDateTime::compareTo).get();
        return new LocalDateTime[]{min, max};
    }

    // 4. Группировка по дням
    public Map<LocalDate, Long> countByDay() {
        return logs.stream()
                .collect(Collectors.groupingBy(LogEntry::getDate, Collectors.counting()));
    }

    // 5. Самый "ошибочный" день (максимальное количество ERROR)
    public Map.Entry<LocalDate, Long> mostErrorDay() {
        return logs.stream()
                .filter(e -> e.getLevel() == LogLevel.ERROR)
                .collect(Collectors.groupingBy(LogEntry::getDate, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
    }

    // 6. Топ сообщений (сообщение -> количество)
    public List<Map.Entry<String, Long>> topMessages(int limit) {
        return logs.stream()
                .collect(Collectors.groupingBy(LogEntry::getMessage, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Генерация текстового отчёта
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Log Analysis Report ===\n\n");

        sb.append("Total logs: ").append(totalCount()).append("\n\n");

        // Количество по уровням
        sb.append("Logs by level:\n");
        countByLevel().forEach((level, count) ->
                sb.append("  ").append(level).append(": ").append(count).append("\n")
        );
        sb.append("\n");

        // Временной промежуток
        LocalDateTime[] range = getTimeRange();
        if (range != null) {
            sb.append("Time range:\n");
            sb.append("  Start: ").append(range[0]).append("\n");
            sb.append("  End:   ").append(range[1]).append("\n\n");
        }

        // Группировка по дням
        sb.append("Logs per day:\n");
        countByDay().forEach((day, count) ->
                sb.append("  ").append(day).append(": ").append(count).append("\n")
        );
        sb.append("\n");

        // Самый ошибочный день
        Map.Entry<LocalDate, Long> errorDay = mostErrorDay();
        sb.append("Most error day: ");
        if (errorDay != null) {
            sb.append(errorDay.getKey()).append(" with ").append(errorDay.getValue()).append(" errors\n");
        } else {
            sb.append("N/A\n");
        }
        sb.append("\n");

        // Топ-5 сообщений
        sb.append("Top 5 messages:\n");
        topMessages(5).forEach(entry ->
                sb.append("  ").append(entry.getKey()).append(" — ").append(entry.getValue()).append("\n")
        );

        return sb.toString();
    }
}