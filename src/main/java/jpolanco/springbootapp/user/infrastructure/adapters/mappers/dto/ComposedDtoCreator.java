package jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto;

import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.DtoCreator;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.EntityCollection;

public interface ComposedDtoCreator<Entity,
        Input extends EntityCollection,
        EntityDto extends Dto,
        Creator extends DtoCreator<Entity, EntityDto>,
        Response extends Dto> {
     Response create(Input input, Creator creator);
}