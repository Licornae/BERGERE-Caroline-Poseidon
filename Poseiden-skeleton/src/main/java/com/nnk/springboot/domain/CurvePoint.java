package com.nnk.springboot.domain;

import jakarta.validation.constraints.*;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;


/**
 * Entity representing a curve point in the system. This class is mapped
 * to the "curvepoint" table in the database and contains details about
 * a specific point on a financial curve, such as its ID, curve ID, term,
 * value, and associated timestamps.
 */
@Entity
@Table(name = "curvepoint")
public class CurvePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Curve ID is required")
    @Positive(message = "Curve ID must be positive")
    @Column(name="CurveId")
    private Integer curveId; //Identifiant de la courbe à laquelle ce point appartient.

    @PastOrPresent(message= "As Of Date cannot be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="asOfDate")
    private LocalDate asOfDate; //Date à laquelle ce point de courbe est valide

    @Positive(message = "Term must be positive")
    @Column(name="term")
    private Double term;  //Échéance du point (en années ou fraction d'année). Ex: 0.5 = 6 mois, 5.0 = 5 ans.

    @Column(name="value")
    private Double value; //Valeur du taux (en %) à cette échéance.

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name="creationDate")
    private Timestamp creationDate; //Date de création du point dans le système.

    @PrePersist
    public void setCreationDateAutomatically() {
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurveId() {
        return curveId;
    }

    public void setCurveId(Integer curveId) {
        this.curveId = curveId;
    }

    public LocalDate getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(LocalDate asOfDate) {
        this.asOfDate = asOfDate;
    }

    public Double getTerm() {
        return term;
    }

    public void setTerm(Double term) {
        this.term = term;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
