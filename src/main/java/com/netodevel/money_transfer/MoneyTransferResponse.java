package com.netodevel.money_transfer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record MoneyTransferResponse(String fromAccount, String toAccount, BigDecimal amount) {
}
