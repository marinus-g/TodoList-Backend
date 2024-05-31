package academy.mischok.todoapp.converter.impl;

import academy.mischok.todoapp.converter.DtoConverter;
import academy.mischok.todoapp.converter.EntityConverter;
import academy.mischok.todoapp.dto.UserDto;
import academy.mischok.todoapp.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityConverter implements DtoConverter<UserDto, UserEntity>,
        EntityConverter<UserEntity, UserDto>{
    @Override
    public UserDto convertToDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
    }

    @Override
    public UserEntity convertToEntity(UserDto userDto) {
        return UserEntity.builder()
                .id(userDto.getId())
                .username(userDto.getUserName())
                .email(userDto.getEmail())
                .build();
    }
}