package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "startTime")
    private LocalDateTime start;

    @Column(name = "endTime")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker")
    private User booker;

    @Column(name = "status")
    private String status;
}


//    Класс Booking в пакете booking будет содержать следующие поля:
//        id — уникальный идентификатор бронирования;
//        start — дата и время начала бронирования;
//        end — дата и время конца бронирования;
//        item — вещь, которую пользователь бронирует;
//        booker — пользователь, который осуществляет бронирование;
//        status — статус бронирования. Может принимать одно из следующих
//        значений: WAITING — новое бронирование, ожидает одобрения, APPROVED —
//        Дополнительные советы ментора 2
//        бронирование подтверждено владельцем, REJECTED — бронирование
//        отклонено владельцем, CANCELED — бронирование отменено создателем.

