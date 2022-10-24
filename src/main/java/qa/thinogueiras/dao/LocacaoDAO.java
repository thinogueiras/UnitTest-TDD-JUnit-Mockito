package qa.thinogueiras.dao;

import java.util.List;

import qa.thinogueiras.entidades.Locacao;

public interface LocacaoDAO {

	public void salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendentes();
		
}
