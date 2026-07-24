package com.devwmu.dc_fin_soft.controllers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.devwmu.dc_fin_soft.repositories.SourceRepository;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.devwmu.dc_fin_soft.entities.Source;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/admin/sources")
@Tag(name = "Sources", description = "This controller interacts with the sources table in various ways")
public class SourceController {
    private final SourceRepository sourceRepository;

    public SourceController(final SourceRepository sourceRepository) {
    this.sourceRepository = sourceRepository;
  }

/** 
   * DESCRIPTION
   * 
   * 
   * @return returns all of the rows of the sources table with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    @GetMapping("/all")
    @Operation(
        summary = "Retrives all of the sources",
        description = "Takes in no input, and returns all of the rows in the Sources table"
    )
    @ApiResponses(value = {
         @ApiResponse(responseCode = "201", description = "Book successfully created"),
         @ApiResponse(responseCode = "400", description = "Invalid input supplied")
    })
    public ResponseEntity<?> getAllSources() {   
        //      OUTPUT: all of the sources

        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.sourceRepository.findAll());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to retrieve all sources");
        }
    }

    @PutMapping("/search")
    @Operation(
        summary = "Filters through the sources based on specified values",
        description = "Takes in a JSON array, where each element is a Filter object consisting of the column to filter by, the operation to filter based on, and the desired value, and returns all of the rows in the Sources table which match the Filter objects"
    )
    /** 
   * DESCRIPTION
   * 
   * @param filters an array of filter objects, which represents the columns, operations, and values to filter by
   * @return the rows of sources that match the filters with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error. If no filters are applied, returns all of the rows. 
  */
    public ResponseEntity<?> filterSources(@RequestBody Filter[] filters) {
        // returns the sources that match
        Specification<Source> spec = Specification.unrestricted();
        for (Filter filter: filters){
            String col = filter.getCol();
            String op = filter.getOp().toLowerCase();
            Object value = filter.getVal();

            if (value == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: No value provided for filter on column: " + col );
            }

            Specification<Source> condition = null;
            switch (op) {
                case "like":
                    try{
                        List<String> allowedCols = List.of("name", "type");
                        if (!(allowedCols.contains(col.toLowerCase()))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with LIKE operator");
                        }
                        String lower = "%" + value.toString().toLowerCase() + "%";
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.like(criteraBuilder.lower(root.get(col)), lower);
                        break;
                    }
                    catch (ClassCastException e){
                        System.out.println(e );
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: non-string value passed with LIKE operator");
                    }
                case "leq": 
                    try{
                        List<String> allowedOps = List.of("id", "quantity", "moneycap", "spent", "budgeted", "available");
                        if (!(allowedOps.contains(col.toLowerCase()))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with LESS THAN OR EQUAL operator");
                        }
                        Integer val = (Integer) value;
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.lessThanOrEqualTo(root.get(col), val);
                        break;
                    } catch (ClassCastException e){
                        System.out.println(e );
                        break;
                    }
                case "geq":
                    try{
                        List<String> allowedOps = List.of("id", "quantity", "moneycap", "spent", "budgeted", "available");
                        if (!(allowedOps.contains(col.toLowerCase()))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with LESS THAN OR EQUAL operator");
                        }
                        Integer val = (Integer) value;
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.greaterThanOrEqualTo(root.get(col), val);
                        break;
                    } catch (ClassCastException e){
                        System.out.println(e );
                        break;
                    }
                case "eq":
                    List<String> notAllowedCols = List.of("name", "type");
                        if (notAllowedCols.contains(col.toLowerCase())){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with EQUAL operator. Pass this with LIKE operator");
                        }
                    condition = (root, query, criteriaBuilder) -> 
                        criteriaBuilder.equal(root.get(col), value);
                    break;
            }
            
            if (condition != null){
                spec = spec.and(condition);
            }

        }
        Iterable<Source> sources =  this.sourceRepository.findAll(spec);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(sources);
    }

    @PostMapping("/create")
    @Operation(
        summary = "Adds a source to the Sources table",
        description = "Takes in a JSON object and adds that source to the Sources table. Returns the object on success"
    )
    /** 
   * DESCRIPTION
   * 
   * @param source a source object to be added to the table
   * @return will return the created source with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public ResponseEntity<?> createSource(@RequestBody Source source){
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.sourceRepository.save(source));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to create source: " + source.toString() );
        }
    }


    @PutMapping("/edit/id={id}")
    @Operation(
        summary = "Edits a source in the Sources table",
        description = "Takes in a JSON object and the id of the source to edit, and edits that Source in the Sources table with the new values provided. Returns the object on success"
    )
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the source to edit
   * @param source the updated source (what you want it to be)
   * @return returns the updated source with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public ResponseEntity<?> editSource(@PathVariable("id") Integer id, @RequestBody Source source){
        Optional<Source> sourceToUpdateOptional = this.sourceRepository.findById(id);
        if (!sourceToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: Invalid source id: " + id.toString() );
        }

        Source sourceToUpdate = sourceToUpdateOptional.get();
        if (source.getName() != null){
            sourceToUpdate.setName(source.getName());
        }
        if (source.getType() != null){
            sourceToUpdate.setType(source.getType());
        }
        if (source.getInternal() != null){
            sourceToUpdate.setInternal(source.getInternal());
        }
        if (source.getMoneyCap() != null){
            sourceToUpdate.setMoneyCap(source.getMoneyCap());
        }
        if (source.getSpent() != null){
            sourceToUpdate.setSpent(source.getSpent());
        }
        if (source.getBudgeted() != null){
            sourceToUpdate.setBudgeted(source.getBudgeted());
        }
        if (source.getAvailable() != null){
            sourceToUpdate.setAvailable(source.getAvailable());
        }
        if (source.getDeleted() != null){
            sourceToUpdate.setDeleted(source.getDeleted());
        }
        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.sourceRepository.save(sourceToUpdate));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update source");
        }

    }

    @PutMapping("/delete/id={id}")
    @Operation(
        summary = "Deletes a source from the Sources table",
        description = "Modifies the deleted column of the source based on the id provided to be 1"
    )
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the source you want to set the deleted flag for
   * @return returns the updated source with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public ResponseEntity<?> deleteSource(@PathVariable("id") Integer id){
        Optional<Source> sourceToDeleteOptional = this.sourceRepository.findById(id);
        if (!sourceToDeleteOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid source id: " + id.toString() );
        }
        Source source = sourceToDeleteOptional.get();
        source.setDeleted(1);

        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.sourceRepository.save(source));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update source");
        } 
    }
}
