package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Setter
@Getter
@ToString
public class ItemRequest {

    private long id;                 // — уникальный идентификатор запроса;
    private String description;      // — текст запроса, содержащий описание требуемой вещи;
    private long requestor;          // — пользователь, создавший запрос;
    private LocalDateTime created;   // — дата и время создания запроса

}
