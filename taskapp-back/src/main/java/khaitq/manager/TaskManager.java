package khaitq.manager;

import khaitq.domain.TaskRepository;
import khaitq.present.TaskDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class TaskManager {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(e -> modelMapper.map(e, TaskDto.class))
                .toList();
    }

}
