package qa.thinogueiras.matchers;

import static qa.thinogueiras.utils.DataUtils.isMesmaData;
import static qa.thinogueiras.utils.DataUtils.obterDataComDiferencaDias;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

	private Integer qtdeDias;
	
	public DataDiferencaDiasMatcher(Integer qtdeDias) {
		this.qtdeDias = qtdeDias;
	}
	
	public void describeTo(Description desc) {
		Date dataEsperada = obterDataComDiferencaDias(qtdeDias);
		DateFormat format = new SimpleDateFormat("dd/MM/YYYY");
		desc.appendText(format.format(dataEsperada));
	}

	@Override
	protected boolean matchesSafely(Date data) {
		return isMesmaData(data, obterDataComDiferencaDias(qtdeDias));
	}

}
