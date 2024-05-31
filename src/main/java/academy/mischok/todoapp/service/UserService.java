package academy.mischok.todoapp.service;

import academy.mischok.todoapp.dto.RegistrationDto;
import academy.mischok.todoapp.dto.UserDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.validation.UserNameValidation;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAllUsers();

    Optional<UserDto> createUser(RegistrationDto userDto);

    Optional<UserEntity> findUserByName(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<? extends UserEntity> findByLogin(String login);

    void deleteUser(Long id);

    public UserNameValidation isValidUsername(String username);

}