package com.netodevel.money_transfer;


import com.netodevel.money_transfer.dto.MoneyTransferRequest;
import com.netodevel.money_transfer.dto.MoneyTransferResponse;
import com.netodevel.money_transfer.entity.Account;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static javax.ws.rs.core.Response.ok;

@Path("api/money-transfer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MoneyTransferResource {

    @POST
    @Transactional
    public Response moneyTransfer(MoneyTransferRequest moneyTransferRequest) {

        // decrease account
        var fromAccountId = Account.findByAccountId("999");
        fromAccountId.amount = fromAccountId.amount.subtract(new BigDecimal("50"));
        fromAccountId.persist();

        // increases to account
        var toAccountId = Account.findByAccountId("777");
        toAccountId.amount = toAccountId.amount.add(new BigDecimal("50"));
        toAccountId.persist();

        var response = new MoneyTransferResponse("999", "777", BigDecimal.valueOf(50L));
        return ok(response).status(201).build();
    }
}
