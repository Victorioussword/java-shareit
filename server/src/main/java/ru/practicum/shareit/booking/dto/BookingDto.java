package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private long itemId;
    @JsonIgnore
    private long booker;
}