package com.artjomkuznetsov.deliveryfee.utils;


import com.artjomkuznetsov.deliveryfee.exceptions.BadRequestBodyException;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Component
public class Updater {
    /**
     * Updates the specified entity object with the given fields.
     * This method allows you to update a {@code T} generic type object with the supplied fields.
     * Fields are represented as {@code Map<String, Object>}, where the key is the field name.
     * to be updated and the value is the new value for that field.
     * Additionally, this method provides the ability to specify a list of non-negative fields. If the
     * updated value of a field is of type {@code float} and is present in the non-negative fields list,
     * the method will check if the new value is non-negative.
     * Note: The fields "id" and "city" are excluded from being updated.
     *
     * @param <T>               The type of the entity to be updated.
     * @param entity            The entity object to be updated.
     * @param fields            A map containing the field names as keys and their new values as values.
     * @param nonNegativeFields A list of field names that should have non-negative values if of type {@code float}.
     * @return The updated entity object.
     * @throws BadRequestBodyException If the updated float value of a field in {@code nonNegativeFields} is negative.
     * @throws BadRequestBodyException If the value of a field is not a number or not compatible with float type.
     */
    public static <T> T updateEntity(T entity, Map<String, Object> fields, List<String> nonNegativeFields) {
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(entity.getClass(), key);
            if (field != null) {
                if (!field.getName().equals("id") && !field.getName().equals("city")) {
                    field.setAccessible(true);
                    if (field.getType().equals(float.class)) {
                        checkFloatValue(field, value, nonNegativeFields);
                        value = ((Number) value).floatValue();
                    }
                    ReflectionUtils.setField(field, entity, value);
                    field.setAccessible(false);
                }
            }
        });
        return entity;
    }

    private static void checkFloatValue(Field field, Object value, List<String> nonNegativeFields) {
        if (value instanceof Number) {
            if (nonNegativeFields.contains(field.getName()) && ((Number) value).floatValue() < 0) {
                field.setAccessible(false);
                throw new BadRequestBodyException(field.getName() + " cannot be negative.");
            }
        } else if (value instanceof String) {
            field.setAccessible(false);
            throw new BadRequestBodyException(field.getName() + " must be a number.");
        }
    }
}
