package com.demo.todos.controller;

import com.demo.todos.model.request.TodoListInsertRequest;
import com.demo.todos.model.response.CommonResponse;
import com.demo.todos.service.TodoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TodoListController {

    @Autowired
    private TodoListService todoListService;

    @PostMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insertTodoList(@RequestBody TodoListInsertRequest request){
       CommonResponse response = todoListService.insertTodoList(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping (value ="/{messageId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateTodoList (@PathVariable("messageId") String messageId, @RequestBody TodoListInsertRequest request){
        CommonResponse response = todoListService.updateTodoList(request, messageId);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

}
