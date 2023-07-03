package org.blackchain.model;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountBalance {

    private String address;
    private BigInteger balance;
}
