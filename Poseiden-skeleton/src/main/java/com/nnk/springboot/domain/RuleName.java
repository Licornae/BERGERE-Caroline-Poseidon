package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
@Table(name = "rulename")
public class RuleName {
    // TODO: ajouter des contraintes de validation

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", length=125)
    private String name;
    @Column(name="description", length=125)
    private String description;
    @Column(name="json", length=125)
    private String json;
    @Column(name="template", length=125)
    private String template;
    @Column(name="sqlStr", length=125)
    private String sqlStr;
    @Column(name="sqlPart", length=125)
    private String sqlPart;
}
