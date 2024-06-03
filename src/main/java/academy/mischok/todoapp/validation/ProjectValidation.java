package academy.mischok.todoapp.validation;

import academy.mischok.todoapp.dto.ProjectDto;

public record ProjectValidation (String projectName, String message){
    public boolean isValid(ProjectDto project) {
        return project.getTitle() != null && !project.getTitle().trim().isEmpty();
    }
}
