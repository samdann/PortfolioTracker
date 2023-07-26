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
          String string = service.executeGetRequest();

          //Mockito.verify(string).isEmpty();
          assert (!string.isEmpty());
     }
}
