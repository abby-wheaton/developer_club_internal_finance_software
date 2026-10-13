package com.devwmu.dc_fin_soft.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.devwmu.dc_fin_soft.entities.Expense;
import com.devwmu.dc_fin_soft.repositories.ExpenseRepository;
import com.devwmu.dc_fin_soft.repositories.SourceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@WebMvcTest(Expense.class)
public class ExpenseControllerTest {
    @Autowired 
    private MockMvc mockMvc;

    @MockitoBean 
    private SourceRepository sourceRepository;

    @MockitoBean 
    private ExpenseRepository expenseRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());
    // web level testing - tests that path goes to correct function

    // allTests
        // all expenses success
        // all expenses empty test
        // all expenses exception test


    // filterExpensesTest{
        // individual tests
        // filter one success test
        // filter no value test
        // filter invalid col like test
        // filter invalid col bw test
        // filter invalid val bw test
        // filter invalid col leq test
        // filter invalid val leq test
        // filter invalid col geq test
        // filter invalid val geq test
        // filter invlaid col eq test
        // filter invalid val eq test

    // editItemTest{
        // individual tests
        // edit success test
        // edit invalid id test
        // edit null test 
        // edit failire test
    

    // budgetItemTest{
        // individual tests
        // create success test
        // create failure test

    

    // FoodFlagItemTest{
        // individual tests
        // flag one test
        // flag invalid id test
        // flag failure test
    

    // requestedFlagItemTest{
        // individual tests
        // flag one test
        // flag invalid id test
        // flag failure test

    // SBuyingFlagItemTest{
        // individual tests
        // flag one test
        // flag invalid id test
        // flag failure test

    // fBuyingFlagItemTest{
        // individual tests
        // flag one test
        // flag invalid id test
        // flag failure test

    // pickedUpFlagItemTest{
        // individual tests
        // flag one test
        // flag invalid id test
        // flag failure test

    // reimbursedFlagItemTest{
        // individual tests
        // flag one test
        // flag invalid id test
        // flag failure test

    // safeDeleteItemTest{
        // individual tests
        // safe delete one test
        // safe delete invalid id test
        // safe delete failure test

    // deleteItemTest{
        // individual tests
        // delete one test
        // delete invalid id test
        // delete failure test    

    // calculateRecommendedTotalPriceTest{
        // individual tests
        // total success test
        // total failure test

    // calculateRecommendedSourceTest{
        // individual tests
        // source success
        // unable to find source test    

}
