package org.blackchain.controller;

import io.goodforgod.api.etherscan.EtherScanAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.AddressAssets;
import org.blackchain.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

@Slf4j
@RestController
public class AccountController {

    private static final String RPC_PROVIDER = "https://rpc.ankr.com/eth";

    @Autowired
    TransactionService transactionService;

    @Operation(description = "account-balance", tags = "account-api")
    @RequestMapping(
            value = "/_account-balance",
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    public ResponseEntity<String> getAccountBalance(
            @Parameter(name = "address", description = "Wallet Address") @RequestParam(value = "address", required = true) final String address)
            throws IOException {

        StringBuilder result = new StringBuilder();

        Web3j web3 = Web3j.build(new HttpService(RPC_PROVIDER));

        EthGetBalance balance = web3.ethGetBalance(address,
                DefaultBlockParameterName.LATEST).send();

        BigDecimal web3BalanceInEth = Convert.fromWei(balance.getBalance().toString(),
                Unit.WEI);

        EtherScanAPI api = EtherScanAPI.builder().build();
        BigInteger etherScanBalanceInEth = api.account().balance(address).getBalanceInWei().asWei();

        if (etherScanBalanceInEth.toString().equals(web3BalanceInEth.toString())) {
            result.append(web3BalanceInEth);

        } else {
            result.append("NADA");
        }

        return ResponseEntity.ok().body(result.toString());

    }

    @Operation(description = "account-transactions", tags = "account-api")
    @RequestMapping(
            value = "/_account-transactions",
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    public ResponseEntity<AddressAssets> getAccountTransactions(
            @Parameter(name = "address", description = "Wallet Address") @RequestParam(value = "address", required = true) final String address)
            throws IOException {
        EtherScanAPI api = EtherScanAPI.builder().build();
        AddressAssets addressAssets = transactionService.getAddressAssets(api, address);
        return ResponseEntity.ok().body(addressAssets);

    }
}
