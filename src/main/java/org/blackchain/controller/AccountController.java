package org.blackchain.controller;

import io.api.etherscan.core.impl.EtherScanApi;
import io.api.etherscan.model.Balance;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@Timed
@RestController
public class AccountController {

    @ApiOperation(value = "account-balance", tags = "account-api")
    @RequestMapping(
            value = "/_account-balance",
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    public ResponseEntity<String> getAccountBalance(
            @ApiParam(name = "apiKey", value = "Api Key") @RequestParam(value = "apiKey", required = true) final String apiKey,
            @ApiParam(name = "address", value = "Wallet Address") @RequestParam(value = "address", required = true) final String address) {

        EtherScanApi api = new EtherScanApi(apiKey);
        Balance balance = api.account().balance(address);

        api.account().balance(address);

        return ResponseEntity.ok().body(balance.getEther().toString());

    }
}
