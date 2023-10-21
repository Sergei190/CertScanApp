package ru.KAA.Sergei.scanner;

import java.util.ArrayList;
import java.util.List;

public class IPScanner {
    public String[] scanIPRange(String ipRange) {
        List<String> addresses = new ArrayList<>();
        addresses.add("51.38.24.1");
        addresses.add("51.38.24.2");
        addresses.add("51.38.24.3");

        return addresses.toArray(new String[0]);
    }
}
