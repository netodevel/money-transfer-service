package com.netodevel.money_transfer;

import com.netodevel.config.AppException;
import com.netodevel.money_transfer.dto.MoneyTransferRequest;
import com.netodevel.money_transfer.dto.MoneyTransferResponse;
import com.netodevel.money_transfer.entity.Account;
import com.netodevel.money_transfer.entity.TransferHistory;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static javax.ws.rs.core.Response.ok;

@Path("api/money-transfer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MoneyTransferResource {

    @POST
    @Transactional
    public Response moneyTransfer(MoneyTransferRequest moneyTransferRequest) {

        var fromAccountId = Account.findByAccountId(moneyTransferRequest.fromAccount());
        validateAccount(fromAccountId, moneyTransferRequest.fromAccount());
        //verify funds
        validateFunds(fromAccountId.amount, moneyTransferRequest.amount());

        // decrease account
        fromAccountId.amount = fromAccountId.amount.subtract(moneyTransferRequest.amount());
        fromAccountId.persist();

        // increases to account
        var toAccountId = Account.findByAccountId(moneyTransferRequest.toAccount());
        validateAccount(toAccountId, moneyTransferRequest.toAccount());

        toAccountId.amount = toAccountId.amount.add(moneyTransferRequest.amount());
        toAccountId.persist();

        var transferHistory = new TransferHistory();
        transferHistory.amout = moneyTransferRequest.amount();
        transferHistory.dateTransfer = LocalDate.now();
        transferHistory.fromAccountId = moneyTransferRequest.fromAccount();
        transferHistory.toAccountId = moneyTransferRequest.toAccount();
        transferHistory.persist();

        var response = new MoneyTransferResponse("999", "777", fromAccountId.amount);
        return ok(response).status(201).build();
    }

    private void validateFunds(BigDecimal amount, BigDecimal amount1) {
        if (amount.compareTo(amount1) < 0) {
            throw new AppException(Response.Status.CONFLICT.getStatusCode(), "Insufficient funds");
        }
    }

    private void validateAccount(Account fromAccountId, String accountRequested) {
        if (Objects.isNull(fromAccountId)) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), String.format("accountId %s is not valid", accountRequested));
        }
    }
}
