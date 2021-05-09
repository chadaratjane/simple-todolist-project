package com.demo.todos.service;

import com.demo.todos.model.entity.TodoTransactionEntity;
import com.demo.todos.model.request.TodoListInsertRequest;
import com.demo.todos.model.response.CommonResponse;
import com.demo.todos.model.response.TodoListInsertResponse;
import com.demo.todos.repository.TodoTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class TodoListService {

    @Autowired
    private TodoTransactionRepository todoTransactionRepository;

    public CommonResponse insertTodoList(TodoListInsertRequest request){
     TodoTransactionEntity entity = new TodoTransactionEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setMessage(request.getMessage());
        Date date = Calendar.getInstance().getTime();
        entity.setCreatedDate(date);
        entity.setUpdatedDate(date);
        entity.setActivated("Y");
        todoTransactionRepository.save(entity);

        CommonResponse response = new CommonResponse();
        response.setStatus("SUCCESS");
        TodoListInsertResponse todoListInsertResponse = new TodoListInsertResponse();
        todoListInsertResponse.setMessage(request.getMessage());
        todoListInsertResponse.setMessageId(id.toString());
        response.setData(todoListInsertResponse);
        return response;
    }

}
