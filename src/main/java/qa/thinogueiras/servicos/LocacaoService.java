package qa.thinogueiras.servicos;

import static qa.thinogueiras.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import qa.thinogueiras.entidades.Filme;
import qa.thinogueiras.entidades.Locacao;
import qa.thinogueiras.entidades.Usuario;
import qa.thinogueiras.exceptions.LocadoraException;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws LocadoraException {		
		if(usuario == null) {
			throw new LocadoraException("Usuário vazio");
		}		
		
		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Sem filmes");
		}
		
		for(Filme filme: filmes) {
			if(filme.getEstoque() == 0) {
				throw new LocadoraException("Estoque vazio");
			}			
		}
		
		for(Filme filme: filmes) {
			if(filme.getPrecoLocacao() == 0) {
				throw new LocadoraException("Filme sem preço");
			}			
		}
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotal = 0d;
		for(Filme filme: filmes) {
			valorTotal += filme.getPrecoLocacao();			
		}
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);		
		
		return locacao;		
	}	
}