package org.blackchain.model.coinbase.candle;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CBCandles {

    private List<CBCandle> candles;
}
