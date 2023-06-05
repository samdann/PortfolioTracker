package org.blackchain.model;

import io.goodforgod.api.etherscan.model.Tx;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class AddressAssets {

    private BigInteger ethBalance;
    private List<Token> tokens;
    private List<Tx> transactions;

    public List<Token> getTokens() {
        if (tokens == null) {
            tokens = new ArrayList<>();
        }
        return tokens;
    }

    public List<Tx> getTransactions() {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        return transactions;
    }
}
