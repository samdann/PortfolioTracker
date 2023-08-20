package org.blackchain.model.blockchain.com;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockchainTx {

     @SerializedName("hash160")
     private String txHash;

     @SerializedName("block_index")
     private BigInteger blockNumber;

     private BigInteger fee;
     private long time;

     @SerializedName("result")
     private BigInteger value;
     private BigInteger balance;
     private List<TxInput> inputs;

     @SerializedName("out")
     private List<TxOutput> outputs;

}
