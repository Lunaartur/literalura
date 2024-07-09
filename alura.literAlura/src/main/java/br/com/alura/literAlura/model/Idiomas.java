package br.com.alura.literAlura.model;

import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Idiomas {
    INGLES("en", "Inglês"),
    FRANCES("fr", "Francês"),
    ALEMAO("de", "Alemão"),
    ESPANHOL("es", "Espanhol"),
    ITALIANO("it", "Italiano"),
    HOLANDES("nl", "Holandês"),
    PORTUGUES("pt", "Português"),
    LATIM("la", "Latim"),
    GREGO("el", "Grego"),
    RUSSO("ru", "Russo"),
    PT("pt", "Português"); // Novo valor para aceitar o código "pt"

    private final String codigoGutendex;
    private final String nome;

    Idiomas(String codigoGutendex, String nome) {
        this.codigoGutendex = codigoGutendex;
        this.nome = nome;
    }

    public String getCodigoGutendex() {
        return codigoGutendex;
    }

    public static List<Idiomas> fromString(List<String> codigosGutendex) {
        List<Idiomas> idiomasEncontrados = new ArrayList<>();
        for (String codigo : codigosGutendex) {
            for (Idiomas idioma : Idiomas.values()) {
                if (idioma.getCodigoGutendex().equalsIgnoreCase(codigo)) {
                    idiomasEncontrados.add(idioma);
                    break; // Para encontrar apenas uma correspondência por código
                }
            }
        }
        if (idiomasEncontrados.isEmpty()) {
            throw new IllegalArgumentException("Nenhum idioma encontrado para os códigos fornecidos: " + codigosGutendex);
        }
        return idiomasEncontrados;
    }

    public String getNome() {
        return nome;
    }
}