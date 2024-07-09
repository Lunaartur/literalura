package br.com.alura.literAlura.autorRepo;

import br.com.alura.literAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface autorRepository extends JpaRepository<Autor, Long> {


    //Query teste do meu exercicio 1
//    @Query("SELECT a.name, a.birthYear, a.deathYear From Autor a")
//    List<Object[]> listarAutores();

    @Query("SELECT a.name, a.birthYear, a.deathYear, l.title FROM Autor a JOIN a.livros l")
    List<Object[]> listarAutores();



    //Query do meu exercício 4
    @Query("SELECT a.name, a.birthYear, a.deathYear From Autor a Where a.birthYear < :anoDescrito AND a.deathYear > :anoDescrito")
    List<Object[]> listarAutoresVivos(@Param("anoDescrito") Integer anoDescrito);


    //Queries do meu exercício 1
    @Query("SELECT a FROM Autor a WHERE a.name LIKE %:nome%")
    Optional<Autor> procurarAutor(@Param("nome") String nome);

    @Query("SELECT a FROM Autor a WHERE a.name LIKE %:nome%")
    List<Autor> dadosAutor(@Param("nome") String nome);


}
