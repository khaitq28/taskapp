package khaitq.infra;

import khaitq.domain.User;
import khaitq.domain.UserRepository;
import khaitq.infra.persitence.TaskEntity;
import khaitq.infra.persitence.UserEntity;
import khaitq.infra.persitence.UserRepositoryDb;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserRepositoryDb userRepositoryDb;
    private final ModelMapper modelMapper = new ModelMapper();
    @Override
    public List<User> findAll() {
        List<UserEntity> entities = userRepositoryDb.findAll();
        return entities.stream().map(e -> modelMapper.map(e, User.class)).toList();
    }
    @Override
    public User save(User user) {
        UserEntity entity = modelMapper.map(user, UserEntity.class);
        entity = userRepositoryDb.save(entity);
        return modelMapper.map(entity, User.class);
    }
    @Override
    public Optional<User> findById(long id) {
        return Optional.of(userRepositoryDb.findById(id))
                .map(e -> modelMapper.map(e, User.class));
    }

    @Override
    public void deleteById(long id) {
        if (!userRepositoryDb.existsById(id))
            throw new RuntimeException("User not found");
        userRepositoryDb.deleteById(id);
    }
}
