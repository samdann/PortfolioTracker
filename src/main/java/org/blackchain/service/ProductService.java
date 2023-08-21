package org.blackchain.service;

import static org.blackchain.util.BasicUtils.instantToStringEpoch;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.coinbase.Granularity;
import org.blackchain.model.coinbase.candle.CBCandle;
import org.blackchain.model.coinbase.product.CBProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class ProductService {

     private static final String ETH_USD_PAIR = "ETH-USD";

     @Autowired
     CoinbaseService coinbaseService;

     public List<CBCandle> getProductCandles(final String ticker,
             final Granularity granularity) {

          List<CBProduct> coinbaseProducts = coinbaseService.getCoinbaseProducts(ticker);
          final String productId = coinbaseProducts.isEmpty() ? ETH_USD_PAIR
                  : coinbaseProducts.get(0).getProduct_id();

          // initializing the start & end
          Instant now = Instant.now();
          String end = instantToStringEpoch(now);
          String start = calculateStart(granularity.getName(), now);

          // building the queryParams
          Map<String, String> queryParams = new LinkedHashMap<>();
          queryParams.put("start", start);
          queryParams.put("end", end);
          queryParams.put("granularity",
                  StringUtils.hasLength(granularity.getName()) ? granularity.getName()
                          : Granularity.ONE_DAY.toString());

          return coinbaseService.getProductHistoricData(productId, queryParams);

     }

     private String calculateStart(final String granularityKey, final Instant now) {
          int numberItems = 300;
          ChronoUnit chronoUnit;
          Granularity granularity = Granularity.get(granularityKey);
          switch (granularity) {
               case ONE_MINUTE, FIVE_MINUTE, FIFTEEN_MINUTE, THIRTY_MINUTE ->
                       chronoUnit = ChronoUnit.MINUTES;
               case ONE_HOUR, SIX_HOUR -> chronoUnit = ChronoUnit.HOURS;
               default -> chronoUnit = ChronoUnit.DAYS;
          }
          return instantToStringEpoch(now.minus(numberItems, chronoUnit));
     }

}
