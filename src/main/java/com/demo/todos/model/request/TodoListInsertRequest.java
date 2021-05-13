package com.demo.todos.model.request;

import javax.validation.constraints.NotBlank;

public class TodoListInsertRequest {

    @NotBlank(message = "message is in valid")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
