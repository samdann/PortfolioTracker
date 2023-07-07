package org.blackchain.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.blackchain.model.portfolio.AssetPerformance;
import org.blackchain.model.portfolio.PairPerformance;

public class DataUtils {

     public static List<AssetPerformance> getAssetPerformanceList() {
          final List<AssetPerformance> assetPerformanceList = new ArrayList<>();
          assetPerformanceList.add(getAssetPerformance("eth".toLowerCase()));
          assetPerformanceList.add(getAssetPerformance("matic".toLowerCase()));
          return assetPerformanceList;
     }

     private static AssetPerformance getAssetPerformance(final String token) {

          final List<PairPerformance> performanceList = new ArrayList<>();
          if ("eth".toLowerCase().equals(token)) {
               performanceList.add(getPairPerformance(1688169600L, token, "1450", "1"));
               performanceList.add(getPairPerformance(1688256000L, token, "1470", "2"));
               performanceList.add(getPairPerformance(1688342400L, token, "1435", "3.5"));
               performanceList.add(getPairPerformance(1688428800L, token, "1490", "4"));
          } else if ("matic".toLowerCase().equals(token)) {
               performanceList.add(getPairPerformance(1688169600L, token, "0.7", "1000"));
               performanceList.add(getPairPerformance(1688256000L, token, "0.81", "100"));
               performanceList.add(getPairPerformance(1688342400L, token, "0.91", "350"));
               performanceList.add(getPairPerformance(1688428800L, token, "0.78", "700"));
          }
          return AssetPerformance.builder().assetName(token)
                  .performanceList(performanceList).build();
     }

     private static PairPerformance getPairPerformance(final Long timestamp,
             final String pairKey, final String price, final String amount) {
          return PairPerformance.builder().time(timestamp).pairKey(pairKey)
                  .price(BigDecimal.valueOf(Double.parseDouble(price)))
                  .amount(BigDecimal.valueOf(Double.parseDouble(amount))).marketValue(
                          BigDecimal.valueOf(
                                  Double.parseDouble(price) * Double.parseDouble(amount)))
                  .build();
     }

}
