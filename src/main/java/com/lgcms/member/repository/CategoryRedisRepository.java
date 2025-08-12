package com.lgcms.member.repository;

import com.lgcms.member.common.dto.exception.BaseException;
import com.lgcms.member.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static com.lgcms.member.common.dto.exception.CategoryError.NO_SUCH_CATEGORY;

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
        List<Long> longIds = ids.stream().map(idString -> Long.parseLong(idString.substring(9))).toList();
        List<String> categoryNames = redisTemplate.opsForValue().multiGet(ids);
        return IntStream.range(0, ids.size())
            .mapToObj(i -> new Category(longIds.get(i), categoryNames.get(i)))
            .toList();
    }

    public List<Category> getCategoriesById(List<Long> ids) {
        List<String> categoryNames = redisTemplate.opsForValue().multiGet(ids.stream().map(id -> keyPrefix + id).toList());
        if (ids.size() != categoryNames.size()) {
            throw new BaseException(NO_SUCH_CATEGORY);
        }
        return IntStream.range(0, ids.size())
            .mapToObj(i -> new Category(ids.get(i), categoryNames.get(i)))
            .toList();
    }
}
