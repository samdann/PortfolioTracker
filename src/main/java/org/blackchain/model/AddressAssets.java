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
    private List<ERC20Token> ERC20Tokens;
    private List<Transaction> transactions;

    public List<ERC20Token> getERC20Tokens() {
        if (ERC20Tokens == null) {
            ERC20Tokens = new ArrayList<>();
        }
        return ERC20Tokens;
    }

    public List<Transaction> getTransactions() {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        return transactions;
    }
}
