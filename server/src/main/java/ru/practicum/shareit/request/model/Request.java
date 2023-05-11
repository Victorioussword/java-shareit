package ru.practicum.shareit.request.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Setter
@Getter
@ToString
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                 // — уникальный идентификатор запроса;

    @Column(name = "description")
    private String description;      // — текст запроса, содержащий описание требуемой вещи;

    @Column(name = "requester_id")
    private long requester;          // — пользователь, создавший запрос;

    @Column(name = "created")
    private LocalDateTime created;   // — дата и время создания запроса
}