package khaitq.present;


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


}
