package org.blackchain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.IOException;
import java.math.BigDecimal;
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

@RestController
public class AccountController {

    private static final String RPC_PROVIDER = "https://rpc.ankr.com/eth";

    @Operation(description = "account-balance", tags = "account-api")
    @RequestMapping(
            value = "/_account-balance",
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    public ResponseEntity<String> getAccountBalance(
            @Parameter(name = "address", description = "Wallet Address") @RequestParam(value = "address", required = true) final String address)
            throws IOException {

        Web3j web3 = Web3j.build(new HttpService(RPC_PROVIDER));

        EthGetBalance balance = web3.ethGetBalance(address,
                DefaultBlockParameterName.LATEST).send();

        BigDecimal balanceInETH = Convert.fromWei(balance.getBalance().toString(),
                Unit.ETHER);

        return ResponseEntity.ok().body(balanceInETH.toString());

    }
}
