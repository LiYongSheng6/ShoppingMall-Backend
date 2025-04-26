package com.shoppingmall.demo.config.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class StringListToLongListDeserializer extends JsonDeserializer<List<Long>> {

    @Override
    public List<Long> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        List<String> stringList = mapper.readValue(p, new TypeReference<List<String>>() {
        });

        return stringList.stream()
                .map(Long::parseLong)
                .toList();
    }
}
