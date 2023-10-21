package ru.KAA.Sergei.exstractor;

import java.io.IOException;

public interface DomainExtractor {
    String extractDomain(String ipAddress) throws IOException;
}
