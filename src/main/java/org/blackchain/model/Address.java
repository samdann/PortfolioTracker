package org.blackchain.model;

import java.math.BigInteger;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Address {

    private BigInteger ethBalance;
    private List<TokenInfo> tokens;
}
