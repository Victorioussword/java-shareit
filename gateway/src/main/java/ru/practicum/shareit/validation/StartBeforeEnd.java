package ru.practicum.shareit.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = StartBeforeEndValidator.class)
public @interface StartBeforeEnd {

    String message() default "End должно быть после sta. Start и end не могут быть null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
