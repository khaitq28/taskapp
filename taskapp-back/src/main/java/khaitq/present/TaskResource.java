package khaitq.present;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import khaitq.manager.TaskManager;
import khaitq.present.dto.CreateUpdateTaskDto;
import khaitq.present.dto.TaskDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Task Management", description = "APIs for managing tasks in the application")
@RequestMapping(path = "/api/v1/tasks")
public class TaskResource {
    private final TaskManager manager;

    @Operation(summary = "Get all tasks", description = "This endpoint retrieves all tasks from the system.")
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(manager.getAllTasks());
    }


    @PostMapping(path = "/")
    @Operation(summary = "Create a new task for user", description = "This endpoint allows you to add a new task to an existing user by their ID.")
    public ResponseEntity<TaskDto> addTaskToUser(@RequestBody CreateUpdateTaskDto dto) {
        TaskDto ret = manager.createTask(dto);
        return ResponseEntity.ok(ret);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("id") long id, @RequestBody CreateUpdateTaskDto dto) {
        TaskDto ret = manager.updateTask(id, dto);
        return ResponseEntity.ok(ret);
    }

}
