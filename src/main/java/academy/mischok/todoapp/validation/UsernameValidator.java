package academy.mischok.todoapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<UsernameValidation, String> {

    private Pattern regex;

    @Override
    public void initialize(UsernameValidation usernameValidation) {
        String regex = usernameValidation.charactersAreValid();
        this.regex = Pattern.compile("^[" + regex + "]+$");
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && s.length() > 2 && s.length() < 51 && regex.matcher(s).matches();
    }
}
