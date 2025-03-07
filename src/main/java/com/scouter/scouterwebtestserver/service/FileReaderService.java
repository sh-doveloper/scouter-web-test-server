package com.scouter.scouterwebtestserver.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileReaderService {
    public String readJsonFile(String fileName) throws IOException {
        Path path = new ClassPathResource("response/" + fileName).getFile().toPath();
        return Files.readString(path);
    }
}
