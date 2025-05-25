package khaitq.present;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import khaitq.manager.UserManager;
import khaitq.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/users")
@Tag(name = "User Management", description = "APIs for managing users in the application")
public class UserResource {
    private final UserManager manager;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(manager.getUsers());
    }

    @Operation(summary = "Get user by ID")
    @GetMapping(path = "/{id}")
    public ResponseEntity<UserTaskDto> getUser(@PathVariable("id") long id) {
        return ResponseEntity.ok(manager.getUserTasksById(id));
    }

    @PostMapping()
    @Operation(summary = "Create a new user", description = "This endpoint allows you to create a new user with the provided details.")
    public ResponseEntity<BaseUserDto> save(@RequestBody BaseUserDto dto) {
        return ResponseEntity.ok(manager.save(dto));
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
