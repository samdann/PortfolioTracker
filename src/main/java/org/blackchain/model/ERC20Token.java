package org.blackchain.model;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ERC20Token {

    private String tokenName;
    private BigInteger tokenBalance;
    private String tokenContractAddress;
    private int numberTx;
}
