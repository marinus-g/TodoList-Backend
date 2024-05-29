package academy.mischok.todoapp.services;

import academy.mischok.todoapp.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> findAllUserEntities();
}
