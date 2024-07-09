package br.com.alura.literAlura.PrincipalTeste;

import br.com.alura.literAlura.model.Autor;
import br.com.alura.literAlura.model.DadosLivros;
import br.com.alura.literAlura.model.Livro;
import br.com.alura.literAlura.repository.livroRepository;
import br.com.alura.literAlura.service.ConsumoApi;
import br.com.alura.literAlura.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "http://gutendex.com/books/?search=";
    private livroRepository repositorio;

    private List<Livro> livros = new ArrayList<>();

    public Principal(livroRepository repositorio){
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {

            var menu = """
                    1 - Buscar Livro
                    2 - Listar Livros Registrados
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();
            switch (opcao) {
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarLivro() {
        DadosLivros dados = getDadosLivros();
        if (dados != null && !dados.resultado().isEmpty()) {
            Livro primeiroLivro = dados.resultado().get(0);

            for (Autor autor : primeiroLivro.getAutores()) {
                autor.getLivros().add(primeiroLivro);
            }

            repositorio.save(primeiroLivro);

            System.out.println("*** Apresentando as informações do livro: ");
            System.out.println("Título: " + primeiroLivro.getTitle() +
                    "\nAutores: " + primeiroLivro.getAutores().stream().map(Autor::getName).collect(Collectors.toList()) +
                    "\nIdiomas: " + primeiroLivro.getLanguages() +
                    "\nNúmero de downloads: " + primeiroLivro.getDownloadCount());
        } else {
            System.out.println("Informe um livro válido!");
        }
    }

    private DadosLivros getDadosLivros() {
        try {
            System.out.println("Informe o nome do livro: ");
            var nomeLivro = leitura.nextLine().trim();
            if (nomeLivro.isEmpty()) {
                System.out.println("O nome do livro não pode estar vazio.");
                return getDadosLivros();
            }

            var json1 = consumoApi.obterDados(ENDERECO + nomeLivro.replace(" ", "%20"));
            System.out.println("Dados recebidos da API: " + json1);

            DadosLivros dadosLivros = conversor.obterDados(json1, DadosLivros.class);
            return dadosLivros;
        } catch (Exception e) {
            System.err.println("Erro ao obter dados do livro: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void listarLivrosRegistrados() {
        livros = repositorio.findAll();
        livros.forEach(System.out::println);
    }
}