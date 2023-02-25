package ru.practicum.shareit.item.dto;

/**
 * TODO Sprint add-controllers.
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;


@Setter
@Getter
@AllArgsConstructor
@Validated
public class ItemDto {

    private long id;
    // @NotNull
  //  @NotBlank
    private String name;
    // @NotNull
  //  @NotBlank
    private String description;

    private Boolean available;
    private Long ownerId;
    private long itemRequest;
}


//DTO — это так называемый value-object на стороне сервера,
// который хранит данные, используемые в слое представления.
// Мы разделим DTO на те, что мы используем при запросе (Request)
// и на те, что мы возвращаем в качестве ответа сервера (Response).
// В нашем случае, они автоматически сериализуются и десериализуются фреймворком Spring.