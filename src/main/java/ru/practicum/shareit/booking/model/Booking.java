package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "startTime")
    LocalDateTime start;

    @Column(name = "endTime")
    LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "itemId")
    Item item;

    @ManyToOne
    @JoinColumn(name = "booker")
    User booker;

    @Column(name = "status")
    String status;
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

