package com.netodevel.money_transfer.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transfer_history")
public class TransferHistory extends PanacheEntity {

    @Column(name = "amount")
    public BigDecimal amout;

    @Column(name = "from_account_id")
    public String fromAccountId;

    @Column(name = "to_account_id")
    public String toAccountId;

    @Column(name = "date_transfer")
    public LocalDate dateTransfer;
}
