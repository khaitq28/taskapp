package khaitq.present;


import khaitq.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserResource {
    private final UserManager manager;

    public UserResource(UserManager manager) {
        this.manager = manager;
    }

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
    public ResponseEntity<List<User>> getUser(@PathVariable("id") long id) {
        return ResponseEntity.ok(manager.getById(id));
    }

    @GetMapping(path = "/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(manager.count());
    }

    @DeleteMapping()
    public ResponseEntity<Void> removeAll() {
        try {
            manager.removeAll();
            return ResponseEntity.ok().build();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
