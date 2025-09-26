package khaitq.infra;

import khaitq.domain.exception.EntityNotFoundException;
import khaitq.domain.user.User;
import khaitq.domain.user.UserId;
import khaitq.domain.user.UserRepository;
import khaitq.infra.persitence.UserEntity;
import khaitq.infra.persitence.UserRepositoryDb;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
        return entities.stream().map(e -> {
            User user = modelMapper.map(e, User.class);
            user.setUserId(new UserId(e.getId()));
            return user;
        }).toList();
    }
    @Override
    public User save(User user) {
        UserEntity entity = modelMapper.map(user, UserEntity.class);
        if (StringUtils.isBlank(entity.getPasswordHash())) entity.setProvider("google");
        userRepositoryDb.save(entity);
        return user;
    }
    @Override
    public Optional<User> findById(UserId id) {
        return userRepositoryDb.findById(id.getValue())
                .map(e -> {
                   User user =  modelMapper.map(e, User.class);
                   user.setUserId(new UserId(e.getId()));
                     return user;
                });
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepositoryDb.findByEmail(email)    // Optional<DbUser>
                .map(dbUser -> {
                    User user = modelMapper.map(dbUser, User.class);
                    user.setUserId(new UserId(dbUser.getId()));
                    return user;
                });
    }

    @Override
    public void deleteById(UserId userId) {
        if (!userRepositoryDb.existsById(userId.getValue()))
            throw new EntityNotFoundException("User", userId);
        userRepositoryDb.deleteById(userId.getValue());
    }

    @Override
    public long count() {
        return userRepositoryDb.count();
    }
}
