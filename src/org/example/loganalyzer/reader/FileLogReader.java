package org.example.loganalyzer.reader;

import org.example.loganalyzer.model.LogEntry;
import org.example.loganalyzer.parser.LogParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileLogReader implements LogReader {

    private final Path filePath;
    private final LogParser parser;

    public FileLogReader(Path filePath, LogParser parser) {
        this.filePath = filePath;
        this.parser = parser;
    }

    @Override
    public List<LogEntry> readAll() throws IOException {
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines
                .peek(line -> {
                    Optional<LogEntry> parsed = parser.parse(line);
                    if (parsed.isEmpty()) {
                        System.out.println("SKIPPED: " + line);
                    }
                })
                .map(parser::parse)
                .flatMap(opt -> opt.stream())
                .collect(Collectors.toList());
        }
    }
}