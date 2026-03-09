package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "rating")
public class Rating {
    // TODO: ajouter des contraintes de validation

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="moodysRating", length=125)
    private String moodysRating;
    @Column(name="sandPRating", length=125)
    private String sandPRating;
    @Column(name="fitchRating", length=125)
    private String fitchRating;
    @Column(name="orderNumber")
    private Integer orderNumber;
}
