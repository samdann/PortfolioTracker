package org.blackchain.controller;

import static org.blackchain.util.BasicUtils.instantToStringEpoch;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.coinbase.Granularity;
import org.blackchain.model.coinbase.candle.CBCandle;
import org.blackchain.model.coinbase.product.CBProduct;
import org.blackchain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @Operation(description = "List of all available products", tags = "coinbase-api")
    @RequestMapping(
            value = "/products",
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    public ResponseEntity<List<CBProduct>> getListProducts() {
        return ResponseEntity.ok().body(productService.getCoinbaseProducts());

    }

    @Operation(description = "Historic data of a given product for a given period", tags = "coinbase-api")
    @RequestMapping(
            value = "/products/{product_id}/candles",
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    public ResponseEntity<List<CBCandle>> getProductHistoricData(
            @Parameter(name = "product_id", description = "Id of the product") @RequestParam(value = "product_id", required = true) final String productId,
            @Parameter(name = "start", description = "Start of the period. Must conform to ISO-8601 format (yyyy-MM-dd'T'HH:mm:ssZ)") @RequestParam(value = "start", required = true) final Instant start,
            @Parameter(name = "end", description = "End of the period. Must conform to ISO-8601 format (yyyy-MM-dd'T'HH:mm:ssZ)") @RequestParam(value = "end", required = true) Instant end,
            @Parameter(name = "granularity", description = "time scale of each candle") @RequestParam(value = "granularity", required = true) Granularity granularity) {

        Map<String, String> queryParams = new LinkedHashMap<>();

        queryParams.put("start", instantToStringEpoch(start));
        queryParams.put("end", instantToStringEpoch(end));
        queryParams.put("granularity", granularity.toString());

        return ResponseEntity.ok()
                .body(productService.getProductHistoricData(productId, queryParams));
    }
}
