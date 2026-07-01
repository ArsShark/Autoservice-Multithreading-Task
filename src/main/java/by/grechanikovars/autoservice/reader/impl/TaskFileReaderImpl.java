package by.grechanikovars.autoservice.reader.impl;

import by.grechanikovars.autoservice.exception.AutoServiceException;
import by.grechanikovars.autoservice.reader.TaskFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TaskFileReaderImpl implements TaskFileReader {

    private static final Logger LOGGER = LogManager.getLogger(TaskFileReaderImpl.class);

    @Override
    public List<String> readLines(String filePath) throws AutoServiceException {
        Path path = Paths.get(filePath);
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            LOGGER.info("Successfully read {} line(s) from file: {}", lines.size(), filePath);
            return lines;
        } catch (IOException e) {
            String message = String.format("Failed to read file: %s", filePath);
            throw new AutoServiceException(message, e);
        }
    }
}
