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
     public void testExecuteGetRequest() {
          final String url = "1EEQKfgJxZ5Ci1B2wmFsAbrnfxM98SVTat";
          String string = service.executeGetRequest(url);

          //Mockito.verify(string).isEmpty();
          assert (!string.isEmpty());
     }

     @Test
     public void testGetBitcoinAddress() {
          final String address = "1EEQKfgJxZ5Ci1B2wmFsAbrnfxM98SVTat";
          BlockchainAddress bitcoinAddress = service.getBitcoinAddress(address);
          assert (bitcoinAddress.getTxs().size() > 0);
     }
}
