package jpolanco.springbootapp.event.infrastructure.adapters.output.repository;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface JpaEventRepository extends JpaRepository<EventEntity, UUID> {

    @Query("SELECT COUNT(e) > 0 FROM EventEntity e WHERE e.schedule = ?1 AND e.durationInSeconds = ?2")
    boolean existEventWithSameSchedule(Instant date, long duration);

    @Query("SELECT COUNT(e) > 0 FROM EventEntity e WHERE e.location.latitude = ?1 AND e.location.longitude = ?2 AND e.preferences.modality != 'VIRTUAL'")
    boolean existEventWithSameLocationAndIsNotVirtual(double latitude, double longitude);

    @Query("SELECT e FROM EventEntity e WHERE e.creator.id = ?1 AND e.schedule = ?2")
     List<EventEntity> findByCreatorIdAndSchedule(UUID creatorId, Instant date);

    List<EventEntity> findByCreator_Id(UUID creatorId);

    void deleteByIdIsAndCreatorId(UUID id, UUID creatorId);

    List<EventEntity> findByCreatorId(UUID creatorId);


}
