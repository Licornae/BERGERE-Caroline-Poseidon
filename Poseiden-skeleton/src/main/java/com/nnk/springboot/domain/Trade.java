package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.sql.Timestamp;


/**
 * Represents a Trade entity mapped to the "trade" table in the database.
 * This class contains information about financial trades, including details
 * such as account, type of trade, quantities, prices, benchmarks, dates, and other metadata.
 * Each field in this class corresponds to a column in the "trade" table.
 * The entity is annotated with JPA and Jakarta Bean Validation annotations
 * to define database mapping and validation constraints.
 */
@Entity
@Table(name = "trade")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tradeId;

    @NotBlank(message = "Account is mandatory")
    @Column(name="account", length=30)
    private String account;

    @NotBlank(message = "Type is mandatory")
    @Column(name="type", length=30)
    private String type;

    @Column(name="buyQuantity")
    private Double buyQuantity;

    @Column(name="sellQuantity")
    private Double sellQuantity;

    @Column(name="buyPrice")
    private Double buyPrice;

    @Column(name="sellPrice")
    private Double sellPrice;

    @Column(name="benchmark", length=125)
    private String benchmark;

    @Column(name="tradeDate")
    private Timestamp tradeDate;

    @Column(name="security", length=125)
    private String security;

    @Column(name="status", length=10)
    private String status;

    @Column(name="trader", length=125)
    private String trader;

    @Column(name="book", length=125)
    private String book;

    @Column(name="creationName", length=125)
    private String creationName;

    @Column(name="creationDate")
    private Timestamp creationDate;

    @Column(name="revisionName", length=125)
    private String revisionName;

    @Column(name="revisionDate")
    private Timestamp revisionDate;

    @Column(name="dealName", length=125)
    private String dealName;

    @Column(name="dealType", length=125)
    private String dealType;

    @Column(name="sourceListId", length=125)
    private String sourceListId;

    @Column(name="side", length=125)
    private String side;

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(Double sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    public Double getBuyQuantity() {
        return buyQuantity;
    }

    public void setBuyQuantity(Double buyQuantity) {
        this.buyQuantity = buyQuantity;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(String benchmark) {
        this.benchmark = benchmark;
    }

    public Timestamp getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Timestamp tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrader() {
        return trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }

    public String getCreationName() {
        return creationName;
    }

    public void setCreationName(String creationName) {
        this.creationName = creationName;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getRevisionName() {
        return revisionName;
    }

    public void setRevisionName(String revisionName) {
        this.revisionName = revisionName;
    }

    public Timestamp getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(Timestamp revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getSourceListId() {
        return sourceListId;
    }

    public void setSourceListId(String sourceListId) {
        this.sourceListId = sourceListId;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }
}
