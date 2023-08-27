package ru.practicum.shareitgateway.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitgateway.client.BaseClient;
import ru.practicum.shareitgateway.request.dto.ItemRequestDto;

import java.util.Map;

@Service
@Slf4j
public class ItemRequestClient extends BaseClient {
    @Autowired
    public ItemRequestClient(@Qualifier("requests") RestTemplate restTemplate) {
        super(restTemplate);
    }

    public ResponseEntity<Object> createItemRequest(long userId, ItemRequestDto itemRequestDto) {
        log.info("Create ite, request {}", userId);
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> getItemRequest(Long requestId, long userId) {
        log.info("Get request with id = {}", requestId);
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getAllItemRequest(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size);
        log.info("Get all item requests for user with id =  {}", userId);
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getItemRequestsByUser(long userId) {
        log.info("Get all  for user with id =  {}", userId);
        return get("", userId);
    }

}
