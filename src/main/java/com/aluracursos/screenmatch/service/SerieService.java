package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.EpisodioDTO;
import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obtenerTodasLasSeries() {
        return convertirDatos(repository.findAll());
    }

    public List<SerieDTO> obtenerTop5() {
        return convertirDatos(repository.findTop5ByOrderByEvaluacionDesc());
    }

    public List<SerieDTO> obtenerLanzamientosMasRecientes() {
        return convertirDatos(repository.lanzamientosMasRecientes());
    }

    public List<SerieDTO> convertirDatos(List<Serie> serie) {
        return serie.stream().
                map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(),
                        s.getPoster(), s.getGenero(), s.getActores(), s.getSinopsis())).
                collect(Collectors.toList());
    }

    public SerieDTO obtenerPorId(Long id) {
        Optional<Serie> serie = repository.findById(id);
        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(),
                    s.getPoster(), s.getGenero(), s.getActores(), s.getSinopsis());
        }
        return null;
    }

    public List<EpisodioDTO> obtenerTodasLasTemporadas(Long id) {
        Optional<Serie> serie = repository.findById(id);
        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream().map(e -> new EpisodioDTO(e.getTemporada(),e.getTitulo(),
                            e.getNumeroEpisodio())).collect(Collectors.toList());
        }
        return null;
    }

    public List<SerieDTO> obtenerSeriesPorCategoria(String nombreGenero) {
        Categoria categoria = Categoria.fromEspanol(nombreGenero);
        return convertirDatos(repository.findByGenero(categoria));
    }

    public List<EpisodioDTO> ontenerTemporadasPorNumero(Long id, Long numeroTemporada) {
        return repository.obtenerTemporadasPorNumero(id, numeroTemporada).stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(),
                        e.getNumeroEpisodio())).collect(Collectors.toList());

    }
}

