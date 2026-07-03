package com.devwmu.dc_fin_soft.controllers;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import com.devwmu.dc_fin_soft.entities.Request;
import com.devwmu.dc_fin_soft.repositories.RequestRepository;

import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

// Fix outputs and inputs

@RestController
@RequestMapping("/requests")
public class RequestController {
    private final RequestRepository requestRepository;

    public RequestController(final RequestRepository requestRepository) {
    this.requestRepository = requestRepository;
  }

    @GetMapping("/requests")
    @Operation(
        summary = "Retrives all of the requests",
        description = "Takes in no input, and returns all of the rows in the Requests table"
    )
    public Iterable<Request> getAllRequests() {  
        //      INPUT: N/A 
        //      OUTPUT: all of the requests
        // returns all of the rows in the requests table
        return this.requestRepository.findAll();
    }
    @PutMapping("/requests/search")
    @Operation(
        summary = "Filters through the requests based on specified values",
        description = "Takes in a JSON array, where each element is a Filter object consisting of the column to filter by, the operation to filter based on, and the desired value, and returns all of the rows in the Requests table which match the Filter objects"
    )
    public Iterable<Request> filterRequests(@RequestBody Filter[] filters){
        // filterRequests(Filter[]) Iterable<Request>
        //     INPUT: filters: Filter[] -  an array of filters to apply to the table
        //      ex) [{"col":"est_attendance", "op":"geq", "val":16}] - this will apply a filter for if the estimated attendance is >= 16
        //     OUTPUT: the selected rows of the requests table

        // returns the events that match

        Specification<Request> spec = Specification.unrestricted();
        for (Filter filter: filters){
            // iterates through all of the filters in the given array
            String col = filter.getCol();
            String op = filter.getOp().toLowerCase();
            Object value = filter.getVal();

            if (value == null){
                continue;
            }

            Specification<Request> condition = null;
            // goes through all of the operators for this table
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
                case "bw":
                    try {
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
            // combines all of the conditions to layer them
            if (condition != null){
                spec = spec.and(condition);
            }

        }
        return this.requestRepository.findAll(spec);
    }

    @PostMapping("/request")
    @Operation(
        summary = "Adds a request to the Requests table",
        description = "Takes in a JSON object and adds that Request to the Requests table. Returns the object on success"
    )
    public Request createRequest(@RequestBody Request request){
        // createRequest(name, community, username, itemName, quantity, pricePerUnit, deadline, purpose): bool
        //     Creates a new entry in the club requests table
        //     OUTPUT: created request
        // saves the new request to the database
        return this.requestRepository.save(request);
    }

    @PutMapping("/request/edit_{id}")
    @Operation(
        summary = "Edits a request in the Requests table",
        description = "Takes in a JSON object and the id of the event to edit, and edits that Request in the Requests table with the new values provided. Returns the object on success"
    )
    public Request editRequest(@PathVariable("id") Integer id, @RequestBody Request request){
        // editRequest(id, request): Request
        //     INPUT: id: Integer - The id of the request, request: Request - the updated request
        //     OUTPUT: the updated request


        // looks to see if the given request object has its columns set, and it it is set, then updates the new request with those values
        Optional<Request> requestToUpdateOptional = this.requestRepository.findById(id);
        if (!requestToUpdateOptional.isPresent()){
            return null;
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

        return this.requestRepository.save(newRequest);
    }

    @PutMapping("/request/delete_{id}")
    @Operation(
        summary = "Deletes an event from the Requests table",
        description = "Modifies the deleted column of the request based on the id provided to be 1"
    )
    public Request deleteRequest(@PathVariable("id") Integer id){
        // deleteRequest(id): bool
        //     The id of the request to be deleted (will just set deleted to 1)
        //      INPUT: id: Integer - the id of the request to be deleted
        //     OUTPUT: the deleted request

        // sets the deleted flag to be 1
        Optional<Request> requestToUpdateOptional = this.requestRepository.findById(id);
        if (!requestToUpdateOptional.isPresent()){
            return null;
        }
        Request deleteRequest = requestToUpdateOptional.get();
        deleteRequest.setDeleted(1);
        return this.requestRepository.save(deleteRequest);
    }


    @PutMapping("/request/approve_id={id}_val={val}")
    @Operation(
        summary = "Toggles the approved flag for an event",
        description = "Using the id provided, it will toggle the approved flag for an expense to either 1 or 0"
    )
    public Request approveRequest(@PathVariable("id") Integer id, @PathVariable("val") Integer value){
        // approveRequest(id, decision) bool: 
        //     will mark a request as approved/disapproved in the club requests table
        //     INPUT: id: Integer - The id of the item to change the conference flag (from database), val: Integer -  what to set the flag to
        //     OUTPUT: the updated Request
        // approves a request by setting it to 1 or 0 based on the value provided
        Optional<Request> requestToUpdateOptional = this.requestRepository.findById(id);
        if (!requestToUpdateOptional.isPresent()){
            return null;
        }
        Request approveRequest = requestToUpdateOptional.get();
        if (value == 1){
            approveRequest.setApproval(1);
        }
        else{
            approveRequest.setApproval(0);
        }
        return this.requestRepository.save(approveRequest);
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
