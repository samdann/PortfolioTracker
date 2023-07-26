package org.blackchain.model.blockchain.com;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockchainAddress {

     private String address;
     private List<BlockchainTx> txs;

}
