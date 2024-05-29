package academy.mischok.todoapp.service;

import academy.mischok.todoapp.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAllUsers();

}