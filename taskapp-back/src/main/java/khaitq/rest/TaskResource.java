package khaitq.rest;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import khaitq.applicatioin.TaskManager;
import khaitq.rest.dto.CreateUpdateTaskDto;
import khaitq.rest.dto.TaskDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Task Management")
@RequestMapping(path = "/api/v1/users/{userId}/tasks")
public class TaskResource {
    private final TaskManager taskManager;

    @Operation(summary = "Get all tasks", description = "This endpoint retrieves all tasks of User.")
    @GetMapping()
    public ResponseEntity<List<TaskDto>> getTasksOfUser(@Parameter(name = "userId", description = "ID of the user", required = true)
                                                         @PathVariable("userId") String userId) {
        return ResponseEntity.ok(taskManager.getByUserId(userId));
    }


    @PostMapping()
    @Operation(summary = "Create a new task for user", description = "This endpoint allows you to add a new task to an existing user by their ID.")
    public ResponseEntity<TaskDto> addTaskToUser(@Parameter(name = "userId", description = "ID of the user", required = true)
                                                     @PathVariable("userId") Long userId,
                                                     @Valid @RequestBody CreateUpdateTaskDto dto) {
        TaskDto ret = taskManager.createTask(dto);
        return ResponseEntity.ok(ret);
    }

    @PutMapping(path = "/{taskId}")
    @Operation(summary = "Update an existing task", description = "This endpoint allows you to update an existing task by its ID.")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("taskId") String taskId, @RequestBody CreateUpdateTaskDto dto) {
        TaskDto ret = taskManager.updateTask(taskId, dto);
        return ResponseEntity.ok(ret);
    }

    @GetMapping(path = "/{taskId}")
    @Operation(summary = "Get task by ID", description = "This endpoint retrieves a specific task by its ID.")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable("taskId") String taskId) {
        TaskDto ret = taskManager.getById(taskId);
        return ResponseEntity.ok(ret);
    }

    @DeleteMapping(path = "/{taskId}")
    @Operation(summary = "Delete a task by ID", description = "This endpoint allows you to delete a specific task by its ID.")
    public ResponseEntity<Void> deleteTask(@PathVariable("taskId") String taskId) {
        taskManager.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }


}
