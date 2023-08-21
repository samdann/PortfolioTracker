package org.blackchain.controller;

import io.goodforgod.api.etherscan.model.TxErc20;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.exception.AccountException;
import org.blackchain.model.coinbase.Granularity;
import org.blackchain.model.portfolio.AssetPerformance;
import org.blackchain.service.EtherScanService;
import org.blackchain.service.TransactionService;
import org.blackchain.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Configuration
public class AccountController {

     @Autowired
     TransactionService transactionService;

     @Autowired
     EtherScanService etherScanService;

     @Operation(description = "account-balance", tags = "account-api")
     @RequestMapping(value = "/ERC20-transactions", produces = {
             "application/json"}, method = RequestMethod.GET)
     public ResponseEntity<List<TxErc20>> getERC20Transactions(
             @Parameter(name = "address", description = "Wallet Address") @RequestParam(value = "address", required = true) final String address) {

          List<TxErc20> ERC20Tokens = etherScanService.getERC20Transactions(address);

          return ResponseEntity.ok().body(ERC20Tokens);

     }

     @Operation(description = "account-transactions", tags = "account-api")
     @RequestMapping(value = "/historic-performance", produces = {
             "application/json"}, method = RequestMethod.GET)
     public ResponseEntity<List<AssetPerformance>> getHistoricPerformance(
             @Parameter(name = "address", description = "Wallet Address") @RequestParam(value = "address", required = true) final String address,
             @Parameter(name = "granularity", description = "Candles granularity") @RequestParam(value = "granularity", required = false) final Granularity granularity) {
          try {
               List<AssetPerformance> assetPerformances =
                       "1".equals(address) ? DataUtils.getAssetPerformanceList()
                               : transactionService.getHistoricPerformanceByProduct(
                                       address, granularity);
               return ResponseEntity.ok().body(assetPerformances);
          } catch (AccountException ex) {
               return ResponseEntity.notFound().build();
          }
     }
}
