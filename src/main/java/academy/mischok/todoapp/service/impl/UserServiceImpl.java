package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.converter.impl.UserEntityConverter;
import academy.mischok.todoapp.dto.RegistrationDto;
import academy.mischok.todoapp.dto.UserDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.repository.UserRepository;
import academy.mischok.todoapp.service.UserService;
import academy.mischok.todoapp.validation.UserNameValidation;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEntityConverter userEntityConverter;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(userEntityConverter::convertToDto)
                .toList();
    }

    @Override
    public Optional<UserDto> createUser(RegistrationDto dto) {
        return Optional
                .ofNullable(dto)
                .filter(registrationDto -> !userRepository.existsByUsername(dto.getUsername()))
                .map(userDto1 -> UserEntity
                        .builder()
                        .email(dto.getEmail())
                        .username(dto.getUsername())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .build()
                )
                .stream()
                .peek(userEntity -> userEntity.setTodos(new ArrayList<>()))
                .map(userRepository::save)
                .map(userEntityConverter::convertToDto)
                .findFirst();
    }

    @Override
    public Optional<UserEntity> findUserByName(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public Optional<UserEntity> findByLogin(String login) {
        return this.userRepository.findByLogin(login);
    }

    @Override
    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public UserNameValidation isValidUsername(String username) {
        if (Objects.isNull(username)) {
            return new UserNameValidation(false, "Username not present");
        } else if (username.length() < 3) {
            return new UserNameValidation(false, "Username too short");
        }  else if (username.length() > 20) {
            return new UserNameValidation(false, "Username too long");
        } else if (!username.matches("^[a-zA-Z0-9]+$")) {
            return new UserNameValidation(false,
                    "Username should only contains numeric " +
                            "and alphabetic characters");
        }
        return new UserNameValidation(true, null);
    }
}