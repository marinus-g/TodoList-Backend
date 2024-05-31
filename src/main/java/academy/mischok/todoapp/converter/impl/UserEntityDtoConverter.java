package academy.mischok.todoapp.converter.impl;

import academy.mischok.todoapp.converter.DtoConverter;
import academy.mischok.todoapp.dto.UserDto;
import academy.mischok.todoapp.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityDtoConverter implements DtoConverter<UserDto, UserEntity> {
    @Override
    public UserDto convertToDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
    }
}