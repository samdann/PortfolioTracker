package org.blackchain.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressAssets {

    private BigInteger ethBalance;
    private List<Token> tokens;
    private List<Transaction> transactions;

    public List<Token> getTokens() {
        if (tokens == null) {
            tokens = new ArrayList<>();
        }
        return tokens;
    }

    public List<Transaction> getTransactions() {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        return transactions;
    }
}
