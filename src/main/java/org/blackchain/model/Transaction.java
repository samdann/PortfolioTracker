package org.blackchain.model;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class Transaction {

    private String txHash;
    private long blockNumber;
    private long timestamp;
    private String from;
    private String to;
    private BigInteger value;
    private BigInteger gas;
    private BigInteger gasUsed;

}
