package org.blackchain.model.transaction;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.blackchain.model.Transaction;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BtcTransaction extends Transaction {

     private String namee;
}
