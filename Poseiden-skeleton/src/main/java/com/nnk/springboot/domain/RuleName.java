package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;

/**
 * Represents a RuleName entity that holds information about a business rule.
 * This class is mapped to the "rulename" table in the database.
 */
@Entity
@Table(name = "rulename")
public class RuleName {

    public RuleName(Integer id, String name, String description, String json, String sqlStr, String template, String sqlPart) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.json = json;
        this.sqlStr = sqlStr;
        this.template = template;
        this.sqlPart = sqlPart;
    }

    public RuleName() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", length=125, nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(name="description", length=125)
    private String description;

    @Column(name="json", length=125)
    private String json;

    @Column(name="template", length=125)
    private String template;

    @Column(name="sql_str", length=125)
    private String sqlStr;

    @Column(name="sql_part", length=125)
    private String sqlPart;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }

    public String getSqlPart() {
        return sqlPart;
    }

    public void setSqlPart(String sqlPart) {
        this.sqlPart = sqlPart;
    }
}
