package org.blackchain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.coinbase.CoinbaseProduct;
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
    public ResponseEntity<List<CoinbaseProduct>> getListProducts() {
        return ResponseEntity.ok().body(productService.getCoinbaseProducts());

    }

    @Operation(description = "Historic data of a given product for a given period", tags = "coinbase-api")
    @RequestMapping(
            value = "/products/{product_id}/candles",
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    public ResponseEntity<String> getProductHistoricData(
            @Parameter(name = "product_id", description = "Id of the product") @RequestParam(value = "product_id", required = true) final String productId,
            @Parameter(name = "start", description = "start of the period") @RequestParam(value = "start", required = true) final Instant start,
            @Parameter(name = "end", description = "end of the period") @RequestParam(value = "end", required = true) Instant end) {

        Map<String, String> queryParams = new HashMap<>();
        if (start != null) {
            queryParams.put("start", start.toString());
        }
        if (end != null) {
            queryParams.put("end", end.toString());
        }
        return ResponseEntity.ok()
                .body(productService.getProductHistoricData(productId, queryParams));
    }
}
