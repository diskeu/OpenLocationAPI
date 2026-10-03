package dev.timjelenz.openlocationapi.dto.responses;

import java.util.List;

import org.springframework.data.domain.Slice;

/**
 * Stable representation of a slice of results.
 *
 * @param <T> type of the contained elements
 */
public record SliceResponse<T>(
    List<T> content,
    int page,
    int size,
    boolean hasNext
) {
    /**
     * Maps a slice to its response.
     *
     * @param slice the slice to map
     * @param <T> type of the contained elements
     * @return the response representation of the slice
     */
    public static <T> SliceResponse<T> from(final Slice<T> slice) {
        return new SliceResponse<>(
            slice.getContent(),
            slice.getNumber(),
            slice.getSize(),
            slice.hasNext()
        );
    }
}
