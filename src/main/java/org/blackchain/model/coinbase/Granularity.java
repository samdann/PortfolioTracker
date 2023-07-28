package org.blackchain.model.coinbase;

public enum Granularity {
     UNKNOWN_GRANULARITY("UNKNOWN_GRANULARITY"), ONE_MINUTE("ONE_MINUTE"), FIVE_MINUTE(
             "FIVE_MINUTE"), FIFTEEN_MINUTE("FIFTEEN_MINUTE"), THIRTY_MINUTE(
             "THIRTY_MINUTE"), ONE_HOUR("ONE_HOUR"), TWO_HOUR("TWO_HOUR"), SIX_HOUR(
             "SIX_HOUR"), ONE_DAY("ONE_DAY");

     private String name;

     Granularity(String name) {
          this.name = name;
     }

     public static Granularity get(String name) {
          switch (name) {
               case "UNKNOWN_GRANULARITY":
                    return UNKNOWN_GRANULARITY;
               case "ONE_MINUTE":
                    return ONE_MINUTE;
               case "FIVE_MINUTE":
                    return FIVE_MINUTE;
               case "FIFTEEN_MINUTE":
                    return FIFTEEN_MINUTE;
               case "THIRTY_MINUTE":
                    return THIRTY_MINUTE;
               case "ONE_HOUR":
                    return ONE_HOUR;
               case "TWO_HOUR":
                    return TWO_HOUR;
               default:
                    return ONE_DAY;
          }
     }

}
