package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.comment.CommentDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemWithBookingAndCommentsDto {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private BookingShortDto lastBooking;
        private BookingShortDto nextBooking;
        private List<CommentDto> comments;
    }
