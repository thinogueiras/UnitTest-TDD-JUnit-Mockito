package qa.thinogueiras.servicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import qa.thinogueiras.dao.LocacaoDAO;
import qa.thinogueiras.entidades.Filme;
import qa.thinogueiras.entidades.Locacao;
import qa.thinogueiras.entidades.Usuario;
import qa.thinogueiras.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	
	@InjectMocks
	private LocacaoService service;	
	
	@Mock
	private LocacaoDAO dao;
	
	@Mock
	private SPCService spc;
	
	private Usuario usuario;
	
	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value = 1)
	public Double valorLocacao;
	
	@Parameter(value = 2)
	public String cenario;
	
	@Before
	public void setup() {
		openMocks(this);
	}
	
	private static Filme filme01 = new Filme("O jogo da imitação", 2, 3.5);
	private static Filme filme02 = new Filme("V de Vingança", 3, 3.5);
	private static Filme filme03 = new Filme("Um sonho de liberdade", 2, 5.0);
	private static Filme filme04 = new Filme("CLick", 4, 4.0);
	private static Filme filme05 = new Filme("Um sonho possível", 8, 3.0);
	private static Filme filme06 = new Filme("Gladiador", 2, 4.0);
	private static Filme filme07 = new Filme("Prenda-Me se For Capaz", 1, 5.0);
	
	@Parameters(name="{2}")
	public static Collection<Object[]> getParametros() {
		return Arrays.asList(new Object[][]{
			{Arrays.asList(filme01, filme02), 7.0, "2 Filmes: Sem desconto"},
			{Arrays.asList(filme01, filme02, filme03), 10.75, "3 Filmes: 25%"},
			{Arrays.asList(filme01, filme02, filme03, filme04), 12.75, "4 Filmes: 50%"},
			{Arrays.asList(filme01, filme02, filme03, filme04, filme05), 13.5, "5 Filmes: 75%"},
			{Arrays.asList(filme01, filme02, filme03, filme04, filme05, filme06), 13.5, "6 Filmes: 100%"},
			{Arrays.asList(filme01, filme02, filme03, filme04, filme05, filme06, filme07), 18.5, "7 Filmes: Sem desconto"}
		});
	}
	
	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws LocadoraException {
		usuario = new Usuario("Usuário 1");		
		Locacao resultado = service.alugarFilme(usuario, filmes);		
		assertEquals(valorLocacao, resultado.getValor());
	}
}
