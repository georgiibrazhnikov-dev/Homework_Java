package org.example.loganalyzer.filter;

import org.example.loganalyzer.model.LogEntry;
import org.example.loganalyzer.model.LogLevel;

import java.time.LocalDate;
import java.util.Set;

public class LogFilter {
    private LocalDate from;
    private LocalDate to;
    private Set<LogLevel> levels;
    private Order order = Order.ASC;

    public enum Order {
        ASC, DESC
    }

    public LocalDate getFrom() { return from; }
    public void setFrom(LocalDate from) { this.from = from; }

    public LocalDate getTo() { return to; }
    public void setTo(LocalDate to) { this.to = to; }

    public Set<LogLevel> getLevels() { return levels; }
    public void setLevels(Set<LogLevel> levels) { this.levels = levels; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public boolean matches(LogEntry entry) {
        if (from != null && entry.getDate().isBefore(from)) return false;
        if (to != null && entry.getDate().isAfter(to)) return false;
        if (levels != null && !levels.isEmpty() && !levels.contains(entry.getLevel())) return false;
        return true;
    }
}