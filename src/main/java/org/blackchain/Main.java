package org.blackchain;

import io.api.etherscan.core.impl.EtherScanApi;
import io.api.etherscan.model.Balance;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {


        EtherScanApi api = new EtherScanApi();
        Balance balance = api.account().balance("0xBaF6dC2E647aeb6F510f9e318856A1BCd66C5e19");
        System.out.println(balance);

    }
}