package com.artjomkuznetsov.deliveryfee.utils;


import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

@Component
public class Updater {
    /**
     *
     * @param entity
     * @param fields
     * @return
     * @param <T>
     */
    public static  <T> T updateEntity(T entity, Map<String, Object> fields) {
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(entity.getClass(), key);
            if (field != null) {
                if (!field.getName().equals("id") && !field.getName().equals("city")) {
                    field.setAccessible(true);
                    // convert Double to Float, if the field data passed is a floating point number
                    if (field.getType().equals(float.class) && value instanceof Double) {
                        value = ((Double) value).floatValue();
                    }
                    ;
                    ReflectionUtils.setField(field, entity, value);
                    field.setAccessible(false);
                }
            }
        });
        return entity;
    }
}
