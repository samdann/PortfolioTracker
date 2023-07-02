package org.blackchain.model.coinbase.candle;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CBCandle {

    private long start;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal volume;
}
