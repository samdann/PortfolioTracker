package org.blackchain.api;

import jakarta.validation.constraints.NotNull;
import org.blackchain.model.AccountBalance;

public interface AccountAPI {

    AccountBalance getAccountBalance(@NotNull String address) throws Exception;

}
