package jpolanco.springbootapp.event.infrastructure.adapters.output.repository;

import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.EventEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface JpaEventRepository extends CrudRepository<EventEntity, UUID> {
}
