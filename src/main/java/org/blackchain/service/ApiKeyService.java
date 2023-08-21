package org.blackchain.service;

import io.goodforgod.api.etherscan.EtherScanAPI;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Setter
@PropertySource("secret.properties")
public class ApiKeyService {

     @Value("${etherscan.api.key}")
     String etherScanApiKey;

     private EtherScanAPI etherScanAPI;

     public EtherScanAPI getEtherScanAPI() {
          if (etherScanAPI == null) {
               etherScanAPI = EtherScanAPI.builder().withApiKey(etherScanApiKey).build();
          }

          return etherScanAPI;
     }

}
