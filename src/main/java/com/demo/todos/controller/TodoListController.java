package com.demo.todos.controller;

import com.demo.todos.model.request.TodoListInsertRequest;
import com.demo.todos.model.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoListController {

    @PostMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insertTodoList(@RequestBody TodoListInsertRequest request){
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus("SUCCESS");
        commonResponse.setData(null);
        return new ResponseEntity<>(commonResponse, HttpStatus.CREATED);
    }
}
