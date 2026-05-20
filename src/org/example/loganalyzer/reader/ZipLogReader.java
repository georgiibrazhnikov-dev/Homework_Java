package org.example.loganalyzer.reader;

import org.example.loganalyzer.model.LogEntry;
import org.example.loganalyzer.parser.LogParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipLogReader implements LogReader {

    private final Path zipPath;
    private final LogParser parser;

    public ZipLogReader(Path zipPath, LogParser parser) {
        this.zipPath = zipPath;
        this.parser = parser;
    }

    @Override
    public List<LogEntry> readAll() throws IOException {
        // Временный файл для распакованного лога
        Path tempFile = Files.createTempFile("extracted_log_", ".log");

        try (ZipFile zipFile = new ZipFile(zipPath.toFile())) {
            // Найти первый файл с расширением .log (не директорию)
            Optional<? extends ZipEntry> logEntry = zipFile.stream()
                    .filter(e -> !e.isDirectory() && e.getName().toLowerCase().endsWith(".log"))
                    .findFirst();

            if (logEntry.isEmpty()) {
                Files.deleteIfExists(tempFile);
                throw new IOException("В архиве не найден .log файл");
            }

            System.out.println("Extracting: " + logEntry.get().getName());

            // Копируем содержимое во временный файл
            try (InputStream is = zipFile.getInputStream(logEntry.get())) {
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // Читаем временный файл обычным FileLogReader
        FileLogReader fileReader = new FileLogReader(tempFile, parser);
        List<LogEntry> entries = fileReader.readAll();

        // Удаляем временный файл
        Files.deleteIfExists(tempFile);

        System.out.println("Parsed " + entries.size() + " log entries.");
        return entries;
    }
}