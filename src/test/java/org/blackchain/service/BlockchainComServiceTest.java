package org.blackchain.service;

import org.blackchain.model.blockchain.com.BlockchainAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BlockchainComServiceTest {

     @InjectMocks
     private BlockchainComService service;

     @Test
     public void testGetBitcoinAddress() {
          final String address = "bc1qjqppdz7v8rmy5ldnvzgx82qd8km2qg0ld7795f";
          BlockchainAddress bitcoinAddress = service.getBitcoinAddress(address);
          assert (bitcoinAddress.getTxs().size() > 0);
     }
}
