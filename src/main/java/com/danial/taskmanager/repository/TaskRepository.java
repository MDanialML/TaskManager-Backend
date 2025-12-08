package com.danial.taskmanager.repository;

 import com.danial.taskmanager.model.Task;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;
 import java.util.List;
import java.util.Optional;

 @Repository
    public interface TaskRepository extends JpaRepository<Task, Long> {
        
        List<Task> findByCompleted(Boolean completed);

        List<Task> findByTitleContainingIgnoreCase(String keyword); 

        List<Task> findByOrderByCreatedAtDesc();

         // New user-specific methods for authentication
        List<Task> findByUserId(Long userId);
    
         List<Task> findByUserIdOrderByCreatedAtDesc(Long userId);
    
        Optional<Task> findByIdAndUserId(Long id, Long userId);
    
        List<Task> findByUserIdAndCompleted(Long userId, Boolean completed);
    
        List<Task> findByUserIdAndTitleContainingIgnoreCase(Long userId, String keyword);
    
        Long countByUserId(Long userId);
    
        void deleteByIdAndUserId(Long id, Long userId);
    }
