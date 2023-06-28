package org.blackchain.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.coinbase.CoinbaseProduct;
import org.blackchain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @Operation(description = "products", tags = "coinbase-api")
    @RequestMapping(
            value = "/products",
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    public ResponseEntity<List<CoinbaseProduct>> getListProducts() {
        return ResponseEntity.ok().body(productService.getCoinbaseProducts());

    }
}
