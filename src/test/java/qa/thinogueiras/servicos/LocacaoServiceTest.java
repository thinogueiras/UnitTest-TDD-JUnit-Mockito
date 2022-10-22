package qa.thinogueiras.servicos;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import static qa.thinogueiras.utils.DataUtils.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import qa.thinogueiras.entidades.Filme;
import qa.thinogueiras.entidades.Locacao;
import qa.thinogueiras.entidades.Usuario;
import qa.thinogueiras.exceptions.LocadoraException;
import qa.thinogueiras.utils.DataUtils;

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
	@Disabled
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
		filmes = Arrays.asList(
				new Filme("Filme 1", 2, 5.0),
				new Filme("Filme 2", 1, 5.0),
				new Filme("Filme 3", 2, 5.0),
				new Filme("Filme 4", 2, 0.01));		
		
		service.alugarFilme(usuario, filmes);

		Double valorTotalAluguel = 0d;
		Integer qtdeFilmesAlugados = 0;

		for (Filme filme : filmes) {
			valorTotalAluguel += filme.getPrecoLocacao();
			qtdeFilmesAlugados += 1;
		}

		assertEquals(15.01, valorTotalAluguel);
		assertEquals(qtdeFilmesAlugados, filmes.toArray().length);
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
	public void nãoDeveAlugarSeUmDosFilmesEstiverSemPreço() throws LocadoraException {
		usuario = new Usuario("Usuario 1");		
		filmes = Arrays.asList(
				new Filme("Filme 1", 2, 5.0),
				new Filme("Filme 2", 1, 0.0));		
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
		filmes = Arrays.asList(
				new Filme("Filme 1", 2, 5.0),
				new Filme("Filme 2", 0, 5.0));		
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
	
	@Test
	public void deveObter25PorCentoDeDescontoQuandoAlugarTerceiroFilme() throws LocadoraException {
		usuario = new Usuario("Usuário 1");
		filmes = Arrays.asList(
				new Filme("Titanic", 6, 3.5),
				new Filme("CLick", 4, 3.5),
				new Filme("Um sonho de liberdade", 1, 5.0));
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		assertEquals(10.75, resultado.getValor());
	}
	
	@Test
	public void deveObter50PorCentoDeDescontoQuandoAlugarQuartoFilme() throws LocadoraException {
		usuario = new Usuario("Usuário 1");
		filmes = Arrays.asList(
				new Filme("Titanic", 6, 3.5),
				new Filme("CLick", 4, 3.5),
				new Filme("Um sonho de liberdade", 1, 5.0),
				new Filme("Um sonho possível", 8, 4.0));
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		assertEquals(12.75, resultado.getValor());
	}
	
	@Test
	public void deveObter75PorCentoDeDescontoQuandoAlugarQuintoFilme() throws LocadoraException {
		usuario = new Usuario("Usuário 1");
		filmes = Arrays.asList(
				new Filme("Titanic", 6, 3.5),
				new Filme("CLick", 4, 3.5),
				new Filme("Um sonho de liberdade", 1, 5.0),
				new Filme("Um sonho possível", 8, 4.0),
				new Filme("A espera de um milagre", 4, 3.0));
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		assertEquals(13.50, resultado.getValor());
	}
	
	@Test
	public void deveObter100PorCentoDeDescontoQuandoAlugarSextoFilme() throws LocadoraException {
		usuario = new Usuario("Usuário 1");
		filmes = Arrays.asList(
				new Filme("Titanic", 6, 3.5),
				new Filme("CLick", 4, 3.5),
				new Filme("Um sonho de liberdade", 1, 5.0),
				new Filme("Um sonho possível", 8, 4.0),
				new Filme("A espera de um milagre", 4, 3.0),
				new Filme("V de Vingança", 3, 4.0));
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		assertEquals(13.50, resultado.getValor());
	}
	
	@Test
	@Disabled
	public void nãoDeveDevolverFilmeNoDomingo() throws LocadoraException {
		usuario = new Usuario("Dominguinhos");
		filmes = Arrays.asList(
				new Filme("Avatar", 3, 4.0));
		
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		boolean segunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		
		assertTrue(segunda);
	}
}
