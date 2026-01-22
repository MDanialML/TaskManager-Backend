package com.danial.taskmanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.danial.taskmanager.exception.TaskNotFoundException;
import com.danial.taskmanager.model.Task;
import com.danial.taskmanager.model.User;
import com.danial.taskmanager.repository.TaskRepository;
import com.danial.taskmanager.repository.UserRepository;


/**
 * Task Service
 * Handles business logic for task operation
 */

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;


    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Get all tasks for a specific user
     * 
     * @param username - Username of authenticated user
     * @return List of User's tasks
     */

    public List<Task> getUserTasks(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
            return taskRepository.findByUserId(user.getId());
    }

    /**
     * Get a specific  task by ID(only if it belongs to the user)
     * @param taskId - Task Id
     * @param username - username of authenticated user
     * @return Task if found and belongs to user
     */
    public Task getTaskById(Long taskId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new RuntimeException("Task not found or access denied"));
    }


    /**
     * Create a new task for the authenticated user
     * @param task -  Task to create
     * @param username - Username of authenticated user 
     * @return Created task
     */

    public Task createTask(Task task, String username) {
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
        //Associate task with user
        task.setUser(user);
        return taskRepository.save(task);
    }

    /**
     * Update an existing task (Only if it belongs to the user)
     * 
     * @param taskId - Task Id to Update
     * @param updatedTask - update task data
     * @param username - username of authenticated user
     * @return Updated task
     */

    public Task updateTask(Long taskId, Task updatedTask, String username) {
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Task not found or access denied"));

        //find task and verify ownership
        Task existingTask = taskRepository.findByIdAndUserId(taskId, user.getId())
        .orElseThrow(() -> new RuntimeException("Task not found or access denied"));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.getCompleted());

        return taskRepository.save(existingTask);
    }

    /**
     * Delete a task (only if it belongs to the user)
     * @param taskId - Task ID to delete
     * @param username - Username of authenticated user
     */

    public void deleteTask(Long taskId, String username) {
       User user = userRepository.findByUsername(username)
       .orElseThrow(() -> new RuntimeException("User not found"));

       //Find task and verify ownership
       Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
       .orElseThrow(() -> new RuntimeException("Task not found or access denied"));
       taskRepository.delete(task);
    }

    /**
     * Toggle task completion status
     * 
     * @param taskId - Task ID
     * @param username - Username of authenticated user
     * @return Updated task
     */

    public Task toggleTaskCompletion(Long taskId, String username){
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
        .orElseThrow(() -> new RuntimeException("Task not found or access denied"));

        task.setCompleted(!task.getCompleted());
        
        return taskRepository.save(task);
    }

    /**
     * Get completed tasks for user
     * 
     * @param username - Username of authenticated user
     * @return list of completed tasks
     */
    public List<Task> getCompletedTasks(String username){
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
        return taskRepository.findByUserIdAndCompleted(user.getId(), true);
    }
    /**
     * Get incomplete tasks for user
     * @param username - Username of authenticated user
     * @return List of incomplete tasks
     */
    public List<Task> getIncompleteTasks(String username){
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
        return taskRepository.findByUserIdAndCompleted(user.getId(), false);
    }
}