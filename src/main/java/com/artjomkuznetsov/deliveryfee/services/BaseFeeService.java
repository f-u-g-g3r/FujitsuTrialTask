package com.artjomkuznetsov.deliveryfee.services;


import com.artjomkuznetsov.deliveryfee.exceptions.BadRequestBodyException;
import com.artjomkuznetsov.deliveryfee.utils.Updater;
import com.artjomkuznetsov.deliveryfee.assemblers.RegionalBaseFeeModelAssembler;
import com.artjomkuznetsov.deliveryfee.controllers.BaseFeeController;
import com.artjomkuznetsov.deliveryfee.exceptions.RegionalBaseFeeNotFoundException;
import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.repositories.RegionalBaseFeeRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BaseFeeService {

    private final RegionalBaseFeeRepository repository;
    private final RegionalBaseFeeModelAssembler assembler;

    public BaseFeeService(RegionalBaseFeeRepository repository, RegionalBaseFeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /**
     * Retrieves all RegionalBaseFee entities from the JPA repository with links to the corresponding operations.
     * This method assumes that the repository will not return null.
     * @return A CollectionModel containing EntityModel instances of all RegionalBaseFee entities.
     */
    public CollectionModel<EntityModel<RegionalBaseFee>> getAllBaseFees() {
        List<EntityModel<RegionalBaseFee>> baseFees = repository.findAll().stream()
                .map(baseFee -> EntityModel.of(baseFee,
                        linkTo(methodOn(BaseFeeController.class).oneByCity(baseFee.getCity())).withSelfRel(),
                        linkTo(methodOn(BaseFeeController.class).all()).withRel("regionalBaseFees")))
                .toList();

        return CollectionModel.of(baseFees, linkTo(methodOn(BaseFeeController.class).all()).withSelfRel());
    }

    /**
     * Retrieves a RegionalBaseFee entity for the specified city from the JPA repository with links to the corresponding operations.
     * @param city The name of the city for which to retrieve the RegionalBaseFee.
     * @return An EntityModel representing the RegionalBaseFee entity for the specified city.
     * @throws RegionalBaseFeeNotFoundException if no RegionalBaseFee is found for the specified city.
     */
    public EntityModel<RegionalBaseFee> getOneByCity(String city) {
        RegionalBaseFee baseFee = repository.findByCity(city)
                .orElseThrow(() -> new RegionalBaseFeeNotFoundException(city));

        return EntityModel.of(baseFee,
                linkTo(methodOn(BaseFeeController.class).oneByCity(city)).withSelfRel(),
                linkTo(methodOn(BaseFeeController.class).all()).withRel("regionalBaseFees"));
    }

    /**
     * Updates the RegionalBaseFee for the specified city with the provided fields.
     * If no RegionalBaseFee is found for the specified city, a RegionalBaseFeeNotFoundException is thrown.
     *
     * @param fields A Map containing the fields to update and their new values.
     * @param city The name of the city for which to update the RegionalBaseFee.
     * @return EntityModel containing the updated RegionalBaseFee with links to the corresponding operations.
     * @throws RegionalBaseFeeNotFoundException if no RegionalBaseFee is found for the specified city.
     */
    public EntityModel<RegionalBaseFee> updateBaseFee(Map<String, Object> fields, String city) {
        RegionalBaseFee updatedBaseFee = repository.findByCity(city)
                .orElseThrow(() -> new RegionalBaseFeeNotFoundException(city));

        repository.save(updateBaseFee(updatedBaseFee, fields));
        return assembler.toModel(updatedBaseFee);
    }

    private static  <T> T updateBaseFee(T entity, Map<String, Object> fields) {
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(entity.getClass(), key);
            if (field != null) {
                if (!field.getName().equals("id") && !field.getName().equals("city")) {
                    field.setAccessible(true);
                    // convert Double to Float, if the field data passed is a floating point number
                    if (field.getType().equals(float.class) && value instanceof Double) {
                        try {
                            float floatValue = ((Double) value).floatValue();
                            String fieldName = field.getName();
                            if (fieldName.equals("carFee") || fieldName.equals("bikeFee") || fieldName.equals("scooterFee") && floatValue < 0) {
                                throw new BadRequestException(fieldName + " cannot be negative.");
                            }
                            value = floatValue;
                        } catch (BadRequestException e) {
                            field.setAccessible(false);
                            throw new BadRequestBodyException(field.getName() + " cannot be negative.");
                        }
                    }

                    ReflectionUtils.setField(field, entity, value);
                    field.setAccessible(false);
                }
            }
        });
        return entity;
    }
}
