package ru.practicum.shareit.validation;


import java.time.LocalDateTime;
import javax.validation.ConstraintValidator;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookItemRequestDto> {

    @Override
    public void initialize(StartBeforeEnd constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookItemRequestDto bookItemRequestDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookItemRequestDto.getStart();
        LocalDateTime end = bookItemRequestDto.getEnd();

        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}