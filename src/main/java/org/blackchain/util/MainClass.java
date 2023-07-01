package org.blackchain.util;

import java.util.Arrays;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {
        List<Long> longs = Arrays.asList(1l, 1000l, 100l, 10l);
        longs.stream().sorted((x, y) -> Long.compare(y, x)).forEach(System.out::println);
    }
}
