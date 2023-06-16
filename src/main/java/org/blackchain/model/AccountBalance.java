package org.blackchain.model;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class AccountBalance {

    private String address;
    private BigInteger balance;
}
