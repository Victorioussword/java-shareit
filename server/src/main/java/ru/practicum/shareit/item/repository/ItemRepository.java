package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByOwnerOrderByIdAsc(long id, Pageable pageable);

    @Query("select i " +
            "from Item i " +
            "where (lower(i.name) like lower(concat('%', ?1, '%')) " +
            "or lower(i.description) like lower(concat('%', ?1, '%'))) " +
            "and i.available is true ")
    Page<Item> search(String text, Pageable pageable);

    List<Item> findByRequestIn(List<Request> requests, Sort sort);  // используеся для перевода в Map
}