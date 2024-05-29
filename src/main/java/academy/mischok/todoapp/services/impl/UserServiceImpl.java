package academy.mischok.todoapp.services.impl;

import academy.mischok.todoapp.dto.UserDTO;
import academy.mischok.todoapp.models.UserEntity;
import academy.mischok.todoapp.repositories.UserRepository;
import academy.mischok.todoapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> findAllUserEntities() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(UserEntity -> mapToUserDTO(UserEntity)).collect(Collectors.toList());
    }
    private UserDTO mapToUserDTO(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUserName())
                .email(userEntity.getEmail())
                .build();
    }
}
