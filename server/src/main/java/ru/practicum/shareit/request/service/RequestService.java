package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotExistInDataBase;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemShortForRequest;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestInputDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {

    private Sort sort = Sort.by(Sort.Direction.DESC, "id");
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public RequestOutputDto createRequest(RequestInputDto requestInputDto, long userId) {

        Request request = RequestMapper.toRequest(requestInputDto, userId);
        if (!userRepository.existsById(userId)) {
            throw new NotExistInDataBase("User c Id " + userId + " не существует");
        }
        RequestOutputDto requestOutputDto = RequestMapper.toRequestOutputDto(requestRepository
                .save(request));

        log.info("RequestService - createRequest(). Создан {}", requestOutputDto.toString());
        return requestOutputDto;
    }


    public List<RequestOutputDto> getRequestsByAuthor(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotExistInDataBase("User c Id " + userId + " не существует");
        }

        List<Request> requests = requestRepository.findAllByRequester(userId);

        log.info("RequestService - getRequestsByAuthor(). Возвращен список из  {} элементов", requests.size());
        return prepareRequests(requests);
    }


    public List<RequestOutputDto> getAllRequests(Long userId, Integer from, Integer size) {

        List<Request> requests = requestRepository
                .findRequestsByRequesterNotOrderByCreatedDesc(userId, PageRequest.of(from, size)).toList();

        return prepareRequests(requests);

    }


    public RequestOutputDto getRequestById(long requestId, long userId) {
        Request request = requestRepository.findRequestById(requestId)
                .orElseThrow(() -> new NotFoundException("Request не найден "));

        if (!userRepository.existsById(userId)) {
            throw new NotExistInDataBase("Пользователь не существует");
        }

        List<Request> requests = new ArrayList<>();
        requests.add(request);
        List<RequestOutputDto> requestOutputDtos = prepareRequests(requests);
        log.info("RequestService - getRequestById(). Возвращен {}", requestOutputDtos.get(0));
        return requestOutputDtos.get(0);
    }

    private List<RequestOutputDto> prepareRequests(List<Request> requests) {
        List<RequestOutputDto> forReturn = requests.stream()
                .map(RequestMapper::toRequestOutputDto).collect(toList());

        Map<Request, List<Item>> requestListMap = itemRepository.findByRequestIn(requests, sort)
                .stream().collect(groupingBy(Item::getRequest, toList()));

        for (int i = 0; i < requests.size(); i++) {
            if (!requestListMap.isEmpty() && requestListMap.containsKey(requests.get(i))) {
                List<ItemShortForRequest> itemShortForRequests = requestListMap.get(requests.get(i))
                        .stream().map(ItemMapper::toItemShortForRequest).collect(toList());
                forReturn.get(i).setItems(itemShortForRequests);
            }
        }
        return forReturn;
    }
}