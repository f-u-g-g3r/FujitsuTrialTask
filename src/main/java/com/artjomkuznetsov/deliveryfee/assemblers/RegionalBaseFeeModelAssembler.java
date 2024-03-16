package com.artjomkuznetsov.deliveryfee.assemblers;

import com.artjomkuznetsov.deliveryfee.controllers.BaseFeeController;
import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class RegionalBaseFeeModelAssembler implements RepresentationModelAssembler<RegionalBaseFee, EntityModel<RegionalBaseFee>> {

    @Override
    public EntityModel<RegionalBaseFee> toModel(@NonNull RegionalBaseFee baseFee) {

        return EntityModel.of(baseFee,
                linkTo(methodOn(BaseFeeController.class).oneByCity(baseFee.getCity()    )).withSelfRel(),
                linkTo(methodOn(BaseFeeController.class).all()).withRel("regionalBaseFees"));
    }
}
