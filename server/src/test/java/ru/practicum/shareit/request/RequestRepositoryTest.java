package ru.practicum.shareit.request;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase
@TestPropertySource(properties = { "db.name=test2"})
public class RequestRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;


    User user1 = new User(null, "userName1", "user1@user.com");
    User user2 = new User(null, "userName2", "user2@user.com");
    Request request1 = new Request(1L, " descriptionOfRequest1", 2L, LocalDateTime.now().minusDays(1));
    Request request2 = new Request(2L, " descriptionOfRequest2", 1L, LocalDateTime.now());
    Item item1 = new Item(null, "item1", "description Item1", true, 1L, request1);
    Item item2 = new Item(null, "item2", "description Item2", true, 1L, request1);

    @BeforeEach
    void beforeEach() {

        userRepository.save(user1);
        userRepository.save(user2);
        requestRepository.save(request1);
        requestRepository.save(request2);
        itemRepository.save(item1);
        itemRepository.save(item2);
    }

    @Test
    void contextLoads() {
        assertThat(em).isNotNull();
    }

    @DirtiesContext
    @Test
    void shouldFindAllWithoutUserId() {

        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<Request> requestsAfter = requestRepository.findAllWithoutUserId(2l, pageRequest);
        List<Request> requestList = requestsAfter.toList();
        assertEquals(1, requestList.size());
        assertEquals(" descriptionOfRequest2", requestList.get(0).getDescription());
    }
}