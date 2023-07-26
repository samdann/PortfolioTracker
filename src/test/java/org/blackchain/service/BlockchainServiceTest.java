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
          final String url = "1EEQKfgJxZ5Ci1B2wmFsAbrnfxM98SVTat";
          String string = service.executeGetRequest(url);

          //Mockito.verify(string).isEmpty();
          assert (!string.isEmpty());
     }
}
