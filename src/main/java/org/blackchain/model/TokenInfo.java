package org.blackchain.model;

import java.math.BigInteger;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class TokenInfo {

    private String tokenName;
    private BigInteger tokenBalance;
    private String tokenContractAddress;
    private int numberTx;
}
