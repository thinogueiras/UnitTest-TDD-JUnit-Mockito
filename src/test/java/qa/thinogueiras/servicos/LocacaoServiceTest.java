package qa.thinogueiras.servicos;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import static qa.thinogueiras.utils.DataUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import qa.thinogueiras.entidades.Filme;
import qa.thinogueiras.entidades.Locacao;
import qa.thinogueiras.entidades.Usuario;
import qa.thinogueiras.exceptions.LocadoraException;

public class LocacaoServiceTest {

	private LocacaoService service;
	private Locacao locacao;
	private List<Filme> filmes;
	private Usuario usuario;

	@BeforeEach
	public void setup() {
		service = new LocacaoService();
	}

	@Test
	public void deveAlugarFilmeComSucesso() throws Exception {
		usuario = new Usuario("Usuario 1");
		filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		locacao = service.alugarFilme(usuario, filmes);
		assertEquals(5.0, locacao.getValor(), 0.01);
		assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
		assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));
	}

	@Test
	public void deveAlugarMaisQueUmFilmeComSucesso() throws LocadoraException {
		usuario = new Usuario("Usuario 1");
		Filme filme01 = new Filme("Filme 1", 2, 5.0);
		Filme filme02 = new Filme("Filme 2", 1, 5.0);

		ArrayList<Filme> filmes = new ArrayList<>();
		filmes.add(filme01);
		filmes.add(filme02);

		service.alugarFilme(usuario, filmes);

		Double valorTotalAluguel = 0d;
		Integer qtdeFilmesAlugados = 0;

		for (Filme filme : filmes) {
			valorTotalAluguel += filme.getPrecoLocacao();
			qtdeFilmesAlugados += 1;
		}

		assertEquals(10.0, valorTotalAluguel);
		assertEquals(2, filmes.toArray().length);
	}
	
	@Test
	public void nãoDeveAlugarUmFilmeSemPreço() throws LocadoraException {
		usuario = new Usuario("Usuario 1");
		filmes = Arrays.asList(new Filme("Filme 1", 2, 0.0));

		try {
			service.alugarFilme(usuario, filmes);
			fail();
		} catch (LocadoraException e) {
			assertEquals(("Filme sem preço"), e.getMessage());
		}
	}
	
	@Test
	public void nãoDeveAlugarUmDosFilmesSemPreço() throws LocadoraException {
		usuario = new Usuario("Usuario 1");
		Filme filme01 = new Filme("Filme 1", 2, 5.0);
		Filme filme02 = new Filme("Filme 2", 1, 0.0);
		ArrayList<Filme> filmes = new ArrayList<>();
		filmes.add(filme01);
		filmes.add(filme02);
		
		try {			
			service.alugarFilme(usuario, filmes);
			fail();
		} catch (LocadoraException e) {
			assertEquals(("Filme sem preço"), e.getMessage());
		}
	}

	@Test
	public void nãoDeveAlugarSemEstoque() throws LocadoraException {
		usuario = new Usuario("Usuario 1");
		filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

		try {
			service.alugarFilme(usuario, filmes);
			fail();
		} catch (LocadoraException e) {
			assertEquals(("Estoque vazio"), e.getMessage());
		}
	}
	
	@Test
	public void nãoDeveAlugarUmDosFilmesSemEstoque() throws LocadoraException {
		usuario = new Usuario("Usuario 1");
		Filme filme01 = new Filme("Filme 1", 2, 5.0);
		Filme filme02 = new Filme("Filme 2", 0, 5.0);
		ArrayList<Filme> filmes = new ArrayList<>();
		filmes.add(filme01);
		filmes.add(filme02);
		
		try {			
			service.alugarFilme(usuario, filmes);
			fail();
		} catch (LocadoraException e) {
			assertEquals(("Estoque vazio"), e.getMessage());
		}
	}

	@Test
	public void nãoDeveAlugarSemFilme() throws LocadoraException {
		usuario = new Usuario("Usuario 1");

		try {
			service.alugarFilme(usuario, null);
			fail();
		} catch (LocadoraException e) {
			assertEquals(("Sem filmes"), e.getMessage());
		}
	}

	@Test
	public void nãoDeveAlugarSemUsuário() throws LocadoraException {
		filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		try {
			service.alugarFilme(null, filmes);
			fail();
		} catch (LocadoraException e) {
			assertEquals(("Usuário vazio"), e.getMessage());
		}
	}
}
