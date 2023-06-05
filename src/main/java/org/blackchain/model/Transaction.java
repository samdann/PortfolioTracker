package org.blackchain.model;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class Transaction {

    private String from;
    private String to;
    private BigInteger value;
}
