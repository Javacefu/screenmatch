package com.aluracursos.screenmatch;

import com.aluracursos.screenmatch.model.DatosEpisodio;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		Crear variable para conectar API
		var consumoAPI = new ConsumoAPI();

//		Conectar la URL completa y se cargaran los datos en consola
		var json = consumoAPI.obtenerDatos("https://www.omdbapi.com/?t=One+piece&apikey=7a422cf3");
//		var json = consumoAPI.obtenerDatos("https://coffee.alexflipnote.dev/random.json");

//		Imprime informacion del json con URL
		System.out.println(json);

//		Se crea conversor y se imprimen los datos de forma ordenada
		ConvierteDatos conversor = new ConvierteDatos();
		var datos = conversor.obtenerDatos(json, DatosSerie.class);
		System.out.println(datos);

//		Conectar API | Se obtienen datos de la API y se transforman con clase DatosEpisodio | Imprime resultado
//		Se busca misma serie pero se especifica temporada y capitulo
		json = consumoAPI.obtenerDatos("https://www.omdbapi.com/?t=One+piece&apikey=7a422cf3&season=1&Episode=1");
		DatosEpisodio episodios = conversor.obtenerDatos(json, DatosEpisodio.class);
		System.out.println(episodios);

		List<DatosTemporadas> temporadas = new ArrayList<>();
		for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
			json = consumoAPI.obtenerDatos("https://www.omdbapi.com/?t=One+piece&Season="+i+"&apikey=7a422cf3");
			var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
			temporadas.add(datosTemporadas);
		}
		temporadas.forEach(System.out::println);
	}
}
