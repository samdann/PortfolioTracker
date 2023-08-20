package org.blackchain.model.blockchain.com;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TxInput {

     @SerializedName("prev_out")
     private TxOutput previousOutput;

}
