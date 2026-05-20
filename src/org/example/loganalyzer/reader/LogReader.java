package org.example.loganalyzer.reader;

import org.example.loganalyzer.model.LogEntry;
import java.util.List;

public interface LogReader {
    List<LogEntry> readAll() throws Exception;
}