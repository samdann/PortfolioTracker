package org.blackchain.service;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.blackchain.model.blockchain.com.BlockchainAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BlockchainComServiceTest {

     @InjectMocks
     private BlockchainComService service;

     private static String readJsonFile(final String filePath) {
          Path path = Paths.get(filePath);
          byte[] bytes = new byte[0];
          try {
               bytes = Files.readAllBytes(path);
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          return new String(bytes);
     }

     @Test
     public void testGetBitcoinAddressWithMultipleInputsOneOutput() {
          final String address = "bc1qjqppdz7v8rmy5ldnvzgx82qd8km2qg0ld7795f";
          testGetBitcoinAddress(address);
     }

     @Test
     public void testGetBitcoinAddressWithOneInputMultipleOutputs() {
          final String address = "bc1qf6k4eh5d3zxausq8hc5xynfcp0waee2c7sc86c";
          testGetBitcoinAddress(address);
     }

     private void testGetBitcoinAddress(final String address) {

          ClassLoader classLoader = this.getClass().getClassLoader();
          //classLoader.getResourceAsStream()

          String filePath = "BTCResponse.json";
          String jsonFile = readJsonFile(filePath);

          Mockito.when(service.executeGetRequest(address)).thenReturn(jsonFile);
          BlockchainAddress bitcoinAddress = service.getBitcoinAddress(address);
          bitcoinAddress.getTxs().forEach(tx -> {
               if (tx.getInputs().size() > 1 && tx.getOutputs().size() == 1) {
                    BigInteger totalValue = BigInteger.valueOf(tx.getInputs().stream()
                            .map(txInput -> txInput.getPreviousOutput().getValue()
                                    .intValue()).reduce(0, Integer::sum));
                    assert (tx.getValue().equals(totalValue.subtract(tx.getFee())));
               }

               if (tx.getInputs().size() == 1 && tx.getOutputs().size() > 1) {
                    BigInteger totalValue = BigInteger.valueOf(tx.getOutputs().stream()
                            .map(txOutput -> txOutput.getValue().intValue())
                            .reduce(0, Integer::sum));
                    assert (tx.getValue().equals(totalValue.subtract(tx.getFee())));
               }

          });
     }
}
