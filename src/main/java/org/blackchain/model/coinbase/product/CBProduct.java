package org.blackchain.model.coinbase.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CBProduct {

    private String product_id;
    private String base_name;
    private String quote_name;
    private String base_display_symbol;
    private String price;
    private String alias;
}
