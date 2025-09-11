package khaitq.rest;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/")
public class MainResource {

    @GetMapping()
    @PreAuthorize("hasRole('tasks_writer')")
    public String hello() {
        return "OK";
    }

}
