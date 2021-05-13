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
import java.util.List;
import java.util.UUID;

import static com.demo.todos.constant.CommonConstant.ACTIVATED_MESSAGE;

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

    public CommonResponse updateTodoList(TodoListInsertRequest request, String messageIdStr) {
        UUID messageId = UUID.fromString(messageIdStr);
        TodoTransactionEntity todoTransactionEntity = todoTransactionRepository.findAllByIdAndActivated(messageId, ACTIVATED_MESSAGE);
        CommonResponse response = new CommonResponse();
        if (todoTransactionEntity!= null){
            TodoTransactionEntity entity = new TodoTransactionEntity();
            entity.setId(todoTransactionEntity.getId());
            entity.setMessage(request.getMessage());
            entity.setCreatedDate(todoTransactionEntity.getCreatedDate());
            entity.setUpdatedDate(Calendar.getInstance().getTime());
            entity.setActivated(todoTransactionEntity.getActivated());
            todoTransactionRepository.save(entity);

           response.setStatus("SUCCESS");
            TodoListInsertResponse todoListInsertResponse = new TodoListInsertResponse();
            todoListInsertResponse.setMessage(request.getMessage());
            todoListInsertResponse.setMessageId(messageIdStr);
            response.setData(todoListInsertResponse);

        }else{
            response.setStatus("NOT FOUND");
            response.setData("transaction todo list not found");
        }
        return response;
    }
}
