package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.converter.impl.ToDoEntityConverter;
import academy.mischok.todoapp.dto.ToDoDto;
import academy.mischok.todoapp.model.Status;
import academy.mischok.todoapp.model.ToDoEntity;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.repository.ToDoRepository;
import academy.mischok.todoapp.service.ToDoService;
import academy.mischok.todoapp.validation.TodoValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

import java.awt.image.ImageProducer;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {
    private final String DATE_FORMAT = "dd-MM-yyyy";
    private final ToDoRepository toDoRepository;
    private final ToDoEntityConverter
            toDoEntityConverter;

    @Override
    public List<ToDoDto> findAllToDos() {
        List<ToDoEntity> users = toDoRepository.findAll();
        return users.stream()
                .map(toDoEntityConverter::convertToDto)
                .toList();
    }

    @Override
    public Optional<ToDoEntity> findToDoByTitle(String title) {
        return this.toDoRepository.findByTitle(title);
    }

    @Override
    public boolean existsByTitle(String title) {
        return this.toDoRepository.existsByTitle(title);
    }

    @Override
    public void deleteToDo(Long id) {
        this.toDoRepository.deleteById(id);
    }

    @Override
    public Optional<ToDoDto> createToDo(UserEntity user, ToDoDto dto) {
        return Optional.ofNullable(dto)
                .map(toDoEntityConverter::convertToEntity)
                .stream()
                .peek(toDoEntity -> toDoEntity.setUser(user))
                .map(toDoRepository::save)
                .map(toDoEntityConverter::convertToDto)
                .findFirst();
    }

    public Optional<ToDoDto> deleteById(UserEntity user, ToDoDto toDoDto) {
        return Optional.ofNullable(toDoDto)
                .map(toDoEntityConverter::convertToEntity)
                .stream()
                .map(toDoRepository::delete)
                .map(toDoEntityConverter::convertToDto);
    }

    /*
        todo = {
                id: 1,
                name: "Hallo"
                }
     */
    // -> // --> GET /todo/1
    // --> erstmal wirst du authentifiziert als user mit der id 2
    // --> dann wird findByIdAndUser(userMitId2, 1) ausgeführt

    public Optional<ToDoDto> findByIdAndUser(UserEntity user, Long id) {
        return toDoRepository.findByIdAndUser(id, user)
                .map(toDoEntityConverter::convertToDto);

    }

    @Override
    public TodoValidation isValidTodo(ToDoDto dto) {
        TodoValidation validation;
        if (!(validation = isTitleValid(dto.getTitle())).isValid()) {
            return validation;
        } else if (!(validation = isDescriptionValid(dto.getDescription())).isValid()) {
            return validation;
        } else if(!(validation = isDateValid(dto.getStartDate())).isValid()) {
            return validation;
        } else if (!(validation = isDateValid(dto.getEndDate())).isValid()) {
            return validation;
        } else if (!(validation = isStatusValid(dto.getStatus())).isValid()) {
            return validation;
        }
        return new TodoValidation(true, null);
    }

    @Override
    public TodoValidation isTitleValid(String title) {
        if (title == null) {
            return new TodoValidation(false, "Title cannot be null");
        } else if (title.isBlank()) {
            return new TodoValidation(false, "Title cannot be blank");
        }
        return new TodoValidation(true, null);
    }

    @Override
    public TodoValidation isDescriptionValid(String description) {
        if (description == null) {
            return new TodoValidation(false, "Description cannot be null");
        } else if (description.isBlank()) {
            return new TodoValidation(false, "Description cannot be blank");
        }
        return new TodoValidation(true, null);
    }
    @Override
    public TodoValidation isDateValid(Date date) {
        if( date != null) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_FORMAT);
                df.setLenient(false);
                df.parse(date.toString());
                return new TodoValidation(true, null);
            } catch (ParseException e) {
                return new TodoValidation(false, "Date invalid");
            }
        } else{
            return new TodoValidation(true, null);
        }

    }
    @Override
    public TodoValidation isStatusValid(Status status){
        if (status == null) {
            return new TodoValidation(false, "Status cannot be null");
        } else if (status.toString().isBlank()) {
            return new TodoValidation(false, "Status cannot be blank");
        } else if (!status.toString().equalsIgnoreCase("todo")
                && !status.toString().equalsIgnoreCase("doing")
                && !status.toString().equalsIgnoreCase("done")){
            return new TodoValidation(false,"Status is valid.");
        }
        return new TodoValidation(true,null);

    }

    @Override
    public List<ToDoDto> findAllToDosByUser(UserEntity user) {
        return this.toDoRepository.findByUser(user)
                .stream()
                .map(toDoEntityConverter::convertToDto)
                .toList();
    }
}
