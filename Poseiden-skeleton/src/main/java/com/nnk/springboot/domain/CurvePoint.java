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
    @Column(name="curve_id")
    private Integer curveId; //Identifiant de la courbe à laquelle ce point appartient (ex: courbe EURIBOR, courbe des obligations d'État).

    @NotNull(message= "As Of Date is required")
    @PastOrPresent(message= "As Of Date cannot be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="as_of_date")
    private LocalDate asOfDate; //Date à laquelle ce point de courbe est valide

    @NotNull(message = "Term is required")
    @Positive(message = "Term must be positive")
    @Column(name="term")
    private Double term;  //Échéance du point (en années ou fraction d'année). Ex: 0.5 = 6 mois, 5.0 = 5 ans.

    @NotNull(message = "Value is required")
    @DecimalMin(value = "0.0", message = "Value must be positive or zero")
    @Column(name="value")
    private Double value; //Valeur du taux (en %) à cette échéance. Ex: 1.2 = 1.2%.

    //@NotNull(message = "Creation Date is required")
    @PastOrPresent(message = "Creation Date cannot be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name="creation_date")
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
