package com.devwmu.dc_fin_soft.controllers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import com.devwmu.dc_fin_soft.repositories.SourceRepository;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.devwmu.dc_fin_soft.entities.Source;

import java.util.Optional;

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
   * @param parameter what the parameter is
   * @return what it returns (errors and success)
  */
    @GetMapping("/all")
    @Operation(
        summary = "Retrives all of the sources",
        description = "Takes in no input, and returns all of the rows in the Sources table"
    )
    // @ApiResponses(value = {
    //     @ApiResponse(responseCode = "201", description = "Book successfully created"),
    //     @ApiResponse(responseCode = "400", description = "Invalid input supplied")
    // })
    public Iterable<Source> getAllSources() {   
        //      OUTPUT: all of the sources

        return this.sourceRepository.findAll();
    }

    @PutMapping("/search")
    @Operation(
        summary = "Filters through the sources based on specified values",
        description = "Takes in a JSON array, where each element is a Filter object consisting of the column to filter by, the operation to filter based on, and the desired value, and returns all of the rows in the Sources table which match the Filter objects"
    )
    public Iterable<Source> filterSources(@RequestBody Filter[] filters) {
        // custom
        // filterSources(filterArray[]) Iterable<Source>
        //      INPUT: filters: Filter[] -  an array of filters to apply to the table
        //      ex) [{"col":"est_attendance", "op":"geq", "val":16}] - this will apply a filter for if the estimated attendance is >= 16
        //     OUTPUT: the selected rows of the sources table

        // returns the events that match
        Specification<Source> spec = Specification.unrestricted();
        for (Filter filter: filters){
            String col = filter.getCol();
            String op = filter.getOp().toLowerCase();
            Object value = filter.getVal();

            if (value == null){
                continue;
            }

            Specification<Source> condition = null;
            switch (op) {
                case "like":
                    try{
                        String lower = "%" + value.toString().toLowerCase() + "%";
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.like(criteraBuilder.lower(root.get(col)), lower);
                        break;
                    }
                    catch (ClassCastException e){
                        System.out.println(e + "\n\n\n");
                        break;
                    }
                case "leq": 
                    try{
                        Integer val = (Integer) value;
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.lessThanOrEqualTo(root.get(col), val);
                        break;
                    } catch (ClassCastException e){
                        System.out.println(e + "\n\n\n");
                        break;
                    }
                case "geq":
                    try{
                        Integer val = (Integer) value;
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.greaterThanOrEqualTo(root.get(col), val);
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
            
            if (condition != null){
                spec = spec.and(condition);
            }

        }
        return this.sourceRepository.findAll(spec);
    }

    @PostMapping("/create")
    @Operation(
        summary = "Adds a source to the Sources table",
        description = "Takes in a JSON object and adds that Request to the Sources table. Returns the object on success"
    )
    public Source createSource(@RequestBody Source source){
        return this.sourceRepository.save(source);
        // createSource(name, cap, type, internal): Source
        //      Adds a source to the source database
        //     INPUT: source: Source - the source to be added to the source database
        //     OUTPUT: created source
    }

    @PutMapping("/edit/id={id}")
    @Operation(
        summary = "Edits a source in the Sources table",
        description = "Takes in a JSON object and the id of the event to edit, and edits that Source in the Sources table with the new values provided. Returns the object on success"
    )
    public Source editSource(@PathVariable("id") Integer id, @RequestBody Source source){
        // editSource((id, editArray[]): bool
        //     Edits columns of a source
        //      INPUT: id: Integer - the id of the souce to be updated, source: Source - the souce to be updated
        //     OUTPUT: edited source
        Optional<Source> sourceToUpdateOptional = this.sourceRepository.findById(id);
        if (!sourceToUpdateOptional.isPresent()){
            return null;
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
        
        return this.sourceRepository.save(sourceToUpdate);

    }

    @PutMapping("/delete/id={id}")
    @Operation(
        summary = "Deletes a source from the Sources table",
        description = "Modifies the deleted column of the source based on the id provided to be 1"
    )
    public Source deleteSource(@PathVariable("id") Integer id){
        // deleteSource(sourceID): Source
        //     Deletes a source from the database
        //     INPUT: id: Integer - the id of the source to be deleted
        //     OUTPUT: deleted source

        Optional<Source> sourceToDeleteOptional = this.sourceRepository.findById(id);
        if (!sourceToDeleteOptional.isPresent()){
            return null;
        }
        Source source = sourceToDeleteOptional.get();
        source.setDeleted(1);

        
        return this.sourceRepository.save(source);
    }
}
