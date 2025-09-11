package khaitq.rest;

import org.springframework.security.access.method.P;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import khaitq.applicatioin.UserManager;
import khaitq.rest.dto.BaseUserDto;
import khaitq.rest.dto.UserDto;
import khaitq.rest.dto.UserTaskDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/users")
@Tag(name = "User Management")
public class UserResource {
    private final UserManager manager;

    @GetMapping
    @PreAuthorize("@authManager.isAdmin()")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(manager.getUsers());
    }

//    @Operation(summary = "Get user and tasks by User ID")
//    @GetMapping(path = "/{id}")
//    public ResponseEntity<UserTaskDto> getUser(@PathVariable("id") String userId) {
//        return ResponseEntity.ok(manager.getUserWithTasksById(userId));
//    }

    @Operation(summary = "Get user and tasks by User email")
    @PreAuthorize("@authManager.isCurrentUser(#p0) || @authManager.isAdmin()")
    @GetMapping(path = "/{email}")
    public ResponseEntity<UserTaskDto> getUserByEmail(@PathVariable("email")  String email) {
        return ResponseEntity.ok(manager.getUserWithTasksByEmail(email));
    }

    @PostMapping()
    @PreAuthorize("@authManager.isAdmin()")
    @Operation(summary = "Create a new user", description = "This endpoint allows you to create a new user with the provided details.")
    public ResponseEntity<BaseUserDto> save(@RequestBody @Valid BaseUserDto dto) {
        return ResponseEntity.ok(manager.save(dto));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("@authManager.isAdmin()")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        manager.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
