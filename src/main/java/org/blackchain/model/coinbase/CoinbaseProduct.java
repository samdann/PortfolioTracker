package org.blackchain.model.coinbase;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class CoinbaseProduct {

    private String product_id;
    private String base_name;
    private String quote_name;
    private String base_display_symbol;
    private String price;
    private String alias;
}
