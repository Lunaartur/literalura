package br.com.alura.literAlura;

import br.com.alura.literAlura.autorRepo.autorRepository;
import br.com.alura.literAlura.principal.Principal;
import br.com.alura.literAlura.repository.livroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {


	@Autowired
	private livroRepository repositorio;

	@Autowired
	private autorRepository repoAutor;

	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorio, repoAutor);
		principal.exibeMenu();
	}
}
