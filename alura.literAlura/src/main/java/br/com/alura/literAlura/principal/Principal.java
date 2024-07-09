package br.com.alura.literAlura.principal;

import br.com.alura.literAlura.autorRepo.autorRepository;
import br.com.alura.literAlura.model.Autor;
import br.com.alura.literAlura.model.DadosLivros;
import br.com.alura.literAlura.model.Idiomas;
import br.com.alura.literAlura.model.Livro;
import br.com.alura.literAlura.repository.livroRepository;
import br.com.alura.literAlura.service.ConsumoApi;
import br.com.alura.literAlura.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "http://gutendex.com/books/?search=";
    private livroRepository repositorio;
    private autorRepository repositorioAutor;

    private List<Livro> livros = new ArrayList<>();

    public Principal(livroRepository repositorio, autorRepository repositorioAutor) {
        this.repositorio = repositorio;
        this.repositorioAutor = repositorioAutor;
    }



    //Redefinir database e adicionar livros para teste

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar Livro
                    2 - Listar Livros Registrados
                    3 - Listar Autores Registrados
                    4 - Listar Autores em um Determinado Ano
                    5 - Listar Livros de por Idioma
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            try {
                opcao = leitura.nextInt();
                leitura.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Por favor, insira um número válido.");
                leitura.nextLine(); // Limpa o buffer do scanner
                continue;
            }
            switch (opcao) {
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    System.out.println("Informe o ano para busca: ");
                    Integer anoBusca = leitura.nextInt();
                    leitura.nextLine();
                    listarAutoresVivos(anoBusca);
                    break;
                case 5:
                    System.out.println("Escolha o número correspondente ao idioma desejado: ");
                    listarLivrosIdioma();
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }




    ///Exercício 1
    private void buscarLivro() {
        DadosLivros dados = getDadosLivros();
        if (dados != null && !dados.resultado().isEmpty()) {
            Livro primeiroLivro = dados.resultado().get(0);

            List<Autor> autoresAtualizados = new ArrayList<>();
            for (Autor autor : primeiroLivro.getAutores()) {
                Optional<Autor> autoresEncontrados = repositorioAutor.procurarAutor(autor.getName());
                if (autoresEncontrados.isPresent()) {
                    Autor autorBD = autoresEncontrados.get();
                    autoresAtualizados.add(autorBD);
                } else {
                    autor.getLivros().add(primeiroLivro);
                    autoresAtualizados.add(autor);
                }
            }

            primeiroLivro.setAutores(autoresAtualizados);
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


    //Exercício 2
    private void listarLivros() {
        List<Object[]> resultados = repositorio.listarNomesEDownloadCountComAutores();

        if (resultados != null) {
            resultados.forEach(resultado -> {
                if (resultado != null && resultado.length >= 4) {
                    String nomeLivro = (String) resultado[0];
                    Long downloadCount = ((Number) resultado[1]).longValue();
                    String idioma = (String) resultado[2];
                    String nomeAutor = (String) resultado[3];
                    //Ajustar para receber idioma
                    System.out.println("Livro: " + nomeLivro);
                    System.out.println("Nome Autor: " + nomeAutor);
                    System.out.println("Idioma: " + idioma);
                    System.out.println("Número de Downloads: " + downloadCount);
                    System.out.println();
                }
            });
        } else {
            System.out.println("A consulta não retornou resultados.");
        }
    }

    //Exercício 3 Funcinando corretamente
    private void listarAutoresRegistrados() {
        List<Object[]> resultados = repositorioAutor.listarAutores();

        if (resultados != null) {
            // Lista para armazenar os autores já processados
            List<String> autoresProcessados = new ArrayList<>();

            for (Object[] resultado : resultados) {
                if (resultado != null && resultado.length == 4) {
                    String autorName = (String) resultado[0];
                    Integer birthYear = resultado[1] != null ? Integer.parseInt(resultado[1].toString()) : null;
                    Integer deathYear = resultado[2] != null ? Integer.parseInt(resultado[2].toString()) : null;

                    // Verifica se o autor já foi processado
                    if (!autoresProcessados.contains(autorName)) {
                        System.out.println("Nome do autor: " + autorName);
                        System.out.println("Ano Nascimento: " + birthYear);
                        System.out.println("Ano Falicimento: " + deathYear);
                        System.out.println("Livros:");
                        ///Mostrando os livros
                        resultados.stream()
                                .filter(r -> r[0].equals(autorName))
                                .forEach(r -> System.out.println(" - " + r[3]));
                        autoresProcessados.add(autorName);
                        System.out.println();
                    }
                }
            }
        } else {
            System.out.println("A consulta não retornou resultados.");
        }
    }




    //Exercício 4
    private void listarAutoresVivos(Integer ano) {
        List<Object[]> resultados = repositorioAutor.listarAutores();

        List<String> autoresProcessados = new ArrayList<>();

        resultados.forEach(resultado -> {
            String autorName = (String) resultado[0];
            Integer birthYear = resultado[1] != null ? Integer.parseInt((String) resultado[1]) : null;
            Integer deathYear = resultado[2] != null ? Integer.parseInt((String) resultado[2]) : null;

            if (!autoresProcessados.contains(autorName) && birthYear < ano && (deathYear == null || deathYear > ano)) {
                System.out.println();
                System.out.println("Nome do autor: " + autorName);
                System.out.println("Ano Nascimento: " + birthYear);
                System.out.println("Ano Falecimento: " + (deathYear != null ? deathYear : "Ainda vivo"));
                System.out.println("Livros:");
                // Lista os títulos dos livros do autor
                resultados.stream()
                        .filter(r -> r[0].equals(autorName))
                        .forEach(r -> System.out.println(" - " + r[3]));
                autoresProcessados.add(autorName);
            }
        });
    }



    private void listarLivrosIdioma() {
        String idiomaUsuario = listarIdiomas();
        List<Object[]> livros = repositorio.listarLivrosPeloIdioma(idiomaUsuario);
        if (livros == null){
            System.out.println("Nenhum livro com esse idioma cadastrado!");
        }
        if (livros != null) {
            livros.forEach(resultado -> {
                if (resultado != null && resultado.length >= 4) {
                    String nomeLivro = (String) resultado[0];
                    Long downloadCount = ((Number) resultado[1]).longValue();
                    String idioma = (String) resultado[2];
                    String nomeAutor = (String) resultado[3];
                    //Ajustar para receber idioma
                    System.out.println("Livro: " + nomeLivro);
                    System.out.println("Nome Autor: " + nomeAutor);
                    System.out.println("Idioma: " + idioma);
                    System.out.println("Número de Downloads: " + downloadCount);
                    System.out.println();
                }
            });
        } else {
            System.out.println("A consulta não retornou resultados.");
        }

    }


    //Método exercício 5.2
    private String listarIdiomas() {
        Integer retorno = null;
        System.out.println("Selecione o idioma:");
        System.out.println("1 - en");
        System.out.println("2 - fr");
        System.out.println("3 - de");
        System.out.println("4 - es");
        System.out.println("5 - it");
        System.out.println("6 - nl");
        System.out.println("7 - pt");
        System.out.println("8 - la");
        System.out.println("9 - el");
        System.out.println("10 - ru");

        while (retorno == null) {
            try {
                String entrada = leitura.nextLine();
                retorno = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número de 1 a 10.");
            }
        }

        switch (retorno) {
            case 1:
                return "en";
            case 2:
                return "fr";
            case 3:
                return "de";
            case 4:
                return "es";
            case 5:
                return "it";
            case 6:
                return "nl";
            case 7:
                return "pt";
            case 8:
                return "la";
            case 9:
                return "el";
            case 10:
                return "ru";
            default:
                return null;
        }
    }

}
