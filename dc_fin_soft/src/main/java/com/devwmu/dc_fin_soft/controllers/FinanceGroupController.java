package com.devwmu.dc_fin_soft.controllers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.devwmu.dc_fin_soft.entities.FinanceGroup;
import com.devwmu.dc_fin_soft.repositories.FinanceGroupRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;
import java.util.List;

// Fix outputs and inputs

@RestController
@RequestMapping("/admin/groups")
@Tag(name = "Finance Groups", description = "Controls the various finance groups and who is all in them")
public class FinanceGroupController {
    private final FinanceGroupRepository financeGroupRepository;

    public FinanceGroupController(final FinanceGroupRepository financeGroupRepository) {
    this.financeGroupRepository = financeGroupRepository;
  }
    @GetMapping("/all")
    /** 
   * DESCRIPTION
   * @return returns all of the rows in the finance group table with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    @Operation(
        summary = "Retrives all of the finance groups",
        description = "Takes in no input, and returns all of the rows in the Finance Group table"
    )
    public ResponseEntity<?> getAllFinanceGroups() {
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.financeGroupRepository.findAll());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to retrieve all finance groups");
        }
    }
    
    @PutMapping("/search")
    /** 
   * DESCRIPTION
   * 
   * @param filters an array of filter objects, which represents the columns, operations, and values to filter by
   * @return the rows of finance groups that match the filters with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error. If no filters are applied, returns all of the rows. 
  */
    @Operation(
        summary = "Filters through finance groups based on specified values",
        description = "Takes in a JSON array, where each element is a Filter object consisting of the column to filter by, the operation to filter based on, and the desired value, and returns all of the rows in the Finance Group table which match the Filter objects"
    )
    public ResponseEntity<?> filterFinanceGroups(@RequestBody Filter[] filters){
    // custom
    // filterFinanceGroups(filterArray[]): Iterable<FinGroup> 	
        //     INPUT: filters: Filter[] -  an array of filters to apply to the table
        //      ex) [{"col":"est_attendance", "op":"geq", "val":16}] - this will apply a filter for if the estimated attendance is >= 16
        //     OUTPUT: the selected rows of the finance group table
    
        // returns the finance groups that match
        Specification<FinanceGroup> spec = Specification.unrestricted();
        // goes through every filter item passed in the parameter array
        for (Filter filter: filters){
            String col = filter.getCol();
            String op = filter.getOp().toLowerCase();
            Object value = filter.getVal();

            if (value == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: No value provided for filter on column: " + col );
            }

            Specification<FinanceGroup> condition = null;
            // goes through every possiable operation for that table
            switch (op) {
                case "like":
                    try{
                        List<String> allowedCols = List.of("title");
                        if (!(allowedCols.contains(col.toLowerCase()))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with LIKE operator");
                        }
                        String lower = "%" + value.toString().toLowerCase() + "%";
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.like(criteraBuilder.lower(root.get(col)), lower);
                        break;
                    } catch (ClassCastException e){
                        System.out.println(e );
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: non-string value passed with LIKE operator");
                    }
                case "eq":
                    List<String> notAllowedCols = List.of("title");
                        if (notAllowedCols.contains(col.toLowerCase())){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with EQUAL operator. Pass this with LIKE operator");
                        }
                    condition = (root, query, criteriaBuilder) -> 
                        criteriaBuilder.equal(root.get(col), value);
                    break;
            }
            // combines the conditons together to stack them
            if (condition != null){
                spec = spec.and(condition);
            }

        }
        Iterable<FinanceGroup> financeGroups =  this.financeGroupRepository.findAll(spec);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(financeGroups);
    }

    @PutMapping("/add_user/user={user}")
    @Operation(
        summary = "Adds a user to a specific finance group",
        description = "Modifies the finance group attribute of a user, using the id provided and the group name. Returns the user on success"
    )
    /** 
   * DESCRIPTION
   * 
   * @param user the id of the user to be added to the finance group
   * @return the user to be modified with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public FinanceGroup addUserToGroup(){
        // custom
        // addUserToGroup(user, group): bool
        //     INPUT: Adds a specific user to a group
        //     OUTPUT: The updated User

        // modify finGroup column of users table - will have to add finGroup col to do this
        // return updated user
        return new FinanceGroup();
    }

    @PutMapping("/remove_user/user={user}")
    @Operation(
        summary = "Deletes a user from a specific finance group",
        description = "Modifies the finance group attribute of a user, using the id provided. Returns the user on success"
    )
    /** 
   * DESCRIPTION
   * 
   * @param user the id of the user to be added to a finance group
   * @return returns the user to be modified with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public FinanceGroup removeUserFromGroup(){
        // custom
        // removeUserFromGroup(user, group): bool
        //     Removes a specific user from a group
        //     OUTPUT: The updated User

        // modify finGroup column of users table - will have to add finGroup col to do this
        // return updated user
        return new FinanceGroup();
    }

    @PostMapping("/create_group")
    @Operation(
        summary = "Adds a new finance group",
        description = "Takes in a JSON representation of a FinanceGroup object and adds it to the Finance Group table. Returns the FinanceGroup object on success"
    )
    /** 
   * DESCRIPTION
   * 
   * @param financeGroup a finance group object representing the group to be created
   * @return the created group with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public ResponseEntity<?> createGroup(@RequestBody FinanceGroup financeGroup){
        // createGroup(name): bool
        //     Creates a new finance group
        //      INPUT: financeGroup: FinanceGroup - the new financeGroup row to be created
        //     OUTPUT: the new finance group
        // adds the new finance group to the database
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.financeGroupRepository.save(financeGroup));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to create finance group: " + financeGroup.toString() );
        }

    }

    @PutMapping("/remove_group/id={id}")
    @Operation(
        summary = "Removes a finance group",
        description = "Takes in the id of a Finance Group and sets the deleted column to 1. Returns the FinanceGroup object on success"
    )
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the group to be removed
   * @return the modifed group with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public ResponseEntity<?> removeGroup(@PathVariable("id") Integer id){
        // removeGroup(name): bool
        //     INPUT: id: Integer - the id of the finance group to be removed
        //     OUTPUT: removed group
        // sets the deleted flag on that specific finance group to be 1
        Optional<FinanceGroup> finGroupToDeleteOptional = this.financeGroupRepository.findById(id);
        if (!finGroupToDeleteOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid finance group id: " + id.toString() );
        }
        FinanceGroup deleteFinGroup = finGroupToDeleteOptional.get();
        deleteFinGroup.setDeleted(1);
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.financeGroupRepository.save(deleteFinGroup));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update finance group");
        } 
    }

}
