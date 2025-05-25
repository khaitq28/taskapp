package khaitq.present.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserTaskDto {

    private UserDto user;
    private List<TaskDto> tasks;
}
