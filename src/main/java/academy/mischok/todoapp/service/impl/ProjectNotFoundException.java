package academy.mischok.todoapp.service.impl;

public class ProjectNotFoundException extends Throwable {
    public ProjectNotFoundException(String s) {
        super(s);
    }
}
