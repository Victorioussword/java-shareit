package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.StartBeforeEnd;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@StartBeforeEnd
public class BookItemRequestDto {

	private long itemId;

	@FutureOrPresent (message = "Начало бронирования должно быть настоящим или будущем")
	@NotNull(message = "Не задано начало бронирования")
	private LocalDateTime start;

	@Future  (message = "Конец бронирования должен быть в будущем")
	@NotNull (message = "Не задан конец бронирования")
	private LocalDateTime end;
}
