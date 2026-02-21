package com.danial.taskmanager.controller;

import java.security.Principal;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.danial.taskmanager.model.Task;
import com.danial.taskmanager.service.TaskService;

/**
 * Task Controller
 * Handles HTTP request for task operations
 * All endpoints require authentication
 */

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    private final TaskService taskService;
    //constructor injection
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Get all tasks for the authenticated user
     * GET  /api/tasks
     */

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(Principal principal)
     {
        String username = principal.getName();
        List<Task> tasks = taskService.getUserTasks(username);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get a specific task by ID
     * GET /api/tasks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id, Principal principal) {
        try{
            String username = principal.getName();
            Task task = taskService.getTaskById(id, username);
            return ResponseEntity.ok(task);
        }catch(RuntimeException e){
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(e.getMessage());
        }
    }

    /**
     * Create a new task
     * POST /api/tasks
     */

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task,Principal principal) {
        try{
            String username = principal.getName();
            Task createdTask = taskService.createTask(task, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        }catch(RuntimeException e){
            return ResponseEntity
            .badRequest()
            .body(e.getMessage());
        }
    }

    /**
     * Update an existing task
     * PUT /api/tasks/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long id, 
            @Valid @RequestBody Task task,
            Principal principal
        ) {
        try{
            String username = principal.getName();
            Task updatedTask = taskService.updateTask(id, task, username);
            return ResponseEntity.ok(updatedTask);
        }catch(RuntimeException e){
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    /**
     * Delete a task
     * DELETE /api/tasks/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, Principal principal) {
       try {
        String username = principal.getName();
        taskService.deleteTask(id, username);
        return ResponseEntity.ok("Task deleted successfully");
       } catch (RuntimeException e) {
        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(e.getMessage());
       }
    }

       /**
        * Toggle task completion status
        * PATCH /api/tasks/{id}/toggle
        */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleTaskCompletion(@PathVariable Long id, Principal principal) {
        try {
            String username = principal.getName();
            Task task = taskService.toggleTaskCompletion(id, username);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    /**
     * Get completed tasks
     * GET /api/tasks/completed
    */
    @GetMapping("/completed")
    public ResponseEntity<List<Task>> getCompletedTasks(Principal principal) {
        String username = principal.getName();
        List<Task> tasks = taskService.getCompletedTasks(username);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get incomplete tasks
     * GET /api/tasks/incomplete
     */
    @GetMapping("/incomplete")
    public ResponseEntity<List<Task>> getIncompleteTasks(Principal principal) {
        String username = principal.getName();
        List<Task> tasks = taskService.getIncompleteTasks(username);
        return ResponseEntity.ok(tasks);
    }
    
}