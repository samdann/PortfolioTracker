package org.blackchain.model.coinbase.product;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CBProducts {

    private List<CBProduct> products;
}
