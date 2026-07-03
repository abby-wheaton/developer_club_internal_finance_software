package com.devwmu.dc_fin_soft.controllers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import com.devwmu.dc_fin_soft.entities.FinanceGroup;
import com.devwmu.dc_fin_soft.repositories.FinanceGroupRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

// Fix outputs and inputs

@RestController
@RequestMapping("/admin/groups")
@Tag(name = "Finance Groups", description = "Controls the various finance groups and who is all in them")
public class FinanceGroupController {
    private final FinanceGroupRepository financeGroupRepository;

    public FinanceGroupController(final FinanceGroupRepository financeGroupRepository) {
    this.financeGroupRepository = financeGroupRepository;
  }
    @GetMapping("/finance_groups")
    @Operation(
        summary = "Retrives all of the finance groups",
        description = "Takes in no input, and returns all of the rows in the Finance Group table"
    )
    public Iterable<FinanceGroup> getAllFinanceGroups() {
        // INPUT: N/A
        // OUTPUT: returns all rows in the finance group table
        return this.financeGroupRepository.findAll();
    }
    
    @PutMapping("/finance_groups/search")
    @Operation(
        summary = "Filters through finance groups based on specified values",
        description = "Takes in a JSON array, where each element is a Filter object consisting of the column to filter by, the operation to filter based on, and the desired value, and returns all of the rows in the Finance Group table which match the Filter objects"
    )
    public Iterable<FinanceGroup> filterFinanceGroups(@RequestBody Filter[] filters){
    // custom
    // filterFinanceGroups(filterArray[]): Iterable<FinGroup> 	
        //     INPUT: filters: Filter[] -  an array of filters to apply to the table
        //      ex) [{"col":"est_attendance", "op":"geq", "val":16}] - this will apply a filter for if the estimated attendance is >= 16
        //     OUTPUT: the selected rows of the finance group table
    
        // returns the events that match
        Specification<FinanceGroup> spec = Specification.unrestricted();
        // goes through every filter item passed in the parameter array
        for (Filter filter: filters){
            String col = filter.getCol();
            String op = filter.getOp().toLowerCase();
            Object value = filter.getVal();

            if (value == null){
                continue;
            }

            Specification<FinanceGroup> condition = null;
            // goes through every possiable operation for that table
            switch (op) {
                case "like":
                    try{
                        String lower = "%" + value.toString().toLowerCase() + "%";
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.like(criteraBuilder.lower(root.get(col)), lower);
                        break;
                    } catch (ClassCastException e){
                        System.out.println(e + "\n\n\n");
                        break;
                    }
                case "eq":
                    condition = (root, query, criteriaBuilder) -> 
                        criteriaBuilder.equal(root.get(col), value);
                    break;
            }
            // combines the conditons together to stack them
            if (condition != null){
                spec = spec.and(condition);
            }

        }
        return this.financeGroupRepository.findAll(spec);
    }

    @PutMapping("/finance_group_users/add_{user}")
    @Operation(
        summary = "Adds a user to a specific finance group",
        description = "Modifies the finance group attribute of a user, using the id provided and the group name. Returns the user on success"
    )
    public FinanceGroup addUserToGroup(){
        // custom
        // addUserToGroup(user, group): bool
        //     INPUT: Adds a specific user to a group
        //     OUTPUT: The updated User

        // modify finGroup column of users table - will have to add finGroup col to do this
        // return updated user
        return new FinanceGroup();
    }

    @PutMapping("/finance_group_users/remove_{user}")
    @Operation(
        summary = "Deletes a user from a specific finance group",
        description = "Modifies the finance group attribute of a user, using the id provided. Returns the user on success"
    )
    public FinanceGroup removeUserFromGroup(){
        // custom
        // removeUserFromGroup(user, group): bool
        //     Removes a specific user from a group
        //     OUTPUT: The updated User

        // modify finGroup column of users table - will have to add finGroup col to do this
        // return updated user
        return new FinanceGroup();
    }

    @PostMapping("/finance_group")
    @Operation(
        summary = "Adds a new finance group",
        description = "Takes in a JSON representation of a FinanceGroup object and adds it to the Finance Group table. Returns the FinanceGroup object on success"
    )
    public FinanceGroup createGroup(@RequestBody FinanceGroup financeGroup){
        // createGroup(name): bool
        //     Creates a new finance group
        //      INPUT: financeGroup: FinanceGroup - the new financeGroup row to be created
        //     OUTPUT: the new finance group
        // adds the new finance group to the database
        return this.financeGroupRepository.save(financeGroup);
    }

    @PutMapping("/finance_group_{id}")
    @Operation(
        summary = "Removes a finance group",
        description = "Takes in the id of a Finance Group and sets the deleted column to 1. Returns the FinanceGroup object on success"
    )
    public FinanceGroup removeGroup(@PathVariable("id") Integer id){
        // removeGroup(name): bool
        //     INPUT: id: Integer - the id of the finance group to be removed
        //     OUTPUT: removed group
        // sets the deleted flag on that specific finance group to be 1
        Optional<FinanceGroup> finGroupToDeleteOptional = this.financeGroupRepository.findById(id);
        if (!finGroupToDeleteOptional.isPresent()){
            return null;
        }
        FinanceGroup deleteFinGroup = finGroupToDeleteOptional.get();
        deleteFinGroup.setDeleted(1);
        return this.financeGroupRepository.save(deleteFinGroup);
    }

}
