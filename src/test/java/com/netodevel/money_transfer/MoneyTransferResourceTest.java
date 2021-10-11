package com.netodevel.money_transfer;


import com.netodevel.money_transfer.dto.MoneyTransferRequest;
import com.netodevel.money_transfer.entity.Account;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class MoneyTransferResourceTest {

    @Inject
    DataSource dataSource;

    org.assertj.db.type.Table tableAccounts;

    @BeforeEach
    public void setUp() {
        RestAssured.config()
                .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));

        tableAccounts = new org.assertj.db.type.Table(dataSource, "accounts");
    }

    @Test
    @DisplayName("given a money transfer valid then should return a status code 201")
    public void shouldReturnStatusCreated() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MoneyTransferRequest("999", "777", new BigDecimal("1000")))
                .when().post("api/money-transfer")
                .then().statusCode(201);
    }

    @Test
    @DisplayName("given a money transfer valid then should return amount decreased")
    public void shouldReturnDataOfMoneyTransfer() {
        var accountInitial = Account.findByAccountId("999");
        accountInitial.amount = new BigDecimal("1000");
        accountInitial.accountId = "999";
        accountInitial.persist();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MoneyTransferRequest("999", "777", new BigDecimal("20")))
                .when().post("api/money-transfer")
                .then().body("fromAccount", Is.is("999"))
                .body("toAccount", Is.is("777"))
                .body("amount", Is.is(50));

        BigDecimal amountAfterTransfer = amountByAccountId("999");
        assertEquals(BigDecimal.valueOf(980L), amountAfterTransfer);
    }

    @Test
    @DisplayName("given a money transfer should increment amount of account received")
    public void shouldUpdatedDataBase() {
        var amountBeforeTransfer = Account.findByAccountId("777").amount;

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MoneyTransferRequest("999", "777", new BigDecimal("1000")))
                .when().post("api/money-transfer")
                .then().statusCode(201);

        BigDecimal amountAfterTransfer = amountByAccountId("777");

        int greaterThan = 1;
        assertEquals(greaterThan, amountAfterTransfer.compareTo(amountBeforeTransfer));
    }

    private BigDecimal amountByAccountId(String s) {
        return (BigDecimal) tableAccounts.getRowsList()
                .stream()
                .filter(e -> e.getColumnValue("accountId").getValue().equals(s))
                .findFirst().get().getColumnValue("amount").getValue();
    }

    @Test
    @DisplayName("given a money transfer valid should to decrease amount of origin account")
    public void shouldDecreaseOriginAccount() {
        var amountBeforeTransfer = Account.findByAccountId("999").amount;

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MoneyTransferRequest("999", "777", new BigDecimal("20")))
                .when().post("api/money-transfer")
                .then().statusCode(201);

        BigDecimal amountAfterTransfer = amountByAccountId("999");

        int lessThen = -1;
        assertEquals(lessThen, amountAfterTransfer.compareTo(amountBeforeTransfer));
    }

}