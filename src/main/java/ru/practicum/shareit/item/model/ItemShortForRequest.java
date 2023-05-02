package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemShortForRequest {

    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
    private long requestId;
}