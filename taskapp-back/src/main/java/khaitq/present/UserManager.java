package khaitq.present;

import khaitq.domain.User;
import khaitq.infra.UserEntity;
import khaitq.infra.UserRepositoryDb;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class UserManager {
    private final UserRepositoryDb userRepositoryDb;

    private final ModelMapper modelMapper = new ModelMapper();

    public User save(User user) {
        UserEntity entity = modelMapper.map(user, UserEntity.class);
        UserEntity created = userRepositoryDb.save(entity);
        return modelMapper.map(created, User.class);
    }


    public List<User> getUsers() {
        List<UserEntity> entities = userRepositoryDb.findAllWithTasks();
        return entities.stream().map(e -> modelMapper.map(e, User.class)).toList();
    }
    public long count() {
        return userRepositoryDb.count();
    }

    public void removeAll() {
        userRepositoryDb.deleteAll();
    }

    public List<User> getById(long id) {
        return userRepositoryDb.findById(id).stream()
                .map(e -> modelMapper.map(e, User.class))
                .toList();
    }
}
