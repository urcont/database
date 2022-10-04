package com.edu.ulab.app.service.impl;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class KeyHolderFactory {
    public KeyHolder getKeyHolder() {
        return new GeneratedKeyHolder();
    }

    public static KeyHolder getKeyHolderWithId(Long id) {
        List<Map<String, Object>> keyList = new ArrayList<>();
        keyList.add(Map.of("id", id));
        return new GeneratedKeyHolder(keyList);
    }
}
