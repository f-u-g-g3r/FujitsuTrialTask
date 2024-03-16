package com.artjomkuznetsov.deliveryfee.services;


import com.artjomkuznetsov.deliveryfee.utils.Patcher;
import com.artjomkuznetsov.deliveryfee.assemblers.RegionalBaseFeeModelAssembler;
import com.artjomkuznetsov.deliveryfee.controllers.BaseFeeController;
import com.artjomkuznetsov.deliveryfee.exceptions.RegionalBaseFeeNotFoundException;
import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.repositories.RegionalBaseFeeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
     *
     * @return
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
     *
     * @param city
     * @return
     */
    public EntityModel<RegionalBaseFee> getOneByCity(String city) {
        RegionalBaseFee baseFee = repository.findByCity(city)
                .orElseThrow(() -> new RegionalBaseFeeNotFoundException(city));

        return EntityModel.of(baseFee,
                linkTo(methodOn(BaseFeeController.class).oneByCity(city)).withSelfRel(),
                linkTo(methodOn(BaseFeeController.class).all()).withRel("regionalBaseFees"));
    }

    /**
     *
     * @param fields
     * @param city
     * @return
     */
    public ResponseEntity<?> updateBaseFee(Map<String, Object> fields, String city) {
        RegionalBaseFee updatedBaseFee = repository.findByCity(city)
                .orElseThrow(() -> new RegionalBaseFeeNotFoundException(city));

        repository.save(Patcher.patch(updatedBaseFee, fields));
        EntityModel<RegionalBaseFee> entityModel = assembler.toModel(updatedBaseFee);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);

    }
}
