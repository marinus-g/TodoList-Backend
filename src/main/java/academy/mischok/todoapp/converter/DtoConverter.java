package academy.mischok.todoapp.converter;

import jakarta.validation.constraints.NotNull;

public interface DtoConverter<D,  E> {

    D convertToDto(@NotNull E e);

}