package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.dto.UserDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.repository.UserRepository;
import academy.mischok.todoapp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(this::mapToUserDTO).toList();
    }
    private UserDto mapToUserDTO(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUserName())
                .email(userEntity.getEmail())
                .build();
    }
}
