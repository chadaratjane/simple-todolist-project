package com.demo.todos.service;

import com.demo.todos.model.entity.TodoTransactionEntity;
import com.demo.todos.model.request.TodoListInsertRequest;
import com.demo.todos.model.response.CommonResponse;
import com.demo.todos.model.response.ErrorResponse;
import com.demo.todos.model.response.TodoListInsertResponse;
import com.demo.todos.repository.TodoTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.demo.todos.constant.CommonConstant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TodoListServiceTest {

    @InjectMocks
    private TodoListService todoListService;

    @Mock
    private TodoTransactionRepository todoTransactionRepository;

    @Test
    public void success_insertTodoList() {
        TodoTransactionEntity todoTransactionEntity = new TodoTransactionEntity();
        UUID resultUUID = UUID.randomUUID();
        todoTransactionEntity.setId(resultUUID);
        todoTransactionEntity.setActivated(ACTIVATED_MESSAGE);
        todoTransactionEntity.setMessage("Mock Message");
        Date date = Calendar.getInstance().getTime();
        todoTransactionEntity.setCreatedDate(date);
        todoTransactionEntity.setUpdatedDate(date);

        Mockito.when(todoTransactionRepository.save(any())).thenReturn(todoTransactionEntity);

        TodoListInsertRequest todoListInsertRequest = new TodoListInsertRequest();
        todoListInsertRequest.setMessage("Mock Message");
        CommonResponse commonResponse = todoListService.insertTodoList(todoListInsertRequest);

        TodoListInsertResponse todoListInsertResponse = (TodoListInsertResponse) commonResponse.getData();

        assertEquals(resultUUID.toString(),todoListInsertResponse.getMessageId());
        assertEquals("Mock Message", todoListInsertResponse.getMessage());

    }

    @Test
    public void fail_insertTodoList(){
        TodoListInsertRequest todoListInsertRequest = new TodoListInsertRequest();
        todoListInsertRequest.setMessage("Mock Message");
        CommonResponse commonResponse = todoListService.insertTodoList(todoListInsertRequest);

        ErrorResponse errorResponse = (ErrorResponse) commonResponse.getData();

        assertEquals("ERROR",commonResponse.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, commonResponse.getHttpStatus());
        assertEquals("INSERT UNSUCCESSFULLY",errorResponse.getError());

    }

    @Test
    public void success_updateTodoList(){
        TodoTransactionEntity todoTransactionEntity = new TodoTransactionEntity();

        TodoTransactionEntity updatedTransactionEntity = new TodoTransactionEntity();

        UUID resultID = UUID.randomUUID();
        todoTransactionEntity.setId(resultID);
        todoTransactionEntity.setMessage("Mock Message");
        Mockito.when(todoTransactionRepository.findAllByIdAndActivated(resultID,ACTIVATED_MESSAGE)).thenReturn(todoTransactionEntity);

        updatedTransactionEntity.setId(resultID);
        updatedTransactionEntity.setActivated(ACTIVATED_MESSAGE);
        updatedTransactionEntity.setMessage("Mock Message updated");
        updatedTransactionEntity.setCreatedDate(todoTransactionEntity.getCreatedDate());
        updatedTransactionEntity.setUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(todoTransactionRepository.save(any())).thenReturn(updatedTransactionEntity);

        TodoListInsertRequest todoListInsertRequest = new TodoListInsertRequest();
        todoListInsertRequest.setMessage("Mock Message updated");

        CommonResponse commonResponse= todoListService.updateTodoList(todoListInsertRequest,resultID.toString());

        TodoListInsertResponse todoListInsertResponse = (TodoListInsertResponse) commonResponse.getData();

        assertEquals(resultID.toString(),todoListInsertResponse.getMessageId());
        assertEquals("Mock Message updated",todoListInsertResponse.getMessage());
        assertEquals(HttpStatus.OK,commonResponse.getHttpStatus());
        assertEquals("SUCCESS",commonResponse.getStatus());
    }

    @Test
    public void fail_updateTodoList_updateTransaction(){
        TodoTransactionEntity todoTransactionEntity = new TodoTransactionEntity();

        UUID resultID = UUID.randomUUID();
        todoTransactionEntity.setId(resultID);
        todoTransactionEntity.setMessage("Mock Message");
        Mockito.when(todoTransactionRepository.findAllByIdAndActivated(resultID,ACTIVATED_MESSAGE)).thenReturn(todoTransactionEntity);

        Mockito.when(todoTransactionRepository.save(any())).thenReturn(null);

        TodoListInsertRequest todoListInsertRequest = new TodoListInsertRequest();
        todoListInsertRequest.setMessage("Mock Message Updated");

        CommonResponse commonResponse = todoListService.updateTodoList(todoListInsertRequest, resultID.toString());

        ErrorResponse errorResponse = (ErrorResponse) commonResponse.getData();

        assertEquals("ERROR", commonResponse.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, commonResponse.getHttpStatus());
        assertEquals("UPDATE UNSUCCESSFULLY", errorResponse.getError());

    }

    @Test
    public void fail_updateTodoList_notFoundTransaction(){

        UUID resultID = UUID.randomUUID();

        Mockito.when(todoTransactionRepository.findAllByIdAndActivated(resultID,ACTIVATED_MESSAGE)).thenReturn(null);

        TodoListInsertRequest todoListInsertRequest = new TodoListInsertRequest();
        todoListInsertRequest.setMessage("Mock Message Updated");

        CommonResponse commonResponse = todoListService.updateTodoList(todoListInsertRequest,resultID.toString());

        ErrorResponse errorResponse = (ErrorResponse) commonResponse.getData();

        assertEquals("NOT FOUND", commonResponse.getStatus());
        assertEquals(HttpStatus.NOT_FOUND, commonResponse.getHttpStatus());
        assertEquals("TRANSACTION NOT FOUND", errorResponse.getError());
    }

    @Test
    public void success_listTodoList(){

        TodoTransactionEntity todoTransactionEntity1 = new TodoTransactionEntity();
        todoTransactionEntity1.setId(UUID.randomUUID());
        todoTransactionEntity1.setMessage("Mock Message1");
        todoTransactionEntity1.setActivated(ACTIVATED_MESSAGE);
        Date date = Calendar.getInstance().getTime();
        todoTransactionEntity1.setCreatedDate(date);
        todoTransactionEntity1.setUpdatedDate(date);

        TodoTransactionEntity todoTransactionEntity2 = new TodoTransactionEntity();
        todoTransactionEntity2.setId(UUID.randomUUID());
        todoTransactionEntity2.setMessage("Mock Message2");
        todoTransactionEntity2.setActivated(ACTIVATED_MESSAGE);
        date = Calendar.getInstance().getTime();
        todoTransactionEntity2.setCreatedDate(date);
        todoTransactionEntity2.setUpdatedDate(date);

        List<TodoTransactionEntity> expectedResult = new ArrayList<>();
        expectedResult.add(todoTransactionEntity1);
        expectedResult.add(todoTransactionEntity2);

        Mockito.when(todoTransactionRepository.findAllByActivated(ACTIVATED_MESSAGE)).thenReturn(expectedResult);

        CommonResponse commonResponse = todoListService.listTodoList();

        List<TodoListInsertResponse> todoListInsertResponseList = (List<TodoListInsertResponse>) commonResponse.getData();

        assertEquals("SUCCESS",commonResponse.getStatus());
        assertEquals(HttpStatus.OK,commonResponse.getHttpStatus());

        assertEquals(expectedResult.size(),todoListInsertResponseList.size());

        for (int i =0 ;i <expectedResult.size();i++){

            assertEquals(expectedResult.get(i).getId().toString(),todoListInsertResponseList.get(i).getMessageId());
            assertEquals(expectedResult.get(i).getMessage(),todoListInsertResponseList.get(i).getMessage());
        }

    }

    @Test
    public void fail_listTodoList(){

        List<TodoTransactionEntity> saveResult = null;

        Mockito.when(todoTransactionRepository.findAllByActivated(ACTIVATED_MESSAGE)).thenReturn(null);

        CommonResponse commonResponse = todoListService.listTodoList();
        List<TodoListInsertResponse> todoListInsertResponseList = (List<TodoListInsertResponse>) commonResponse.getData();

        assertEquals(saveResult,todoListInsertResponseList);

    }

    @Test
    public void success_removeTodoList(){
        TodoTransactionEntity todoTransactionEntity = new TodoTransactionEntity();
        UUID resultUUID = UUID.randomUUID();
        todoTransactionEntity.setId(resultUUID);
        todoTransactionEntity.setMessage("Mock Message");
        todoTransactionEntity.setActivated(ACTIVATED_MESSAGE);
        Date date = Calendar.getInstance().getTime();
        todoTransactionEntity.setCreatedDate(date);
        todoTransactionEntity.setUpdatedDate(date);

        Mockito.when(todoTransactionRepository.findAllByIdAndActivated(resultUUID,ACTIVATED_MESSAGE)).thenReturn(todoTransactionEntity);

        TodoTransactionEntity removeTransactionEntity = new TodoTransactionEntity();
        removeTransactionEntity.setId(todoTransactionEntity.getId());
        removeTransactionEntity.setActivated(INACTIVATED_MESSAGE);
        removeTransactionEntity.setUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(todoTransactionRepository.save(any())).thenReturn(removeTransactionEntity);

        CommonResponse commonResponse = todoListService.removeTodoList(resultUUID.toString());

        TodoListInsertResponse todoListInsertResponse =  (TodoListInsertResponse) commonResponse.getData();

        assertEquals("SUCCESS",commonResponse.getStatus());
        assertEquals(HttpStatus.OK,commonResponse.getHttpStatus());
        assertEquals(resultUUID.toString(),todoListInsertResponse.getMessageId());

    }

    @Test
    public void fail_removeTodoList_removeTransaction() {
        TodoTransactionEntity todoTransactionEntity = new TodoTransactionEntity();
        UUID resultUUID = UUID.randomUUID();
        todoTransactionEntity.setId(resultUUID);
        todoTransactionEntity.setMessage("Mock Message");
        todoTransactionEntity.setActivated(ACTIVATED_MESSAGE);
        Date date = Calendar.getInstance().getTime();
        todoTransactionEntity.setCreatedDate(date);
        todoTransactionEntity.setUpdatedDate(date);

        Mockito.when(todoTransactionRepository.findAllByIdAndActivated(resultUUID, ACTIVATED_MESSAGE)).thenReturn(todoTransactionEntity);

        TodoTransactionEntity removeTransactionEntity = new TodoTransactionEntity();
        removeTransactionEntity.setId(todoTransactionEntity.getId());
        removeTransactionEntity.setActivated(INACTIVATED_MESSAGE);
        removeTransactionEntity.setUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(todoTransactionRepository.save(any())).thenReturn(null);

        CommonResponse commonResponse = todoListService.removeTodoList(resultUUID.toString());

        ErrorResponse errorResponse = (ErrorResponse) commonResponse.getData();

        assertEquals("ERROR",commonResponse.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,commonResponse.getHttpStatus());
        assertEquals("REMOVE UNSUCCESSFULLY",errorResponse.getError());

    }

    @Test
    public void fail_removeTodoList_notFoundTransaction(){
        UUID resultID = UUID.randomUUID();

        Mockito.when(todoTransactionRepository.findAllByIdAndActivated(resultID,ACTIVATED_MESSAGE)).thenReturn(null);

        CommonResponse commonResponse = todoListService.removeTodoList(resultID.toString());

        ErrorResponse errorResponse = (ErrorResponse) commonResponse.getData();

        assertEquals("NOT FOUND", commonResponse.getStatus());
        assertEquals(HttpStatus.NOT_FOUND, commonResponse.getHttpStatus());
        assertEquals("TRANSACTION NOT FOUND", errorResponse.getError());
    }

}
