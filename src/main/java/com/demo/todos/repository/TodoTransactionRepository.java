package com.demo.todos.repository;

import com.demo.todos.model.entity.TodoTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TodoTransactionRepository extends JpaRepository<TodoTransactionEntity, UUID> {
}
