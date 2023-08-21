package org.blackchain.model.blockchain.com;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TxOutput {

     @SerializedName("addr")
     private String address;
     private BigDecimal value;

}
