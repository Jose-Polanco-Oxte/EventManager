package jpolanco.springbootapp.event.infrastructure.adapters.output.repository;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.EventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    Slice<EventEntity> findByCreator_Id(UUID creatorId, Pageable pageable);

    @Query("""
        SELECT e FROM EventEntity e
        WHERE (e.createdAt > :cursorDate)
           OR (e.createdAt = :cursorDate AND e.id > :cursorId)
        ORDER BY e.createdAt ASC, e.id ASC
    """)
    Slice<EventEntity> findByCreator_Id(
            @Param("cursorDate") Instant date,
            @Param("cursorId") UUID cursorId,
            @Param("creatorId") UUID creatorId,
            Pageable pageable
    );

    @Query("""
            SELECT e FROM EventEntity e
            WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :name, '%'))
            ORDER BY e.title ASC
    """)
    List<EventEntity> searchByName(String name, Pageable pageable);

    @Query("""
            SELECT e FROM EventEntity e
            WHERE e.preferences.isPublic = true AND LOWER(e.title) LIKE LOWER(CONCAT('%', :name, '%'))
            ORDER BY e.title ASC
    """)
    List<EventEntity> searchPublicByName(String name, Pageable pageable);

    @Query("""
        SELECT e FROM EventEntity e
        WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :name, '%')) AND e.creator.id = :creatorId
        ORDER BY e.title ASC
    """)
    List<EventEntity> searchMyEventsByName(String name, UUID creatorId, Pageable pageable);

    void deleteByIdIsAndCreatorId(UUID id, UUID creatorId);

    List<EventEntity> findByCreatorId(UUID creatorId);

    @Query("""
        SELECT e FROM EventEntity e
        WHERE e.preferences.isPublic = true
        ORDER BY e.createdAt DESC, e.id DESC
    """)
    Slice<EventEntity> findByPreferencesPublic(Pageable pageable);

    @Query("""
        SELECT e FROM EventEntity e
        WHERE (e.createdAt > :cursorDate)
           OR (e.createdAt = :cursorDate AND e.id > :cursorId)
           AND e.preferences.isPublic = true
        ORDER BY e.createdAt ASC, e.id ASC
    """)
    Slice<EventEntity> findByPreferencesPublic(
            @Param("cursorDate") Instant date,
            @Param("cursorId") UUID cursorId,
            Pageable pageable
    );

    @Query("""
        SELECT e FROM EventEntity e
        WHERE (e.createdAt > :cursorDate)
           OR (e.createdAt = :cursorDate AND e.id > :cursorId)
        ORDER BY e.createdAt ASC, e.id ASC
    """)
    Slice<EventEntity> findAll(
            @Param("cursorDate") Instant date,
            @Param("cursorId") UUID cursorId,
            Pageable pageable
    );
}
