package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.ItemShortForRequest;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class RequestOutputDto {

    private long id;
    private String description;
    private long requestor;
    private LocalDateTime created;
    private List<ItemShortForRequest> items;
}
