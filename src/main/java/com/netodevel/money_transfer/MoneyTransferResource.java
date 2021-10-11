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
import java.util.Objects;

import static javax.ws.rs.core.Response.ok;

@Path("api/money-transfer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MoneyTransferResource {

    @POST
    @Transactional
    public Response moneyTransfer(MoneyTransferRequest moneyTransferRequest) {

        // decrease account
        var fromAccountId = Account.findByAccountId(moneyTransferRequest.fromAccount());
        if (Objects.isNull(fromAccountId)) {
            return Response.status(400).build();
        }
        fromAccountId.amount = fromAccountId.amount.subtract(moneyTransferRequest.amount());
        fromAccountId.persist();

        // increases to account
        var toAccountId = Account.findByAccountId(moneyTransferRequest.toAccount());
        if (Objects.isNull(toAccountId)) {
            return Response.status(400).build();
        }
        toAccountId.amount = toAccountId.amount.add(moneyTransferRequest.amount());
        toAccountId.persist();

        var response = new MoneyTransferResponse("999", "777", fromAccountId.amount);
        return ok(response).status(201).build();
    }
}
