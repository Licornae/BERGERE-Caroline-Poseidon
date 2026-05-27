package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.sql.Timestamp;

/**
 * Represents a credit rating entity for storing rating information such as Moody's, S&P,
 * and Fitch credit ratings along with an order number.
 * This class is mapped to the "rating" table in the database.
 * It includes validation constraints to ensure data integrity for each field.
 */
@Entity
@Table(name = "rating")
public class Rating {

    public Rating(Integer id,String moodysRating, String sandPRating, String fitchRating, int orderNumber) {
        this.id = id;
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }

    public Rating() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(
            regexp = "^[A-Z][a-z]{1,2}[1-3]?$",
            message = "Invalid Moody's rating format. Expected: Aaa, Aa1, Baa3, etc."
    )
    @Column(name="moodysRating", length=125)
    private String moodysRating;

    @Pattern(
            regexp = "^[A-Z]{1,3}[+-]?$",
            message = "Invalid S&P rating format. Expected: AAA, BB+, CCC-, etc."
    )
    @Column(name="sandPRating", length=125)
    private String sandPRating;

    @Pattern(
            regexp = "^[A-Z]{1,3}[+-]?$",
            message = "Invalid Fitch rating format. Expected: AAA, BB+, CCC-, etc."
    )
    @Column(name="fitchRating", length=125)
    private String fitchRating;

    @NotNull(message = "Order number is required")
    @PositiveOrZero(message = "Order number must be positive or zero")
    @Column(name="orderNumber")
    private Integer orderNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMoodysRating() {
        return moodysRating;
    }

    public void setMoodysRating(String moodysRating) {
        this.moodysRating = moodysRating;
    }

    public String getSandPRating() {
        return sandPRating;
    }

    public void setSandPRating(String sandPRating) {
        this.sandPRating = sandPRating;
    }

    public String getFitchRating() {
        return fitchRating;
    }

    public void setFitchRating(String fitchRating) {
        this.fitchRating = fitchRating;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

}
