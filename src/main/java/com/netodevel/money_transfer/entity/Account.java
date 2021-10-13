package com.netodevel.money_transfer.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account extends PanacheEntity {

    @Column(name = "account_id")
    public String accountId;
    public BigDecimal amount;

    public static Account findByAccountId(String accountId) {
        return find("accountId", accountId).firstResult();
    }
}
