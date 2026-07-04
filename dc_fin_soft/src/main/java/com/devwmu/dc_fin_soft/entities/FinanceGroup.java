package com.devwmu.dc_fin_soft.entities;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "FinanceGroup")
public class FinanceGroup {
        // initalization
    @Id
    @GeneratedValue()
    @Column(name = "id")
    private Integer id;

    @Schema(description = "The title of the finance group", example = "Admin", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="title")
    private String title;

    @Schema(description = "A flag for if this finance group is deleted or not", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="deleted")
    private Integer deleted;


    // getters
    public Integer getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public Integer getDeleted(){
        return this.deleted;
    }

    // setters
    public void setTitle(String title){
        this.title = title;
    }

    public void setDeleted(Integer deleted){
        this.deleted = deleted;
    }
}
