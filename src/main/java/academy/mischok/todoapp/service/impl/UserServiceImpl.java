package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.converter.impl.UserEntityDtoConverter;
import academy.mischok.todoapp.dto.UserDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.repository.UserRepository;
import academy.mischok.todoapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEntityDtoConverter userEntityDtoConverter;


    @Override
    public List<UserDto> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(this::mapToUserDTO).toList();
    }

    @Override
    public Optional<UserEntity> findUserByName(String username) {
        return Optional.empty();
    }

    private UserDto mapToUserDTO(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
    }
}
