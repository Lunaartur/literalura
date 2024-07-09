package br.com.alura.literAlura.repository;

import br.com.alura.literAlura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface livroRepository extends JpaRepository<Livro, Long> {



    @Query("SELECT l.title, l.downloadCount, l.idioma, a.name  FROM Livro l JOIN l.autores a")
    List<Object[]> listarNomesEDownloadCountComAutores();


    @Query("Select l.title, l.downloadCount, l.idioma, a.name From Livro l JOIN l.autores a WHERE l.idioma LIKE %:idioma% ")
    List<Object[]> listarLivrosPeloIdioma( String idioma);

}
