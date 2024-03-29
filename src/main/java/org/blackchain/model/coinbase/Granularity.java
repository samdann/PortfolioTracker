package org.blackchain.model.coinbase;

public enum Granularity {
     UNKNOWN_GRANULARITY("UNKNOWN_GRANULARITY"), ONE_MINUTE("ONE_MINUTE"), FIVE_MINUTE(
             "FIVE_MINUTE"), FIFTEEN_MINUTE("FIFTEEN_MINUTE"), THIRTY_MINUTE(
             "THIRTY_MINUTE"), ONE_HOUR("ONE_HOUR"), SIX_HOUR("SIX_HOUR"), ONE_DAY(
             "ONE_DAY");

     private String name;

     Granularity(String name) {
          this.name = name;
     }

     public static Granularity get(String name) {
          return switch (name) {
               case "UNKNOWN_GRANULARITY" -> UNKNOWN_GRANULARITY;
               case "ONE_MINUTE" -> ONE_MINUTE;
               case "FIVE_MINUTE" -> FIVE_MINUTE;
               case "FIFTEEN_MINUTE" -> FIFTEEN_MINUTE;
               case "THIRTY_MINUTE" -> THIRTY_MINUTE;
               case "ONE_HOUR" -> ONE_HOUR;
               case "SIX_HOUR" -> SIX_HOUR;
               default -> ONE_DAY;
          };
     }

     public String getName() {
          return name;
     }

}
