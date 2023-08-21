package org.blackchain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.BlockChain;
import org.blackchain.model.coinbase.candle.CBCandle;
import org.blackchain.model.etherscan.HistoricBalance;
import org.blackchain.model.portfolio.AssetPerformance;
import org.blackchain.model.portfolio.PairPerformance;
import org.blackchain.model.transaction.Transaction;
import org.blackchain.service.blockchain.BitcoinService;
import org.blackchain.service.blockchain.EthereumService;
import org.blackchain.util.BasicUtils;
import org.blackchain.util.EthereumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionService {

     @Autowired
     EthereumService ethereumService;

     @Autowired
     BitcoinService bitcoinService;

     @Autowired
     ProductService productService;

     /**
      * Retrieving a list of historic data over a default period of 300 days
      *
      * @param address Wallet address on Ethereum
      * @return List of PairPerformance object containing all historic data
      */
     public List<AssetPerformance> getHistoricPerformanceByProduct(final String address,
             final String granularity) {

          log.info("Retrieving historic performance for address: {}", address);

          final List<AssetPerformance> assetPerformanceList = new ArrayList<>();

          final Map<String, List<HistoricBalance>> historicBalanceMap = getHistoricBalanceMap(
                  address);

          historicBalanceMap.forEach((key, value) -> {

               // list of historic prices over a defined period for product {key}
               List<CBCandle> productHistoricData = productService.getProductCandles(key,
                               granularity).stream()
                       .sorted(Comparator.comparingLong(CBCandle::getTimeStamp)).toList();
               // determine the oldest timestamp in the candles
               Optional<Long> minOptional = productHistoricData.stream()
                       .map(CBCandle::getTimeStamp).min(Comparator.comparingLong(x -> x));
               Long oldestTime = minOptional.orElse(null);

               //build map for historic balance
               Map<Long, BigDecimal> balanceMap = value.stream().collect(
                       Collectors.toMap(HistoricBalance::getTimeStamp,
                               HistoricBalance::getBalance, (a, b) -> (b)));

               List<Long> filteredList = new ArrayList<>(
                       value.stream().map(HistoricBalance::getTimeStamp)
                               .filter(timeStamp -> timeStamp > oldestTime).toList());

               // determine timestamp of transaction: to be used in case all candles are after
               Optional<Long> maxOptional = value.stream()
                       .map(HistoricBalance::getTimeStamp)
                       .max(Comparator.comparingLong(x -> x));
               Long latestTime = maxOptional.orElse(null);

               final List<PairPerformance> result = new ArrayList<>();
               productHistoricData.forEach(data -> {
                    long initialPoint =
                            !filteredList.isEmpty() ? filteredList.get(0) : latestTime;
                    if (data.getTimeStamp() > initialPoint && filteredList.size() > 1) {
                         filteredList.remove(0);
                         initialPoint = filteredList.get(0);
                    }

                    BigDecimal amount = balanceMap.get(initialPoint);
                    BigDecimal price = data.getClose();
                    PairPerformance pair = PairPerformance.builder().amount(amount)
                            .time(data.getTimeStamp()).price(price)
                            .pairKey(key + "-" + EthereumUtils.SUPPORTED_CURRENCIES)
                            .marketValue(price.multiply(amount)).build();

                    result.add(pair);
               });

               AssetPerformance assetPerformance = AssetPerformance.builder()
                       .assetName(key).performanceList(result).build();
               assetPerformanceList.add(assetPerformance);

          });

          return assetPerformanceList;
     }

     /*
      * Builds a balance for a given token based on the in / out transactions value.
      */
     private Map<String, List<HistoricBalance>> getHistoricBalanceMap(
             final String address) {

          BlockChain addressBlockChain = BasicUtils.getAddressBlockchain(address);

          final Map<String, List<HistoricBalance>> result = new HashMap<>();

          final Map<String, List<Transaction>> transactionsByToken = new HashMap<>();

          switch (addressBlockChain) {
               case ETHEREUM -> transactionsByToken.putAll(
                       ethereumService.getTransactionsByToken(address));
               case BITCOIN -> transactionsByToken.putAll(
                       bitcoinService.getTransactionsByToken(address));
               case UNKNOWN -> log.warn("Address: {} blockchain not recognized", address);
          }

          transactionsByToken.forEach((key, value) -> {
               final List<HistoricBalance> balanceList = new ArrayList<>();
               List<Transaction> sortedList = value.stream()
                       .sorted(Comparator.comparingLong(Transaction::getTimestamp))
                       .toList();

               BigDecimal balance = BigDecimal.valueOf(0);
               for (Transaction tx : sortedList) {
                    balance = address.equalsIgnoreCase(tx.getTo()) ? balance.add(
                            tx.getValue()) : balance.subtract(tx.getValue());
                    assert (balance.intValue() >= 0);
                    HistoricBalance historicBalance = HistoricBalance.builder()
                            .balance(balance).timeStamp(tx.getTimestamp()).build();
                    balanceList.add(historicBalance);

               }
               result.putIfAbsent(key, balanceList);
          });

          return result;
     }

}
