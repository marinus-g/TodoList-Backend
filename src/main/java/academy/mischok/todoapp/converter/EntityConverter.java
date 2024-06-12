package academy.mischok.todoapp.converter;

public interface EntityConverter<E, D> {

    E convertToEntity(D d);

}