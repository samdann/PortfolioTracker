package org.blackchain.util;

import java.time.Instant;

public class BasicUtils {

     public static String instantToStringEpoch(final Instant time) {
          return String.valueOf(time.toEpochMilli() / 1000);
     }

}
