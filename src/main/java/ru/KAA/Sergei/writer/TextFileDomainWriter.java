package ru.KAA.Sergei.writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TextFileDomainWriter implements DomainWriter {
    private final String filePath;

    public TextFileDomainWriter(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveDomain(String domain) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, (domain + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
