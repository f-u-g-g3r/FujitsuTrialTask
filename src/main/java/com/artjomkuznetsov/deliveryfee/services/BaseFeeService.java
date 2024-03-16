package com.artjomkuznetsov.deliveryfee.services;


import com.artjomkuznetsov.deliveryfee.assemblers.RegionalBaseFeeModelAssembler;
import com.artjomkuznetsov.deliveryfee.controllers.BaseFeeController;
import com.artjomkuznetsov.deliveryfee.controllers.exceptions.regionalBaseFeeNotFound.RegionalBaseFeeNotFoundException;
import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.repositories.RegionalBaseFeeRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public CollectionModel<EntityModel<RegionalBaseFee>> getAllBaseFees() {
        List<EntityModel<RegionalBaseFee>> baseFees = repository.findAll().stream()
                .map(baseFee -> EntityModel.of(baseFee,
                        linkTo(methodOn(BaseFeeController.class).oneByCity(baseFee.getCity())).withSelfRel(),
                        linkTo(methodOn(BaseFeeController.class).all()).withRel("regionalBaseFees")))
                .toList();

        return CollectionModel.of(baseFees, linkTo(methodOn(BaseFeeController.class).all()).withSelfRel());
    }

    public EntityModel<RegionalBaseFee> getOneByCity(String city) {
        RegionalBaseFee baseFee = repository.findByCity(city)
                .orElseThrow(() -> new RegionalBaseFeeNotFoundException(city));

        return EntityModel.of(baseFee,
                linkTo(methodOn(BaseFeeController.class).oneByCity(city)).withSelfRel(),
                linkTo(methodOn(BaseFeeController.class).all()).withRel("regionalBaseFees"));
    }

    public ResponseEntity<?> updateBaseFee(RegionalBaseFee newData, String city) {
        RegionalBaseFee updatedBaseFee = repository.findByCity(city)
                .map((baseFee -> {
                    if (newData.getCarFee() != 0) baseFee.setCarFee(newData.getCarFee());
                    if (newData.getBikeFee() != 0) baseFee.setBikeFee(newData.getBikeFee());
                    if (newData.getScooterFee() != 0) baseFee.setScooterFee(newData.getScooterFee());
                    return repository.save(baseFee);
                }))
                .orElseThrow(() -> new RegionalBaseFeeNotFoundException(city));
        EntityModel<RegionalBaseFee> entityModel = assembler.toModel(updatedBaseFee);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);

    }
}
