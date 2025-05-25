package khaitq.present;


import io.swagger.v3.oas.annotations.Operation;
import khaitq.domain.User;
import khaitq.manager.TaskManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/tasks")
public class TaskResource {
    private final TaskManager manager;

    @GetMapping(path = "/hi")
    public String hello() {
        return "hi";
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(manager.getAllTasks());
    }


    @PostMapping(path = "/")
    @Operation(summary = "Create a new task for user", description = "This endpoint allows you to add a new task to an existing user by their ID.")
    public ResponseEntity<TaskDto> addTaskToUser(@RequestBody TaskDto dto) {
        dto = manager.saveTask(dto);
        return ResponseEntity.ok(dto);
    }



}
