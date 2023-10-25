package ru.KAA.Sergei;

import ru.KAA.Sergei.exstractor.DomainExtractor;
import ru.KAA.Sergei.exstractor.SSLCertDomainExtractor;
import ru.KAA.Sergei.scanner.IPScanner;
import ru.KAA.Sergei.scanner.SSLScanner;
import ru.KAA.Sergei.writer.DomainWriter;
import ru.KAA.Sergei.writer.TextFileDomainWriter;

public class Main {

    public static void main(String[] args) {
        DomainExtractor domainExtractor = new SSLCertDomainExtractor();
        DomainWriter domainWriter = new TextFileDomainWriter("index.html");
        IPScanner ipScanner = new IPScanner();

        SSLScanner sslScanner = new SSLScanner(domainExtractor, domainWriter, ipScanner);
        sslScanner.start();
    }
}
