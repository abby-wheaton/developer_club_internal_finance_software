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

    @Schema(description = "A flag for if this finance group has general permission to read", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="read")
    private Integer read;

    @Schema(description = "A flag for if this finance group has general permission to write", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="write")
    private Integer write;

    @Schema(description = "A flag for if this finance group has general permission to delete", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="delete")
    private Integer delete;

    @Schema(description = "A flag for if this finance group has permission to read/write/delete their own requests", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="requests")
    private Integer requests;


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

    

    public Integer getRead() {
        return read;
    }

    public Integer getWrite() {
        return write;
    }

    public Integer getDelete() {
        return delete;
    }

    public Integer getRequests() {
        return requests;
    }

    // setters
    public void setTitle(String title){
        this.title = title;
    }

    public void setDeleted(Integer deleted){
        this.deleted = deleted;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public void setWrite(Integer write) {
        this.write = write;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }

    public void setRequests(Integer requests) {
        this.requests = requests;
    }

    
}
