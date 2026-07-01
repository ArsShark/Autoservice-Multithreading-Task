package by.grechanikovars.autoservice.reader;

import by.grechanikovars.autoservice.exception.AutoServiceException;

import java.util.List;

public interface TaskFileReader {

    List<String> readLines(String filePath) throws AutoServiceException;
}
