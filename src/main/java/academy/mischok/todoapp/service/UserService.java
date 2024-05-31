package academy.mischok.todoapp.service;

import academy.mischok.todoapp.dto.UserDto;
import academy.mischok.todoapp.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAllUsers();

    Optional<UserEntity> findUserByName(String username);

}