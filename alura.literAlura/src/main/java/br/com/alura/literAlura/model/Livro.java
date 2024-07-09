package br.com.alura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("title")
    private String title;

    @JsonAlias("authors")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id"))
    private List<Autor> autores = new ArrayList<>();

    @ElementCollection
    @JsonAlias("languages")
    private List<String> languages = new ArrayList<>();

    @Column
    private String idioma; // Campo para armazenar o idioma do livro

    @JsonAlias("download_count")
    private int downloadCount;

    private Long idAutor;

    public Livro() {
    }

    public Livro(DadosLivros dados) {
        if (dados != null && dados.resultado() != null && !dados.resultado().isEmpty()) {
            // Considerando apenas o primeiro livro encontrado nos dados
            Livro primeiroLivro = dados.resultado().get(0);

            this.title = primeiroLivro.getTitle();
            this.languages = convertToIdiomas(primeiroLivro.getLanguages());
            this.downloadCount = primeiroLivro.getDownloadCount();
            this.autores = primeiroLivro.getAutores()
                    .stream()
                    .map(autor -> new Autor(autor.getName(), autor.getBirthYear(), autor.getDeathYear()))
                    .collect(Collectors.toList());

            // Definindo o idioma com base no primeiro idioma da lista convertida
            if (!this.languages.isEmpty()) {
                this.idioma = this.languages.get(0);
            }
        }
    }

    private List<String> convertToIdiomas(List<String> codigosIdiomas) {
        List<String> idiomas = new ArrayList<>();
        for (String codigo : codigosIdiomas) {
            Idiomas idiomaEnum = Idiomas.fromString(Arrays.asList(codigo)).get(0);
            idiomas.add(idiomaEnum.getNome());
        }
        return idiomas;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
        // Definindo o idioma com base no primeiro idioma da lista convertida
        if (languages != null && !languages.isEmpty()) {
            this.idioma = languages.get(0);
        }
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", languages=" + languages +
                ", idioma='" + idioma + '\'' +
                ", downloadCount=" + downloadCount +
                ", autores=" + autores.stream().map(Autor::getName).collect(Collectors.toList()) +
                '}';
    }
}
