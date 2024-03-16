package com.artjomkuznetsov.deliveryfee.utils;


import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

@Component
public class Patcher {
    /**
     *
     * @param entity
     * @param fields
     * @return
     * @param <T>
     */
    public static  <T> T patch(T entity, Map<String, Object> fields) {
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(entity.getClass(), key);
            if (field != null) {
                field.setAccessible(true);
                // convert Double to Float, if the field data passed is a floating point number
                if (field.getType().equals(float.class) && value instanceof Double) {
                    value = ((Double) value).floatValue();
                };
                ReflectionUtils.setField(field, entity, value);
                field.setAccessible(false);
            }
        });
        return entity;
    }
}
