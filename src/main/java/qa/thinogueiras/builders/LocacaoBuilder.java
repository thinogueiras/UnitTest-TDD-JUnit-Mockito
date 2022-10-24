package qa.thinogueiras.builders;

import static qa.thinogueiras.utils.DataUtils.obterDataComDiferencaDias;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import qa.thinogueiras.entidades.Filme;
import qa.thinogueiras.entidades.Locacao;
import qa.thinogueiras.entidades.Usuario;

public class LocacaoBuilder {
	
	private Locacao locacao;
	public static Usuario usuario = new Usuario("Usuário padrão");
	private static List<Filme> filmes = Arrays.asList(new Filme("Lagoa azul", 1, 5.0));
	
	private LocacaoBuilder() {}
	
	public static LocacaoBuilder obterLocacao() {
		LocacaoBuilder builder = new LocacaoBuilder();
		builder.locacao = new Locacao();		
		builder.locacao.setUsuario(usuario);
		builder.locacao.setFilmes(filmes);
		builder.locacao.setDataLocacao(new Date());
		builder.locacao.setDataRetorno(obterDataComDiferencaDias(1));
		builder.locacao.setValor(5.0);		
		
		return builder;
	}
	
	public LocacaoBuilder comDataLocacao(Date data) {
		locacao.setDataLocacao(data);
		return this;
	}
	
	public LocacaoBuilder comDataRetorno(Date data) {
		locacao.setDataRetorno(data);
		return this;
	}
	
	public LocacaoBuilder atrasada() {
		locacao.setDataLocacao(obterDataComDiferencaDias(-4));
		locacao.setDataRetorno(obterDataComDiferencaDias(-2));
		return this;
	}
	
	public LocacaoBuilder comUsuario(Usuario usuario) {
		locacao.setUsuario(usuario);		
		return this;
	}
	
	public LocacaoBuilder comListaFilmes(Filme... filmes) {
		locacao.setFilmes(Arrays.asList(filmes));
		return this;
	}
	
	public LocacaoBuilder comValor(Double valor) {
		locacao.setValor(valor);
		return this;
	}
	
	public Locacao agora() {
		return locacao;
	}
}
