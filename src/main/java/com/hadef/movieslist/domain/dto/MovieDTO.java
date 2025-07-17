package com.hadef.movieslist.domain.dto;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {

    private UUID id;

    @NotBlank(message = "PLease provide movie's title")
    private String title;

    @NotBlank(message = "PLease provide movie's studio")
    private String studio;

    @NotBlank(message = "PLease provide movie's director")
    private String director;
    private Set<String> movieCast;
    private Integer releaseYear;

    @NotBlank(message = "PLease provide movie's poster url")
    private String posterUrl;

    @NotBlank(message = "PLease provide movie's poster")
    private String poster;
}
