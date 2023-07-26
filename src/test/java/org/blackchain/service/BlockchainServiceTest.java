package org.blackchain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BlockchainServiceTest {

     @InjectMocks
     private BlockchainService service;

     @Test
     public void testExecuteGetRequest() {
          final String url = "3Fb3VStsZfAV3KHLVuFuRNLQLDzgH4BKby";
          String string = service.executeGetRequest(url);

          //Mockito.verify(string).isEmpty();
          assert (!string.isEmpty());
     }
}
