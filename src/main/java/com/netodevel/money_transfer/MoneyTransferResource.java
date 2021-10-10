package com.netodevel.money_transfer;


import io.quarkus.logging.Log;

import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static javax.ws.rs.core.Response.ok;

@Path("api/money-transfer")
public class MoneyTransferResource {

    @POST
    @Transactional
    public Response moneyTransfer() {

        //find to from accountId
        //update fromAccount dicrement

        //find to to accountId
        var toAccountId = Account.findByAccountId("777");
        toAccountId.amount = toAccountId.amount.add(new BigDecimal("50"));
        toAccountId.persist();
        //update fromAccount increment

        System.out.println(toAccountId.amount);

        var response = new MoneyTransferResponse("999", "777", BigDecimal.valueOf(50L));
        return ok(response).status(201).build();
    }
}
