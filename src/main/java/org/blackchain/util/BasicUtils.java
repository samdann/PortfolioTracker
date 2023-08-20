package org.blackchain.util;

import java.time.Instant;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.params.MainNetParams;
import org.blackchain.model.Blockchain;

public class BasicUtils {

     public static String instantToStringEpoch(final Instant time) {
          return String.valueOf(time.toEpochMilli() / 1000);
     }

     public static Blockchain getAddressBlockchain(final String address) {
          if (EthereumUtils.isEthereumAddress(address)) {
               return Blockchain.ETHEREUM;
          } else if (isBitcoinAddress(address)) {
               return Blockchain.BITCOIN;
          } else {
               return Blockchain.NONE;
          }
     }

     private static boolean isBitcoinAddress(final String address) {
          try {
               Address.fromString(MainNetParams.get(), address);
               return true;
          } catch (AddressFormatException ex) {
               return false;
          }

     }

}
