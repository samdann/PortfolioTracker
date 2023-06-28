package org.blackchain.model.coinbase;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class CoinbaseProducts {

    private List<CoinbaseProduct> products;
}
