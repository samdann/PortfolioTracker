package org.blackchain.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.blockchain.com.BlockchainAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BlockchainComService {

     private static final String URL = "https://blockchain.info/rawaddr/";

     @Autowired
     HttpService httpService;

     public BlockchainAddress getBitcoinAddress(final String address) {
          String url = URL + address;
          String responseString = httpService.executeGetRequest(url);

          Gson gson = new Gson();
          BlockchainAddress blockchainAddress = gson.fromJson(responseString,
                  BlockchainAddress.class);
          return blockchainAddress;
     }

}
