package org.blackchain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
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
            value = "/products/{productId}/candles}",
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    public ResponseEntity<String> getProductHistoricData(
            @Parameter(name = "productId", description = "Id of the product") @RequestParam(value = "product_id", required = true) final String productId) {
        return ResponseEntity.ok().body(productService.getProductHistoricData(productId));
    }
}
