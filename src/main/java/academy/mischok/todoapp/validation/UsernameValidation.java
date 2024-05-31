package academy.mischok.todoapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface UsernameValidation {

    String charactersAreValid() default "a-zA-Z0-9";
    String message() default "Invalid Username";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
