package ru.practicum.shareit.booking;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
public class Booking {
    long id;
    LocalDateTime start;
    LocalDateTime end;
    long itemId;
    long booker;
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

