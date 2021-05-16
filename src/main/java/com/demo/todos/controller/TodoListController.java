package com.demo.todos.controller;

import com.demo.todos.model.request.TodoListInsertRequest;
import com.demo.todos.model.response.CommonResponse;
import com.demo.todos.service.TodoListService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TodoListController {

    private static final Logger logger = LogManager.getLogger(TodoListController.class);

    @Autowired
    private TodoListService todoListService;

    @PostMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> insertTodoList(@RequestBody TodoListInsertRequest request){
        logger.info("START IMPLEMENTING INSERT TODOLIST, message : {}", request.getMessage());
       CommonResponse  response = todoListService.insertTodoList(request);
        logger.info("END IMPLEMENTING INSERT TODOLIST, response : {}", response);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PutMapping (value ="/{messageId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateTodoList (@PathVariable("messageId") String messageId, @RequestBody TodoListInsertRequest request){
        logger.info("START IMPLEMENTING UPDATE TODOLIST, message : {}", request.getMessage());
        CommonResponse response = todoListService.updateTodoList(request, messageId);
        logger.info("END IMPLEMENTING INSERT TODOLIST, response : {}", response);
        return new ResponseEntity<>(response,response.getHttpStatus());
    }

    @GetMapping(value =" ", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> listTodoList (){
        logger.info("START IMPLEMENTING RETRIEVE TODOLIST");
        CommonResponse response = todoListService.listTodoList();
        logger.info("END IMPLEMENTING RETRIEVE TODOLIST, response : {}",response);
        return new ResponseEntity<>(response,response.getHttpStatus());
    }
}
