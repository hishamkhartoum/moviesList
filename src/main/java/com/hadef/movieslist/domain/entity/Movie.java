package com.hadef.movieslist.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "PLease provide movie's title")
    private String title;
    @Column(nullable = false)
    @NotBlank(message = "PLease provide movie's studio")
    private String studio;
    @Column(nullable = false)
    @NotBlank(message = "PLease provide movie's director")
    private String director;
    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;
    private Integer releaseYear;
    @Column(nullable = false)
    @NotBlank(message = "PLease provide movie's poster")
    private String poster;
}
