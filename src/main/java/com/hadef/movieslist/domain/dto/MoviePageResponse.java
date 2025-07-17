package com.hadef.movieslist.domain.dto;

import java.util.List;

public record MoviePageResponse(
        List<MovieDTO> movieDTOList,
        Integer PageNumber,
        long totalElement,
        int totalPages,
        boolean isLast
) {
}
