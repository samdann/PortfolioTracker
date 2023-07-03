package org.blackchain.controller;

import io.goodforgod.api.etherscan.EtherScanAPI;
import io.goodforgod.api.etherscan.model.TxErc20;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.portfolio.PairPerformance;
import org.blackchain.service.EtherScanService;
import org.blackchain.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@PropertySource("secret.properties")
@Configuration
public class AccountController {

     @Autowired
     TransactionService transactionService;

     @Autowired
     EtherScanService etherScanService;

     @Value("${etherscan.api.key}")
     private String etherscanApiKey;

     @Operation(description = "account-balance", tags = "account-api")
     @RequestMapping(value = "/ERC20-transactions", produces = {
             "application/json"}, method = RequestMethod.GET)
     public ResponseEntity<List<TxErc20>> getERC20Transactions(
             @Parameter(name = "address", description = "Wallet Address") @RequestParam(value = "address", required = true) final String address) {
          EtherScanAPI api = EtherScanAPI.builder().withApiKey(etherscanApiKey).build();

          List<TxErc20> ERC20Tokens = etherScanService.getERC20Transactions(api, address);

          return ResponseEntity.ok().body(ERC20Tokens);

     }

     @Operation(description = "account-transactions", tags = "account-api")
     @RequestMapping(value = "/historic-performance", produces = {
             "application/json"}, method = RequestMethod.GET)
     public ResponseEntity<List<PairPerformance>> getHistoricPerformance(
             @Parameter(name = "address", description = "Wallet Address") @RequestParam(value = "address", required = true) final String address) {
          EtherScanAPI api = EtherScanAPI.builder().withApiKey(etherscanApiKey).build();
          List<PairPerformance> historicPerf = transactionService.getHistoricPerformanceByProduct(
                  api, address);
          return ResponseEntity.ok().body(historicPerf);

     }

}
