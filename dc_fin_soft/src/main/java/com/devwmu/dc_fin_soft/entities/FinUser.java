package com.devwmu.dc_fin_soft.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "FinUser")
public class FinUser {
    // initalization
    @Id
    @GeneratedValue
    private Integer id;

    @Schema(description = "The name of the user", example = "Adam", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="name")
    private String name;

    @Schema(description = "The id of the finance group", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="fin_group")
    private String finGroup;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFinGroup(String finGroup) {
        this.finGroup = finGroup;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFinGroup() {
        return finGroup;
    }

}