package com.demo.todos.service;

import com.demo.todos.controller.TodoListController;
import com.demo.todos.model.entity.TodoTransactionEntity;
import com.demo.todos.model.request.TodoListInsertRequest;
import com.demo.todos.model.response.CommonResponse;
import com.demo.todos.model.response.ErrorResponse;
import com.demo.todos.model.response.TodoListInsertResponse;
import com.demo.todos.repository.TodoTransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.demo.todos.constant.CommonConstant.*;

@Service
public class TodoListService {

    private static final Logger logger = LogManager.getLogger(TodoListService.class);

    @Autowired
    private TodoTransactionRepository todoTransactionRepository;

    public CommonResponse insertTodoList(TodoListInsertRequest request) {
        TodoTransactionEntity todoTransactionEntity = new TodoTransactionEntity();

        TodoTransactionEntity entity = new TodoTransactionEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setMessage(request.getMessage());
        Date date = Calendar.getInstance().getTime();
        entity.setCreatedDate(date);
        entity.setUpdatedDate(date);
        entity.setActivated("Y");

        TodoTransactionEntity saveResult = todoTransactionRepository.save(entity);
        CommonResponse response = new CommonResponse();

        if (saveResult != null) {
            logger.info("INSERT SUCCESSFULLY");
            response.setStatus(SUCCESS_CODE);
            TodoListInsertResponse todoListInsertResponse = new TodoListInsertResponse();
            todoListInsertResponse.setMessage(request.getMessage());
            todoListInsertResponse.setMessageId(id.toString());
            response.setData(todoListInsertResponse);
            response.setHttpStatus(HttpStatus.CREATED);
        } else {
            logger.error("INSERT UNSUCCESSFULLY");
            response.setStatus("ERROR");
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError("INSERT UNSUCCESSFULLY");
            response.setData(errorResponse);
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public CommonResponse updateTodoList(TodoListInsertRequest request, String messageIdStr) {
        UUID messageId = UUID.fromString(messageIdStr);
        TodoTransactionEntity todoTransactionEntity = todoTransactionRepository.findAllByIdAndActivated(messageId, ACTIVATED_MESSAGE);
        CommonResponse response = new CommonResponse();

        if (todoTransactionEntity!= null){
            logger.info("TRANSACTION FOUND");

            TodoTransactionEntity entity = new TodoTransactionEntity();
            entity.setId(todoTransactionEntity.getId());
            entity.setMessage(request.getMessage());
            entity.setCreatedDate(todoTransactionEntity.getCreatedDate());
            entity.setUpdatedDate(Calendar.getInstance().getTime());
            entity.setActivated(todoTransactionEntity.getActivated());
            TodoTransactionEntity saveResult = todoTransactionRepository.save(entity);

            if(saveResult!= null) {


                logger.info("UPDATE SUCCESSFULLY");
                response.setStatus(SUCCESS_CODE);
                TodoListInsertResponse todoListInsertResponse = new TodoListInsertResponse();
                todoListInsertResponse.setMessage(request.getMessage());
                todoListInsertResponse.setMessageId(messageIdStr);
                response.setData(todoListInsertResponse);
                response.setHttpStatus(HttpStatus.OK);
            }else {
                logger.info("UPDATE UNSUCCESSFULLY");
                response.setStatus("ERROR");
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setError("UPDATE UNSUCCESSFULLY");
                response.setData(errorResponse);
                response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);

            }

        }else{
            logger.error("TRANSACTION NOT FOUND messageId :{}" ,messageIdStr);
            response.setStatus("NOT FOUND");
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError("TRANSACTION NOT FOUND");
            response.setData(errorResponse);
            response.setHttpStatus(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    public CommonResponse listTodoList () {
        List<TodoTransactionEntity> todoTransactionEntityList = todoTransactionRepository.findAllByActivated(ACTIVATED_MESSAGE);
        CommonResponse response = new CommonResponse();
        response.setStatus(SUCCESS_CODE);
        response.setHttpStatus(HttpStatus.OK);
        ArrayList<TodoListInsertResponse> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(todoTransactionEntityList)) {
            logger.info("NO DATA TO RETRIEVE");
        } else {
            logger.info("RETRIEVE SUCCESSFULLY");
            for (TodoTransactionEntity tran : todoTransactionEntityList) {
                TodoListInsertResponse item = new TodoListInsertResponse();
                item.setMessageId(tran.getId().toString());
                item.setMessage(tran.getMessage());
                list.add(item);
            }
            response.setData(list);
        }
        return response;
    }

    public CommonResponse removeTodoList(String messageIdStr){
        UUID messageId = UUID.fromString(messageIdStr);
        TodoTransactionEntity todoTransactionEntity = todoTransactionRepository.findAllByIdAndActivated(messageId, ACTIVATED_MESSAGE);
        CommonResponse response = new CommonResponse();

        if (todoTransactionEntity!= null){
            logger.info("TRANSACTION FOUND");

            TodoTransactionEntity entity = new TodoTransactionEntity();
            entity.setId(todoTransactionEntity.getId());
            entity.setMessage(todoTransactionEntity.getMessage());
            entity.setCreatedDate(todoTransactionEntity.getCreatedDate());
            entity.setUpdatedDate(Calendar.getInstance().getTime());
            entity.setActivated(INACTIVATED_MESSAGE);
            TodoTransactionEntity saveResult = todoTransactionRepository.save(entity);

            if(saveResult!= null) {


                logger.info("REMOVE SUCCESSFULLY");
                response.setStatus(SUCCESS_CODE);
                TodoListInsertResponse todoListInsertResponse = new TodoListInsertResponse();
                todoListInsertResponse.setMessage(saveResult.getMessage());
                todoListInsertResponse.setMessageId(messageIdStr);
                response.setData(todoListInsertResponse);
                response.setHttpStatus(HttpStatus.OK);
            }else {
                logger.info("REMOVE UNSUCCESSFULLY");
                response.setStatus("ERROR");
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setError("REMOVE UNSUCCESSFULLY");
                response.setData(errorResponse);
                response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);

            }

        } else {
            logger.error("TRANSACTION NOT FOUND messageId :{}", messageIdStr);
            response.setStatus("NOT FOUND");
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError("TRANSACTION NOT FOUND");
            response.setData(errorResponse);
            response.setHttpStatus(HttpStatus.NOT_FOUND);
        }
        return response;
    }

}

