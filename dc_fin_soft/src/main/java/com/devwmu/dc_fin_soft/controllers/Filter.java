package com.devwmu.dc_fin_soft.controllers;

import io.swagger.v3.oas.annotations.media.Schema;

public class Filter{
    @Schema(description = "The column to filter by", example = "feeFlag", requiredMode = Schema.RequiredMode.REQUIRED)
    private String col;
    @Schema(description = "The operator to filter by", example = "leq", requiredMode = Schema.RequiredMode.REQUIRED)
    private String op;
    @Schema(description = "The value to filter by", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Object val;

    public Filter(String col, String op, Object val) {
        this.col = col;
        this.op = op;
        this.val = val;
    }

    public Filter() {}



    public String getCol(){
        return this.col;
    }

    public String getOp(){
        return this.op;
    }

    public Object getVal(){
        return this.val;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    

}