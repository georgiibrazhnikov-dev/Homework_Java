package org.example.loganalyzer.parser;

import org.example.loganalyzer.model.LogEntry;
import org.example.loganalyzer.model.LogLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    private static final Pattern LOG_PATTERN = Pattern.compile(
        "^(\\d{4}-\\d{2}-\\d{2})[T ](\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?)\\s+(TRACE|DEBUG|INFO|WARN|ERROR)\\s+(.*)$"
    );

    // Гибкий форматер, принимающий миллисекунды от 0 до 6 знаков после точки
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .appendLiteral(' ')
            .appendPattern("HH:mm:ss")
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 6, true) // 0-6 знаков после точки, точка опциональна
            .toFormatter();

    public Optional<LogEntry> parse(String line) {
        if (line == null || line.isBlank()) {
            return Optional.empty();
        }

        // Убираем BOM и обрезаем пробелы по краям
        String cleanLine = line.startsWith("\uFEFF") ? line.substring(1) : line;
        cleanLine = cleanLine.trim();

        Matcher matcher = LOG_PATTERN.matcher(cleanLine);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        // Собираем полную дату-время, заменяя T на пробел, если есть
        String datePart = matcher.group(1);
        String timePart = matcher.group(2);
        String timestampStr = datePart + " " + timePart; // теперь всегда пробел

        String levelStr = matcher.group(3);
        String message = matcher.group(4);

        LocalDateTime timestamp;
        try {
            timestamp = LocalDateTime.parse(timestampStr, TIMESTAMP_FORMATTER);
        } catch (Exception e) {
            return Optional.empty();
        }

        LogLevel level;
        try {
            level = LogLevel.valueOf(levelStr);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }

        return Optional.of(new LogEntry(timestamp, level, message));
    }
}