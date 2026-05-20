package org.example.loganalyzer.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LogEntry {
    private final LocalDateTime timestamp;
    private final LogLevel level;
    private final String message;

    public LogEntry(LocalDateTime timestamp, LogLevel level, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LocalDate getDate() {
        return timestamp.toLocalDate();
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return timestamp + " " + level + " " + message;
    }
}