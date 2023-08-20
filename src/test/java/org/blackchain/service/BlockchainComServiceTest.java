package org.blackchain.service;

import java.math.BigInteger;
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
          final String address = "bc1qf6k4eh5d3zxausq8hc5xynfcp0waee2c7sc86c";
          //final String address = "bc1qjqppdz7v8rmy5ldnvzgx82qd8km2qg0ld7795f";
          BlockchainAddress bitcoinAddress = service.getBitcoinAddress(address);
          bitcoinAddress.getTxs().forEach(tx -> {
               assert (tx.getOutputs().size() == 1);
               BigInteger totalValue = BigInteger.valueOf(tx.getInputs().stream()
                       .map(txInput -> txInput.getPreviousOutput().getValue().intValue())
                       .reduce(0, Integer::sum));
               assert (tx.getValue().equals(totalValue.subtract(tx.getFee())));
          });
     }
}
