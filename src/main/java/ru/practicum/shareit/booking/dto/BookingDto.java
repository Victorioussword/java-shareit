package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.Create;
import ru.practicum.shareit.user.dto.Update;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    @NotNull(groups = Update.class)
    private long id;

    @FutureOrPresent(groups = Create.class)
    @NotNull(groups = Create.class)
    private LocalDateTime start;

    @Future(groups = Create.class)
    @NotNull(groups = Create.class)
    private LocalDateTime end;

    @NotNull(groups = Create.class)
    private long itemId;

    private long booker;
    String status;
}
