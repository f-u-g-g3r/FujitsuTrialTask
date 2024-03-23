package com.artjomkuznetsov.deliveryfee.services;


import com.artjomkuznetsov.deliveryfee.exceptions.BadRequestBodyException;
import com.artjomkuznetsov.deliveryfee.utils.Updater;
import com.artjomkuznetsov.deliveryfee.assemblers.RegionalBaseFeeModelAssembler;
import com.artjomkuznetsov.deliveryfee.controllers.RegionalBaseFeeController;
import com.artjomkuznetsov.deliveryfee.exceptions.RegionalBaseFeeNotFoundException;
import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.repositories.RegionalBaseFeeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BaseFeeService {
    private static final List<String> NON_NEGATIVE_FIELDS = List.of("carFee", "bikeFee", "scooterFee");

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
                        linkTo(methodOn(RegionalBaseFeeController.class).oneByCity(baseFee.getCity())).withSelfRel(),
                        linkTo(methodOn(RegionalBaseFeeController.class).all()).withRel("regionalBaseFees")))
                .toList();

        return CollectionModel.of(baseFees, linkTo(methodOn(RegionalBaseFeeController.class).all()).withSelfRel());
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
                linkTo(methodOn(RegionalBaseFeeController.class).oneByCity(city)).withSelfRel(),
                linkTo(methodOn(RegionalBaseFeeController.class).all()).withRel("regionalBaseFees"));
    }

    /**
     * Updates the RegionalBaseFee for the specified city with the provided fields.
     * If no RegionalBaseFee is found for the specified city, a RegionalBaseFeeNotFoundException is thrown.
     *
     * @param fields A Map containing the fields to update and their new values.
     * @param city The name of the city for which to update the RegionalBaseFee.
     * @return EntityModel containing the updated RegionalBaseFee with links to the corresponding operations.
     * @throws RegionalBaseFeeNotFoundException if no RegionalBaseFee is found for the specified city.
     * @throws BadRequestBodyException If the updated float value of a field in {@code NON_NEGATIVE_FIELDS} is negative.
     * @throws BadRequestBodyException If the value of a field is not a number or not compatible with float type.
     */
    public EntityModel<RegionalBaseFee> updateBaseFee(Map<String, Object> fields, String city) {
        RegionalBaseFee updatedBaseFee = repository.findByCity(city)
                .orElseThrow(() -> new RegionalBaseFeeNotFoundException(city));

        repository.save(Updater.updateEntity(updatedBaseFee, fields, NON_NEGATIVE_FIELDS));
        return assembler.toModel(updatedBaseFee);
    }
}
