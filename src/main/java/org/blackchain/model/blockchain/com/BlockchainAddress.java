package org.blackchain.model.blockchain.com;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockchainAddress {

     private String address;

     @SerializedName("total_received")
     private BigInteger totalReceived;

     @SerializedName("total_sent")
     private BigInteger totalSent;

     @SerializedName("txs")
     private List<BlockchainTx> txs;

     @Override
     public String toString() {
          return "BlockchainAddress{" + "address='" + address + '\'' + ", txs=" + txs
                  + '}';
     }
}
