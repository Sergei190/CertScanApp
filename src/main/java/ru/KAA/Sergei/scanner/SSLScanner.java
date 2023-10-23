package ru.KAA.Sergei.scanner;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.StaticFileConfig;
import ru.KAA.Sergei.configuration.StaticFilesConfiguration;
import ru.KAA.Sergei.exstractor.DomainExtractor;
import ru.KAA.Sergei.writer.DomainWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

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
        Javalin app = Javalin.create(config -> {
            StaticFilesConfiguration staticFiles = new StaticFilesConfiguration();
            staticFiles.setStaticFilesPath("/templates"); // Указываем путь к папке с шаблонами
            staticFiles.setStaticFilesLocation("/resources"); // Указываем путь к ресурсам
            config.enableWebjars(); // Включаем поддержку WebJars
            config.addStaticFiles((Consumer<StaticFileConfig>) staticFiles);
        }).start(8080);

        app.get("/scan", ctx -> {
            String ipRange = ctx.queryParam("range");
            int threads = Integer.parseInt(ctx.queryParam("threads"));

            ExecutorService executor = Executors.newFixedThreadPool(threads);
            List<String> domains = new ArrayList<>();

            for (String ipAddress : ipScanner.scanIPRange(ipRange)) {
                executor.execute(() -> {
                    try {
                        String domain = domainExtractor.extractDomain(ipAddress);
                        if (domain != null) {
                            synchronized (domains) {
                                domains.add(domain);
                            }
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

            StringBuilder result = new StringBuilder();
            for (String domain : domains) {
                result.append(domain).append(",");
            }
            if (result.length() > 0) {
                result.deleteCharAt(result.length() - 1); // Удаляем последнюю запятую

                ctx.redirect("/result?domains=" + result);
            } else {
                ctx.redirect("/result");
            }
        });

        app.get("/result", ctx -> {
            String domains = ctx.queryParam("domains");

            Map<String, Object> model = new HashMap<>();
            model.put("result", domains);

            ctx.render("/templates/result.html", model);
        });
    }
}
