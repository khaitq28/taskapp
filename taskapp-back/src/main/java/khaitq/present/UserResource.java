package khaitq.present;


import khaitq.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/users")
public class UserResource {
    private final UserManager manager;

    @GetMapping(path = "/hi")
    public String hello() {
        return "hi";
    }

    @PostMapping()
    public ResponseEntity<User> save(@RequestBody User dto) {
        return ResponseEntity.ok(manager.save(dto));
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(manager.getUsers());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        return ResponseEntity.ok(manager.getById(id));
    }

    @PostMapping(path = "/{id}/tasks")
    public ResponseEntity<User> addTaskToUser(@PathVariable("id") long userId, @RequestBody TaskDto dto) {
        User user = manager.addTaskToUser(userId, dto);
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(manager.count());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        try {
            manager.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
