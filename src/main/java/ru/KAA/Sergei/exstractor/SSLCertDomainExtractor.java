package ru.KAA.Sergei.exstractor;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSLCertDomainExtractor implements DomainExtractor {
    @Override
    public String extractDomain(String ipAddress) throws IOException {
        String url = "https://" + ipAddress;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());

            Pattern pattern = Pattern.compile("\".*CN=([^,]*),.*\"");
            Matcher matcher = pattern.matcher(responseBody);

            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null;
    }
}
