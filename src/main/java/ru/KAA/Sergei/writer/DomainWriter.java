package ru.KAA.Sergei.writer;

import java.io.IOException;

public interface DomainWriter {
    void saveDomain(String domain) throws IOException;
}
