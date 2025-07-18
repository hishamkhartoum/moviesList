package com.hadef.movieslist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hadef.movieslist.domain.dto.MovieDTO;
import com.hadef.movieslist.domain.dto.MoviePageResponse;
import com.hadef.movieslist.exception.EmptyFileException;
import com.hadef.movieslist.service.impl.MovieServiceImpl;
import com.hadef.movieslist.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieServiceImpl movieServiceImpl;

    public MovieController(MovieServiceImpl movieServiceImpl) {
        this.movieServiceImpl = movieServiceImpl;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDTO> addMovieHandler(
            @RequestPart MultipartFile file,
            @RequestPart String movieDTO) throws IOException, EmptyFileException {
        if(file.isEmpty()) {
            throw new EmptyFileException("File is empty");
        }
        MovieDTO dto = convertToMovieDto(movieDTO);
        return new ResponseEntity<>(movieServiceImpl.addMovie(dto,file), HttpStatus.CREATED);
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDTO> updateMovieHandler(
            @PathVariable UUID movieId,
            @RequestPart MultipartFile file,
            @RequestPart String movieDTO) throws IOException {

        if(file.isEmpty()) file=null;
        MovieDTO dto = convertToMovieDto(movieDTO);
        return ResponseEntity.ok(movieServiceImpl.updateMovie(movieId,dto,file));
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable UUID movieId) throws IOException {
        return ResponseEntity.ok(movieServiceImpl.deleteMovie(movieId));
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDTO> getMovieHandler(@PathVariable UUID movieId) {
        return ResponseEntity.ok(movieServiceImpl.getMovieById(movieId));
    }



    @GetMapping("/all")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        return ResponseEntity.ok(movieServiceImpl.getAllMovies());
    }

    @GetMapping("/allMoviesPage")
    public ResponseEntity<MoviePageResponse> getMoviesWithPagination(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(movieServiceImpl.getAllMoviesWithPagination(pageNumber,pageSize));
    }

    @GetMapping("/allMoviesPageSort")
    public ResponseEntity<MoviePageResponse> getMoviesWithPaginationAndSorting(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String dir
    ) {
        return ResponseEntity.ok(movieServiceImpl.getAllMoviesWithPaginationAndSorting(pageNumber,pageSize,sortBy,dir));
    }

    private MovieDTO convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDtoObj, MovieDTO.class);
    }


}
