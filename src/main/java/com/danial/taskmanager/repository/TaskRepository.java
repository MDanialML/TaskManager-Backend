package com.danial.taskmanager.repository;

 import com.danial.taskmanager.model.Task;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;
 import java.util.List;

 @Repository
    public interface TaskRepository extends JpaRepository<Task, Long> {
        
        List<Task> findByCompleted(Boolean completed);
        List<Task> findByTitleContainingIgnoreCase(String keyword); 
        List<Task> findByOrderByCreatedAtDesc();
    }
