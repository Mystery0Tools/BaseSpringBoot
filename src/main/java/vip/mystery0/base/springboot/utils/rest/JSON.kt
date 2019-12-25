package vip.mystery0.base.springboot.utils.rest;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author Mystery0
 */
public interface JSON {
    <T> T fromJson(String json, Class<T> clazz);

    <T> T fromJson(String json, TypeReference<T> typeReference);

    <T> String toJson(T obj);
}
