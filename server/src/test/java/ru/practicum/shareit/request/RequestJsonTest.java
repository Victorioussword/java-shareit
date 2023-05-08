package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.ItemShortForRequest;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestJsonTest {

    @Autowired
    private JacksonTester<RequestOutputDto> tester;

    RequestOutputDto requestOutputDto;

    @BeforeEach
    void beforeEach() {

        Request request = new Request(1L, "descriptionOfRequest1", 1L, LocalDateTime.now());
        List<ItemShortForRequest> itemShorts = List.of(new ItemShortForRequest(1L,
                "itemName1",
                "itemDescription1",
                true,
                1L,
                2L));

        requestOutputDto = RequestMapper.toRequestOutputDto(request);
        requestOutputDto.setItems(itemShorts);
    }

    @Test
    void testOfSerializing() throws Exception {
        JsonContent<RequestOutputDto> result = tester.write(requestOutputDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.requestor");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).hasJsonPath("$.items");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(Math.toIntExact(requestOutputDto.getId()));
        assertThat(result).extractingJsonPathNumberValue("$.requestor").isEqualTo(Math.toIntExact(requestOutputDto.getRequestor()));
    }
}