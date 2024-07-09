Liter Alura
<div align="center">
  <img src="https://github.com/Lunaartur/alurahub/assets/125662966/bba0254d-adeb-466f-882c-538efd2e7a8b" alt="alurahub">
</div>
Projeto desenvolvido em Spring, onde consumimos a api gutendex, e realizamos operações com os livros que são retornados.

📋 Pré-requisitos
- Java JDK Versão 17.
- IntelliJ IDEA -> https://www.jetbrains.com/pt-br/idea/
- Postgree SQL instalado -> [https://www.mysql.com/downloads/](https://www.postgresql.org/)
- gutendex
🚀 Funcionamento

Na parte de código, o consumo da API só é realizada pela primeira operação, onde retorna alguns dados que são filtrados e salvos no banco de dados.
As demais operações utilizam-se do banco de dados para consulta, utilizando formato de Queries e consultas JPQL para retornar os dados.

Ao inciar o projeto, um menu se abrirá com as seguintes operações:
"""
                    1 - Buscar Livro
                    2 - Listar Livros Registrados
                    3 - Listar Autores Registrados
                    4 - Listar Autores em um Determinado Ano
                    5 - Listar Livros de por Idioma
                    0 - Sair                                 
                    
""";

Basta selecionar a operação desejada caso já possua um livro cadastrado.
Powered by Luna, Arthur

