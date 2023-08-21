package org.blackchain.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
     public void testGetBitcoinAddressWithOneInputMultipleOutputs() {
          final String address = "bc1qf6k4eh5d3zxausq8hc5xynfcp0waee2c7sc86c";
          final String jsonFilePath = ("src/test/resources/BTCResponse01.json");
          testGetBitcoinAddress(address, jsonFilePath);
     }

     @Test
     public void testGetBitcoinAddressWithMultipleInputsOneOutput() {
          final String address = "bc1qjqppdz7v8rmy5ldnvzgx82qd8km2qg0ld7795f";
          final String jsonFilePath = ("src/test/resources/BTCResponse02.json");
          testGetBitcoinAddress(address, jsonFilePath);
     }

     private void testGetBitcoinAddress(final String address, final String jsonFilePath) {

          String jsonFile = readJsonFile(jsonFilePath);

          String url = BlockchainComService.URL + address;

          Mockito.when(httpService.executeGetRequest(url)).thenReturn(jsonFile);

          BlockchainAddress bitcoinAddress = service.getBitcoinAddress(address);

          Mockito.verify(httpService).executeGetRequest(Mockito.anyString());

          bitcoinAddress.getTxs().forEach(tx -> {
               if (tx.getInputs().size() > 1 && tx.getOutputs().size() == 1) {
                    long inputsValue = tx.getInputs().stream()
                            .map(txInput -> txInput.getPreviousOutput().getValue()
                                    .longValue()).reduce(0l, Long::sum);
                    assert (inputsValue == tx.getValue().longValue() + tx.getFee()
                            .longValue());
               }

               if (tx.getInputs().size() == 1 && tx.getOutputs().size() > 1) {
                    long outputsValue = tx.getOutputs().stream()
                            .map(txOutput -> txOutput.getValue().longValue())
                            .reduce(0l, Long::sum);
                    long txInputValue = tx.getInputs().get(0).getPreviousOutput()
                            .getValue().longValue();
                    assert (txInputValue == outputsValue + tx.getFee().longValue());
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
