package com.devwmu.dc_fin_soft.controllers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.devwmu.dc_fin_soft.entities.FinUser;
import com.devwmu.dc_fin_soft.entities.FinanceGroup;
import com.devwmu.dc_fin_soft.repositories.FinUserRepository;
import com.devwmu.dc_fin_soft.repositories.FinanceGroupRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;
import java.util.List;

// Fix outputs and inputs

@RestController
@RequestMapping("groups")
@Tag(name = "Finance Groups", description = "Controls the various finance groups and who is all in them")
public class FinanceGroupController {
    private final FinUserRepository finUserRepository;
    private final FinanceGroupRepository financeGroupRepository;

    public FinanceGroupController(final FinanceGroupRepository financeGroupRepository, final FinUserRepository finaFinUserRepository, FinUserRepository finUserRepository) {
    this.financeGroupRepository = financeGroupRepository;
    this.finUserRepository = finUserRepository;
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
    @ApiResponses(value = {
         @ApiResponse(responseCode = "200",
            description = "All rows were successfully returned",
            content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = FinanceGroup.class)),
            examples = @ExampleObject(value = "[{\"delete\":null,\"deleted\":1,\"id\":1,\"read\":null,\"requests\":null,\"title”:”Test”,”write\":null}]"))}),
         @ApiResponse(responseCode = "500",
            description = "Unable to retrieve rows",
            content = {@Content(mediaType = "text/plain",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Error: unable to retrieve all finance groups"))}
         )
    })

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
    @ApiResponses(value = {
         @ApiResponse(responseCode = "200",
            description = "All filters successfully applied and returned the filtered rows",
            content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = FinanceGroup.class)),
            examples = @ExampleObject(value = "[{\"delete\":null,\"deleted\":1,\"id\":1,\"read\":null,\"requests\":null,\"title”:”Test”,”write\":null}]"))}),
         @ApiResponse(responseCode = "400",
            description = "No/wrong type of value provided for a column",
            content = {@Content(mediaType = "text/plain",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Error: non-number value passed with GREATER THAN OR EQUAL TO operator"))}),
         @ApiResponse(responseCode = "403",
             description = "Not allowed to use that operator with that column",
            content = {@Content(mediaType = "text/plain",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Error: invalid column: name passed with EQUAL operator. Pass this with LIKE operator"))})
    })
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

    @PutMapping("/edit_user/user={user}/id={id}")
    @Operation(
        summary = "Edits a user's finance group",
        description = "Modifies the finance group attribute of a user, using the id provided and the group name. Returns the user on success"
    )
    @ApiResponses(value = {
         @ApiResponse(responseCode = "200",
            description = "The finance group was successfully modified",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = FinanceGroup.class),
            examples = @ExampleObject(value = "{\"delete\":null,\"deleted\":1,\"id\":1,\"read\":null,\"requests\":null,\"title”:”Test”,”write\":null}"))}),
        @ApiResponse(responseCode = "400",
            description = "Invalid finance group id or user id",
            content = {@Content(mediaType = "text/plain",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Error: Invalid financeGroup id: 2"))}),
         @ApiResponse(responseCode = "500",
            description = "Unable to update finance group",
            content = {@Content(mediaType = "text/plain",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Error: unable to update finance group"))})
    })
    /** 
   * DESCRIPTION
   * 
   * @param user the id of the user to be added to the finance group
   * @param id the id of the finance group that the user is being added to
   * @return the user to be modified with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public ResponseEntity<?> editUserGroup(@PathVariable("user") Integer user, @PathVariable("id") Integer id){
        // custom
        // addUserToGroup(user, group): bool
        //     INPUT: Adds a specific user to a group
        //     OUTPUT: The updated User

        // modify finGroup column of users table - will have to add finGroup col to do this
        // return updated user
        Optional<FinanceGroup> financeGroupOptional = this.financeGroupRepository.findById(id);
        if (!financeGroupOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: Invalid FinanceGroup id: " + id.toString() );
        }

        FinanceGroup financeGroup = financeGroupOptional.get();

        // get the user
        Optional<FinUser> finUserToUpdateOptional = this.finUserRepository.findById(user);
        if (!finUserToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: Invalid user id: " + user.toString() );
        }

        FinUser finUserToUpdate = finUserToUpdateOptional.get();

        finUserToUpdate.setFinGroup(financeGroup.getId());
        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.finUserRepository.save(finUserToUpdate));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update financeUser");
        }
    }


    @PostMapping("/create_group")
    @Operation(
        summary = "Adds a new finance group",
        description = "Takes in a JSON representation of a FinanceGroup object and adds it to the Finance Group table. Returns the FinanceGroup object on success"
    )
    @ApiResponses(value = {
         @ApiResponse(responseCode = "200",
            description = "The finance group was successfully modified",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = FinanceGroup.class),
            examples = @ExampleObject(value = "{\"delete\":null,\"deleted\":1,\"id\":1,\"read\":null,\"requests\":null,\"title”:”Test”,”write\":null}"))}),
         @ApiResponse(responseCode = "500",
            description = "Unable to update finance group",
            content = {@Content(mediaType = "text/plain",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Error: unable to update finance group"))})
    })
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

    @PutMapping("/edit_group")
    @Operation(
        summary = "Edits an existing finance group",
        description = "Modifies the finance group specified using the id provided. Returns the updated finance group on success"
    )
    @ApiResponses(value = {
         @ApiResponse(responseCode = "200",
            description = "The finance group was successfully modified",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = FinanceGroup.class),
            examples = @ExampleObject(value = "{\"delete\":null,\"deleted\":1,\"id\":1,\"read\":null,\"requests\":null,\"title”:”Test”,”write\":null}"))}),
        @ApiResponse(responseCode = "400",
            description = "Invalid finance group id",
            content = {@Content(mediaType = "text/plain",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Error: Invalid financeGroup id: 2"))}),
         @ApiResponse(responseCode = "500",
            description = "Unable to update finance group",
            content = {@Content(mediaType = "text/plain",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Error: unable to update finance group"))})
    })
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the finance group that the user is being added to
   * @param financeGroup a financeGroup object that is used to update a finance group
   * @return the user to be modified with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public ResponseEntity<?> editFinGroup(@PathVariable("id") Integer id, @RequestBody FinanceGroup financeGroup){
        // custom
        // addUserToGroup(user, group): bool
        //     INPUT: Adds a specific user to a group
        //     OUTPUT: The updated User

        // modify finGroup column of users table - will have to add finGroup col to do this
        // return updated user
        Optional<FinanceGroup> financeGroupToUpdateOptional = this.financeGroupRepository.findById(id);
        if (!financeGroupToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: Invalid FinanceGroup id: " + id.toString() );
        }

        FinanceGroup financeGroupToUpdate = financeGroupToUpdateOptional.get();
        if (financeGroup.getTitle() != null){
            financeGroupToUpdate.setTitle(financeGroup.getTitle());
        }
        if (financeGroup.getDeleted() != null){
            financeGroupToUpdate.setDeleted(financeGroup.getDeleted());
        }
        if (financeGroup.getRead() != null){
            financeGroupToUpdate.setRead(financeGroup.getRead());
        }
        if (financeGroup.getWrite() != null){
            financeGroupToUpdate.setWrite(financeGroup.getWrite());
        }
        if (financeGroup.getDelete() != null){
            financeGroupToUpdate.setDelete(financeGroup.getDelete());
        }
        if (financeGroup.getRequests() != null){
            financeGroupToUpdate.setRequests(financeGroup.getRequests());
        }
        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.financeGroupRepository.save(financeGroupToUpdate));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update financeGroup");
        }
    }

    @PutMapping("/safe_delete_group/id={id}")
    @Operation(
        summary = "Removes a finance group",
        description = "Takes in the id of a Finance Group and sets the deleted column to 1. Returns the FinanceGroup object on success"
    )
    @ApiResponses(value = {
         @ApiResponse(responseCode = "200",
            description = "The finance group was successfully modified",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = FinanceGroup.class),
            examples = @ExampleObject(value = "{\"delete\":null,\"deleted\":1,\"id\":1,\"read\":null,\"requests\":null,\"title”:”Test”,”write\":null}"))}),
        @ApiResponse(responseCode = "400",
            description = "Invalid finance group id",
            content = {@Content(mediaType = "text/plain",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Error: Invalid financeGroup id: 2"))}),
         @ApiResponse(responseCode = "500",
            description = "Unable to update finance group",
            content = {@Content(mediaType = "text/plain",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Error: unable to update finance group"))})
    })
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

    @DeleteMapping("/delete_group/id={id}")
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the financeGroup you want to delete
   * @return returns the deleted financeGroup with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    @Operation(
        summary = "Deletes an financeGroup from the FinanceGroups table",
        description = "Deletes an financeGroup based on the id provided on success with a 200 response code and the deleted financeGroup. On error, returns an error response code and text explaining the error"
    )
    @ApiResponses(value = {
         @ApiResponse(responseCode = "200",
            description = "The finance group was successfully modified",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = FinanceGroup.class),
            examples = @ExampleObject(value = "{\"delete\":null,\"deleted\":1,\"id\":1,\"read\":null,\"requests\":null,\"title”:”Test”,”write\":null}"))}),
         @ApiResponse(responseCode = "500",
            description = "Unable to delete finance group",
            content = {@Content(mediaType = "text/plain",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Error: unable to delete finance group"))})
    })
    public ResponseEntity<?> deleteFinanceGroup(@PathVariable("id") Integer id){
        // deleteFinanceGroup(id): bool
        //     INPUT: id: Integer - The id of the item to be deleted (from the database)
        //     OUTPUT: deleted financeGroup
        // sets the delete flag to be 1 
        Optional<FinanceGroup> financeGroupToDeleteOptional = this.financeGroupRepository.findById(id);
        if (!financeGroupToDeleteOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid financeGroup id: " + id.toString()  );
        }
        FinanceGroup deleteFinanceGroup = financeGroupToDeleteOptional.get();
        this.financeGroupRepository.delete(deleteFinanceGroup);
        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(deleteFinanceGroup);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to delete financeGroup");
        }
    }



}
