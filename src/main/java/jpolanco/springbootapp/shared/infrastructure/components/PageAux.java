package jpolanco.springbootapp.shared.infrastructure.components;

import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public class PageAux {
    public static Sort resolveSort(String sortBy, String sortOrder) {
        if (sortBy.equalsIgnoreCase("none")) {
            return Sort.unsorted();
        }
        return sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(Sort.Direction.ASC, sortBy) :
                Sort.by(Sort.Direction.DESC, sortBy);
    }

    public static String encodeCursor(Instant date, UUID id) {
        String raw = date + ":" + id;
        return Base64.getEncoder().encodeToString(raw.getBytes());
    }

    public static CursorData decodeCursor(String cursor) {
        String decoded = new String(Base64.getDecoder().decode(cursor));
        String[] parts = decoded.split(":");
        Instant date = Instant.parse(parts[0]);
        UUID id = UUID.fromString(parts[1]);
        return new CursorData(date, id);
    }

    public record CursorData(Instant date, UUID id) {}
}
