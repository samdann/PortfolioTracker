package org.blackchain.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import org.blackchain.model.blockchain.com.BlockchainAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.ResourceUtils;

@RunWith(MockitoJUnitRunner.class)
public class BlockchainComServiceTest {

     @Mock
     HttpService httpService;
     @InjectMocks
     private BlockchainComService service;

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

          String jsonFile = readJsonFile("src/test/resources/BTCResponse.json");

          Mockito.when(httpService.executeGetRequest(Mockito.anyString()))
                  .thenReturn(jsonFile);

          BlockchainAddress bitcoinAddress = service.getBitcoinAddress(address);

          Mockito.verify(httpService).executeGetRequest(Mockito.anyString());

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

     private String readJsonFile(final String filePath) {
          try {
               File file = ResourceUtils.getFile(filePath);
               byte[] bytes = Files.readAllBytes(file.toPath());
               return new String(bytes);
          } catch (FileNotFoundException ex) {
               throw new RuntimeException();
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
     }
}
