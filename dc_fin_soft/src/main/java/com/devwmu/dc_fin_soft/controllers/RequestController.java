package com.devwmu.dc_fin_soft.controllers;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.devwmu.dc_fin_soft.entities.Request;
import com.devwmu.dc_fin_soft.repositories.RequestRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;


@RestController
@RequestMapping("/requests")
@Tag(name = "Requests", description = "This controller interacts with the requests table in various ways")
public class RequestController {
    private final RequestRepository requestRepository;

    public RequestController(final RequestRepository requestRepository) {
    this.requestRepository = requestRepository;
  }

    @GetMapping("/all")
    @Operation(
        summary = "Retrives all of the requests",
        description = "Takes in no input, and returns all of the rows in the Requests table"
    )
    /** 
   * DESCRIPTION
   * 
   * 
   * @return returns all of the rows of the requests table with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public ResponseEntity<?> getAllRequests() {  
        //      INPUT: N/A 
        //      OUTPUT: all of the requests
        // returns all of the rows in the requests table
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.requestRepository.findAll());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to retrieve all requests");
        }
    }
    @PutMapping("/search")
    @Operation(
        summary = "Filters through the requests based on specified values",
        description = "Takes in a JSON array, where each element is a Filter object consisting of the column to filter by, the operation to filter based on, and the desired value, and returns all of the rows in the Requests table which match the Filter objects"
    )
    /** 
   * DESCRIPTION
   * 
   * @param filters an array of filter objects, which represents the columns, operations, and values to filter by
   * @return the rows of requests that match the filters with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error. If no filters are applied, returns all of the rows. 
  */
    public ResponseEntity<?> filterRequests(@RequestBody Filter[] filters){
        // filterRequests(Filter[]) Iterable<Request>
        //     INPUT: filters: Filter[] -  an array of filters to apply to the table
        //      ex) [{"col":"est_attendance", "op":"geq", "val":16}] - this will apply a filter for if the estimated attendance is >= 16
        //     OUTPUT: the selected rows of the requests table

        // returns the requests that match

        Specification<Request> spec = Specification.unrestricted();
        for (Filter filter: filters){
            // iterates through all of the filters in the given array
            String col = filter.getCol();
            String op = filter.getOp().toLowerCase();
            Object value = filter.getVal();

            if (value == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: No value provided for filter on column: " + col + "\n");
            }

            Specification<Request> condition = null;
            // goes through all of the operators for this table
            switch (op) {
                case "like":
                    try{
                        List<String> allowedCols = List.of("communityname","requesteeuser", "itemname", "purpose");
                        if (!(allowedCols.contains(col.toLowerCase()))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with LIKE operator\n");
                        }
                        String lower = "%" + value.toString().toLowerCase() + "%";
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.like(criteraBuilder.lower(root.get(col)), lower);
                        break;
                    }
                    catch (ClassCastException e){
                        System.out.println(e + "\n\n\n");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: non-string value passed with LIKE operator\n");
                    }
                case "bw":
                    try {
                        List<String> allowedCols = List.of("deadline");
                        if (!(allowedCols.contains(col.toLowerCase()))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with BETWEEN operator\n");
                        }
                        ArrayList<String> value2 = (ArrayList<String>) value;
                        LocalDateTime date1 = LocalDateTime.parse(value2.get(0));
                        LocalDateTime date2 = LocalDateTime.parse(value2.get(1));
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.between(root.get(col), date1, date2);

                        break;
                    } catch (ClassCastException e){
                        System.out.println(e + "\n\n\n");
                        break;
                    }
                case "leq": 
                    try{
                        List<String> allowedOps = List.of("id", "quantity", "priceperunit");
                        if (!(allowedOps.contains(col.toLowerCase()))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with LESS THAN OR EQUAL operator\n");
                        }
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
                        List<String> allowedOps = List.of("id", "quantity", "priceperunit");
                        if (!(allowedOps.contains(col.toLowerCase()))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with GREATER THAN OR EQUAL operator\n");
                        }
                        Integer val = (Integer) value;
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.greaterThanOrEqualTo(root.get(col), val);
                        break;
                    } catch (ClassCastException e){
                        System.out.println(e + "\n\n\n");
                        break;
                    }
                case "eq":
                    List<String> notAllowedCols = List.of("communityname","requesteeuser", "itemname", "purpose");
                        if (notAllowedCols.contains(col.toLowerCase())){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with EQUAL operator. Pass this with LIKE operator\n");
                        }
                    condition = (root, query, criteriaBuilder) -> 
                        criteriaBuilder.equal(root.get(col), value);
                    break;
            }
            // combines all of the conditions to layer them
            if (condition != null){
                spec = spec.and(condition);
            }

        }
        Iterable<Request> requests =  this.requestRepository.findAll(spec);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requests);
    }

    @PostMapping("/create")
    @Operation(
        summary = "Adds a request to the Requests table",
        description = "Takes in a JSON object and adds that Request to the Requests table. Returns the object on success"
    )
    /** 
   * DESCRIPTION
   * 
   * @param request an request object to be added to the table
   * @return will return the created request with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public ResponseEntity<?> createRequest(@RequestBody Request request){
        // createRequest(name, community, username, itemName, quantity, pricePerUnit, deadline, purpose): bool
        //     Creates a new entry in the club requests table
        //     OUTPUT: created request
        // saves the new request to the database
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.requestRepository.save(request));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to create request: " + request.toString() + "\n");
        }
    }

    @PutMapping("/edit/id={id}")
    @Operation(
        summary = "Edits a request in the Requests table",
        description = "Takes in a JSON object and the id of the request to edit, and edits that Request in the Requests table with the new values provided. Returns the object on success"
    )
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the request to edit
   * @param request the updated request (what you want it to be)
   * @return returns the updated request with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public ResponseEntity<?> editRequest(@PathVariable("id") Integer id, @RequestBody Request request){
        // editRequest(id, request): Request
        //     INPUT: id: Integer - The id of the request, request: Request - the updated request
        //     OUTPUT: the updated request


        // looks to see if the given request object has its columns set, and it it is set, then updates the new request with those values
        Optional<Request> requestToUpdateOptional = this.requestRepository.findById(id);
        if (!requestToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: Invalid request id: " + id.toString() + "\n");
        }
        Request newRequest = requestToUpdateOptional.get();

        if (request.getCommunityName() != null){
            newRequest.setCommunityName(request.getCommunityName());
        }
        if (request.getRequesteeUser() != null){
            newRequest.setRequesteeUser(request.getRequesteeUser());
        }
        if (request.getItemName() != null){
            newRequest.setItemName(request.getItemName());
        }
        if (request.getApproval() != null){
            newRequest.setApproval(request.getApproval());
        }
        if (request.getQuantity() != null){
            newRequest.setQuantity(request.getQuantity());
        }
        if (request.getPricePerUnit() != null){
            newRequest.setPricePerUnit(request.getPricePerUnit());
        }
        if (request.getDeadline() != null){
            newRequest.setDeadline(request.getDeadline());
        }
        if (request.getPurpose() != null){
            newRequest.setPurpose(request.getPurpose());
        }
        if (request.getDeleted() != null){
            newRequest.setDeleted(request.getDeleted());
        }

        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.requestRepository.save(newRequest));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update request");
        }
    }

    @PutMapping("/delete/id={id}")
    @Operation(
        summary = "Deletes an request from the Requests table",
        description = "Modifies the deleted column of the request based on the id provided to be 1"
    )
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the request you want to set the deleted flag for
   * @return returns the updated request with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public ResponseEntity<?> deleteRequest(@PathVariable("id") Integer id){

        // sets the deleted flag to be 1
        Optional<Request> requestToUpdateOptional = this.requestRepository.findById(id);
        if (!requestToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid request id: " + id.toString() + "\n");
        }
        Request deleteRequest = requestToUpdateOptional.get();
        deleteRequest.setDeleted(1);
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.requestRepository.save(deleteRequest));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update request");
        } 
    }


    @PutMapping("/approve/id={id}_val={val}")
    @Operation(
        summary = "Toggles the approved flag for an request",
        description = "Using the id provided, it will toggle the approved flag for an request to either 1 or 0"
    )
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the request you want to set the approve flag for
   * @param value the value that you want the flag to be set to
   * @return returns the updated request with a 200 response code on success. On error, the appropriate error code will be set with text body explaining the error
  */
    public ResponseEntity<?> approveRequest(@PathVariable("id") Integer id, @PathVariable("val") Integer value){
        // approves a request by setting it to 1 or 0 based on the value provided
        Optional<Request> requestToUpdateOptional = this.requestRepository.findById(id);
        if (!requestToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid request id: " + id.toString() + "\n");
        }
        Request approveRequest = requestToUpdateOptional.get();
        if (value == 1){
            approveRequest.setApproval(1);
        }
        else{
            approveRequest.setApproval(0);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.requestRepository.save(approveRequest));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update request");
        } 
    }

    @PostMapping("/new_request")
    public Request newRequestNotify(){
        // custom
        // newRequestNotify(RequestID, email(s)): bool
        //     Sends a notification to the admin of the dev club about a new request
        //     OUTPUT: success or not

        // do a search of the admins of the database, and email them that a new request has been made
        return new Request();
    }

    @PostMapping("/request_status_updated")
    public Request requestStatusUpdatedNotify(){
        // custom
        // requestStatusUpdatedNotify(requestID, update): bool
        //     Updates the requestee on the request that there has been a change to their request (and what the change is)
        //     OUTPUT: success or not

        // when admin changes approves/disapproves of a request, it will call this funcion
        // take in the id of the request, extract who made the request, and send them an email
        return new Request();
    }
}
