package com.lgcms.member.repository;

import com.lgcms.member.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class CategoryRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final String keyPrefix = "CATEGORY:";

    public List<Category> getAllCategories() {
        Set<String> ids = redisTemplate.keys(keyPrefix + "*");
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        List<Long> longIds = ids.stream().map(Long::parseLong).toList();
        List<String> categoryNames = redisTemplate.opsForValue().multiGet(ids.stream().map(id -> keyPrefix + id).toList());
        return IntStream.range(0, ids.size())
            .mapToObj(i -> new Category(longIds.get(i), categoryNames.get(i)))
            .toList();
    }
}
