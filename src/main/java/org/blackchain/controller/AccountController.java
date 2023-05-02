package org.blackchain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Operation(description = "account-balance", tags = "account-api")
    @RequestMapping(
            value = "/_account-balance",
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    public ResponseEntity<String> getAccountBalance(
            @Parameter(name = "apiKey", description = "Api Key") @RequestParam(value = "apiKey", required = true) final String apiKey,
            @Parameter(name = "address", description = "Wallet Address") @RequestParam(value = "address", required = true) final String address) {

        return ResponseEntity.ok().body("");

    }
}
