package com.netodevel.seed;

import com.netodevel.money_transfer.Account;
import io.quarkus.logging.Log;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@ApplicationScoped
public class PrepareData {

    void onStart(@Observes StartupEvent ev) {
        execute();
        Log.info("seed executed");
    }

    void onStop(@Observes ShutdownEvent ev) {
        Log.info("The application is stopping...");
    }

    @Transactional
    public void execute() {
        var toAccountId = new Account();
        toAccountId.accountId = "777";
        toAccountId.amount = new BigDecimal("50");
        toAccountId.persist();
    }

}
