package jpolanco.springbootapp.event.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.response.CursorPageResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.response.SlicePageResponse;

import java.util.List;

public interface OwnEventQueryService {
    SlicePageResponse<EventResponse> getEventsByPages(String creatorId, int page, int size, String sortBy, String sortOrder);
    CursorPageResponse<EventResponse, String> getEventsByCursorBased(String creatorId, String cursor, int size, String sortBy, String sortOrder);
    List<EventResponse> getEventsByCategory(String creatorId, String category, String modality, int page, int size);
    List<EventResponse> getEventsByTitle(String creatorId, String title, String modality, int page, int size);
    List<EventResponse> getEventsByLocation(String creatorId, String locationName, String modality, int page, int size);
}