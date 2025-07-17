package com.hadef.movieslist.service;

import com.hadef.movieslist.domain.dto.MovieDTO;
import com.hadef.movieslist.domain.dto.MoviePageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MovieService {
    MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) throws IOException;
    MovieDTO getMovieById(UUID id);
    List<MovieDTO> getAllMovies();
    MovieDTO updateMovie(UUID id, MovieDTO movieDTO, MultipartFile file) throws IOException;
    String deleteMovie(UUID id) throws IOException;
    MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize);

    MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize,
                                                           String sortBy, String dir);
}
