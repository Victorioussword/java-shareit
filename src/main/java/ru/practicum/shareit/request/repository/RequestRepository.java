package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequester(Long userId);


    @Query(nativeQuery = true,
            value = "select * from REQUESTS where REQUESTS.requester_id !=?1",
            countQuery = "select count(*) from REQUESTS where REQUESTS.requester_id !=?1")
    Page<Request> findAllWithoutUserId(Long userId, Pageable pageable);


    Optional<Request> findRequestById(long requestId);
}