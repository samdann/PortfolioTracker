package org.blackchain.model.etherscan;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoricBalance {

    private String assetName;
    private long timeStamp;
    private BigInteger balance;
}
