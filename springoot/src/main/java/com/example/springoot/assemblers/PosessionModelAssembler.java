package com.example.springoot.assemblers;

import com.example.springoot.controllers.APIController;
import com.example.springoot.models.Posessions;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PosessionModelAssembler implements RepresentationModelAssembler<Posessions, EntityModel<Posessions>> {

    @Override
    public EntityModel<Posessions> toModel(Posessions entity) {
        return EntityModel.of(entity,
                WebMvcLinkBuilder.linkTo(methodOn(APIController.class).userPosessions(entity.getUser().getUsername())).withSelfRel());
    }
}
