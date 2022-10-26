package qa.thinogueiras.servicos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static qa.thinogueiras.builders.LocacaoBuilder.obterLocacao;
import static qa.thinogueiras.matchers.Matchers.caiNumaSegunda;
import static qa.thinogueiras.matchers.Matchers.hoje;
import static qa.thinogueiras.matchers.Matchers.hojeComDiferencaDias;
import static qa.thinogueiras.utils.DataUtils.verificarDiaSemana;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import qa.thinogueiras.builders.LocacaoBuilder;
import qa.thinogueiras.dao.LocacaoDAO;
import qa.thinogueiras.entidades.Filme;
import qa.thinogueiras.entidades.Locacao;
import qa.thinogueiras.entidades.Usuario;
import qa.thinogueiras.exceptions.LocadoraException;

public class LocacaoServiceTest {	
	
	private Locacao locacao;
	private List<Filme> filmes;
	private List<Locacao> locacoes;
	private Usuario usuario;
	
	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private LocacaoDAO dao;
	
	@Mock
	private SPCService spcService;	
	
	@Mock
	private EmailService emailService;

	@BeforeEach
	public void setup() {
		openMocks(this);		
	}

	@Test
	public void deveAlugarFilmeComSucesso() throws LocadoraException {		
		assumeFalse(verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		usuario = new Usuario("Usuario 1");
		filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		locacao = service.alugarFilme(usuario, filmes);
		
		assertEquals(5.0, locacao.getValor(), 0.01);
		assertThat(locacao.getDataLocacao(), hoje());
		assertThat(locacao.getDataRetorno(), hojeComDiferencaDias(1));
	}

	@Test
	public void deveAlugarMaisQueUmFilmeComSucesso() throws LocadoraException {
		usuario = new Usuario("Usuario 1");
		filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0), new Filme("Filme 2", 1, 5.0), new Filme("Filme 3", 2, 5.0),
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
		filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0), new Filme("Filme 2", 1, 0.0));
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
		filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0), new Filme("Filme 2", 0, 5.0));
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
	public void deveDevolverFilmeNaSegundaAoAlugarNoSabado() throws LocadoraException {
		assumeTrue(verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		usuario = new Usuario("Dominguinhos");
		filmes = Arrays.asList(new Filme("Avatar", 3, 4.0));

		Locacao retorno = service.alugarFilme(usuario, filmes);		

		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
	}

	@Test
	public void nãoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		usuario = new Usuario("Teste SPC");
		filmes = Arrays.asList(new Filme("Titanic", 2, 5.0));

		try {
			when(spcService.getStatusSPC(usuario)).thenReturn(true);
			service.alugarFilme(usuario, filmes);
			fail();
		} catch (LocadoraException e) {
			assertEquals("Usuário negativado", e.getMessage());
		}

		verify(spcService).getStatusSPC(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocaçõesAtrasadas() throws LocadoraException {
		Usuario usuario2 = new Usuario("Usuario atrasado");
		Usuario usuario3 = new Usuario("Usuario em dia");
		locacoes = Arrays.asList(
				obterLocacao().atrasada().agora(),
				obterLocacao().atrasada().comUsuario(usuario2).agora(),
				obterLocacao().comUsuario(usuario3).agora());
		
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);			
		service.notificarAtrasosNaDevolucao();		
						
		verify(emailService).notificarAtrasoLocacao(LocacaoBuilder.usuario);
		verify(emailService).notificarAtrasoLocacao(usuario2);
		verify(emailService, never()).notificarAtrasoLocacao(usuario3);
		verifyNoMoreInteractions(emailService);
	}
	
	@Test
	public void deveTratarErroNoSPC() throws Exception {
		usuario = new Usuario("Instabilidade SPC");
		filmes = Arrays.asList(new Filme("O Lobo de Wall Street", 3, 4.50));
		
		when(spcService.getStatusSPC(usuario)).thenThrow(new Exception("Falha na consulta"));
		
		try {
			service.alugarFilme(usuario, filmes);
			fail();
		} catch (LocadoraException e) {
			assertEquals("Erro na consulta ao SPC", e.getMessage());
		}		
	}
	
	@Test
	public void deveProrrogarUmaLocação() {
		locacao = obterLocacao().agora();
		
		service.prorrogarLocacao(locacao, 3);
		
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		
		verify(dao).salvar(argCapt.capture());
		Locacao locacaoCapturada = argCapt.getValue();
		
		assertEquals(15.0, locacaoCapturada.getValor());		
		assertThat(locacaoCapturada.getDataLocacao(), hoje());
		assertThat(locacaoCapturada.getDataRetorno(), hojeComDiferencaDias(3));
	}
}
