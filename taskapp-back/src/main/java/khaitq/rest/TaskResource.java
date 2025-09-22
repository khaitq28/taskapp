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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Task Management")
@RequestMapping(path = "/api/v1/tasks")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class TaskResource {
    private final TaskManager taskManager;

    @Operation(summary = "Get all tasks", description = "This endpoint retrieves all tasks of User.")
    @GetMapping()
    public ResponseEntity<List<TaskDto>> getTasksOfUsers() {
        return ResponseEntity.ok(taskManager.getAllTasks());
    }


    @PostMapping()
    @Operation(summary = "Create a new task for user", description = "This endpoint allows you to add a new task to an existing user by their ID.")
    public ResponseEntity<TaskDto> addTaskToUser(@Valid @RequestBody CreateUpdateTaskDto dto) {
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
