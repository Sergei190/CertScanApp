package ru.KAA.Sergei.scanner;

import io.javalin.Javalin;
import ru.KAA.Sergei.exstractor.DomainExtractor;
import ru.KAA.Sergei.writer.DomainWriter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SSLScanner {
    private final DomainExtractor domainExtractor;
    private final DomainWriter domainWriter;
    private final IPScanner ipScanner;

    public SSLScanner(DomainExtractor domainExtractor, DomainWriter domainWriter, IPScanner ipScanner) {
        this.domainExtractor = domainExtractor;
        this.domainWriter = domainWriter;
        this.ipScanner = ipScanner;
    }

    public void start() {
        Javalin app = Javalin.create().start(8080);

        app.get("/scan", ctx -> {
            String ipRange = ctx.queryParam("range");
            int threads = Integer.parseInt(ctx.queryParam("threads"));

            ExecutorService executor = Executors.newFixedThreadPool(threads);

            for (String ipAddress : ipScanner.scanIPRange(ipRange)) {
                executor.execute(() -> {
                    try {
                        String domain = domainExtractor.extractDomain(ipAddress);
                        if (domain != null) {
                            domainWriter.saveDomain(domain);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ctx.result("Scanning complete");
        });
    }
}
