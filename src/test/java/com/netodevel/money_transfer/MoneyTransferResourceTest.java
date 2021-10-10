package com.netodevel.money_transfer;


import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class MoneyTransferResourceTest {

    @BeforeEach
    @Transactional
    public void setUp() {
        RestAssured.config()
                .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));

//        prepareData();
    }

    @Test
    @DisplayName("given a money transfer valid then should return a status code 201")
    public void shouldReturnStatusCreated() {
        given()
                .when().post("api/money-transfer")
                .then().statusCode(201);
    }

    @Test
    @DisplayName("given a money transfer valid then should return a full response")
    public void shouldReturnDataOfMoneyTransfer() {
        given()
                .when().post("api/money-transfer")
                .then().body("fromAccount", Is.is("999"))
                .body("toAccount", Is.is("777"))
                .body("amount", Is.is(50));
    }

    @Test
    @DisplayName("given a money transfer should increment amount of account received")
    public void shouldUpdatedDataBase() {
        var amountBeforeTransfer = Account.findByAccountId("777").amount;

        given()
                .when().post("api/money-transfer")
                .then().statusCode(201);

        var amountAfterTransfer = Account.findByAccountId("777").amount;

        int greaterThan = 1;
        assertEquals(greaterThan, amountAfterTransfer.compareTo(amountBeforeTransfer));
    }

    @AfterEach
    @Transactional
    public void tearDown() {
        Account.deleteAll();
    }

    private void prepareData() {
        var toAccount = new Account();
        toAccount.accountId = "777";
        toAccount.amount = new BigDecimal("50");
        toAccount.persist();
    }

}