package com.nnk.springboot.domain;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;


@Entity
@Table(name = "curvepoint")
public class CurvePoint {
    // TODO: ajouter des contraintes de validation

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="curveId")
    private Integer curveId;

    @Column(name="asOfDate")
    private Timestamp asOfDate;

    @Column(name="term")
    private Double term;

    @Column(name="value")
    private Double value;

    @Column(name="creationDate")
    private Timestamp creationDate;
}
