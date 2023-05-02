package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.user.model.UserShort;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoForReturn {

    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemShort item;
    private UserShort booker;
    private Status status;
}