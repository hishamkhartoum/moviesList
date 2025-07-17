package com.hadef.movieslist.service.impl;

import com.hadef.movieslist.domain.dto.MovieDTO;
import com.hadef.movieslist.domain.dto.MoviePageResponse;
import com.hadef.movieslist.domain.entity.Movie;
import com.hadef.movieslist.exception.FileExistsException;
import com.hadef.movieslist.exception.MovieNotFoundException;
import com.hadef.movieslist.repository.MovieRepository;
import com.hadef.movieslist.service.FileService;
import com.hadef.movieslist.service.MovieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;
    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }
    @Value("${project.poster}")
    private String path;
    @Value("${base.url}")
    private String baseUrl;
    @Override
    public MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) throws IOException {
        if (Files.exists(Path.of(path + File.separator + file.getOriginalFilename()))){
           throw new FileExistsException("File already exists with name " + file.getOriginalFilename());
        }
        String uploadedFileName = fileService.uploadFile(path,file);
        movieDTO.setPoster(uploadedFileName);
        Movie movie = Movie.builder()
                .id(null)
                .title(movieDTO.getTitle())
                .movieCast(movieDTO.getMovieCast())
                .director(movieDTO.getDirector())
                .studio(movieDTO.getStudio())
                .releaseYear(movieDTO.getReleaseYear())
                .poster(movieDTO.getPoster())
                .build();
        Movie savedMovie = movieRepository.save(movie);
        String posterUrl = baseUrl + "/file/" + uploadedFileName;
        return MovieDTO.builder()
                .id(savedMovie.getId())
                .title(savedMovie.getTitle())
                .studio(savedMovie.getStudio())
                .director(savedMovie.getDirector())
                .movieCast(savedMovie.getMovieCast())
                .releaseYear(savedMovie.getReleaseYear())
                .poster(savedMovie.getPoster())
                .posterUrl(posterUrl)
                .build();
    }

    @Override
    public MovieDTO getMovieById(UUID id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + id));
        String posterUrl = baseUrl + "/file/" + movie.getPoster();
        return MovieDTO.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .studio(movie.getStudio())
                .director(movie.getDirector())
                .movieCast(movie.getMovieCast())
                .releaseYear(movie.getReleaseYear())
                .poster(movie.getPoster())
                .posterUrl(posterUrl)
                .build();
    }

    @Override
    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        List<MovieDTO> movieDTOList = new ArrayList<>();
        for (Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDTO movieDTO = MovieDTO.builder()
                    .id(movie.getId())
                    .title(movie.getTitle())
                    .studio(movie.getStudio())
                    .director(movie.getDirector())
                    .movieCast(movie.getMovieCast())
                    .releaseYear(movie.getReleaseYear())
                    .poster(movie.getPoster())
                    .posterUrl(posterUrl)
                    .build();
            movieDTOList.add(movieDTO);
        }
        return movieDTOList;
    }

    @Override
    public MovieDTO updateMovie(UUID id, MovieDTO movieDTO, MultipartFile file) throws IOException {
        Movie mv = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + id));
        String fileName = mv.getPoster();
        if(file != null){
            Files.deleteIfExists(Path.of(path + File.separator + fileName));
            fileName = fileService.uploadFile(path,file);
        }
        movieDTO.setPoster(fileName);
        Movie movie = Movie.builder()
                .id(mv.getId())
                .title(movieDTO.getTitle())
                .studio(movieDTO.getStudio())
                .director(movieDTO.getDirector())
                .movieCast(movieDTO.getMovieCast())
                .releaseYear(movieDTO.getReleaseYear())
                .poster(movieDTO.getPoster())
                .build();
        Movie updatedMovie = movieRepository.save(movie);

        String posterUrl = baseUrl + "/file/" + fileName;

        MovieDTO rs = MovieDTO.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .studio(movieDTO.getStudio())
                .director(movieDTO.getDirector())
                .movieCast(movieDTO.getMovieCast())
                .releaseYear(movieDTO.getReleaseYear())
                .poster(movieDTO.getPoster())
                .posterUrl(posterUrl)
                .build();

        return rs;
    }

    @Override
    public String deleteMovie(UUID id) throws IOException{
        Movie mv = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id = " + id));
        UUID movieId = mv.getId();

        // 2. delete the file associated with this object
        Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));

        // 3. delete the movie object
        movieRepository.delete(mv);

        return "Movie deleted with id = " + movieId;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie>  movies = moviePages.getContent();
        List<MovieDTO> movieDTOList = new ArrayList<>();
        for (Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDTO movieDTO = MovieDTO.builder()
                    .id(movie.getId())
                    .title(movie.getTitle())
                    .studio(movie.getStudio())
                    .director(movie.getDirector())
                    .movieCast(movie.getMovieCast())
                    .releaseYear(movie.getReleaseYear())
                    .poster(movie.getPoster())
                    .posterUrl(posterUrl)
                    .build();
            movieDTOList.add(movieDTO);
        }
        return new MoviePageResponse(movieDTOList,pageNumber,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie>  movies = moviePages.getContent();
        List<MovieDTO> movieDTOList = new ArrayList<>();
        for (Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDTO movieDTO = MovieDTO.builder()
                    .id(movie.getId())
                    .title(movie.getTitle())
                    .studio(movie.getStudio())
                    .director(movie.getDirector())
                    .movieCast(movie.getMovieCast())
                    .releaseYear(movie.getReleaseYear())
                    .poster(movie.getPoster())
                    .posterUrl(posterUrl)
                    .build();
            movieDTOList.add(movieDTO);
        }
        return new MoviePageResponse(movieDTOList,pageNumber,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast());
    }
}
