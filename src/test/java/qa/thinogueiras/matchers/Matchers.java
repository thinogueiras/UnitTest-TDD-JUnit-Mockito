package qa.thinogueiras.matchers;

import java.util.Calendar;

public class Matchers {
	
	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}
	
	public static DataDiferencaDiasMatcher hojeComDiferencaDias(Integer qtdeDias) {
		return new DataDiferencaDiasMatcher(qtdeDias);
	}
	
	public static DataDiferencaDiasMatcher hoje() {
		return new DataDiferencaDiasMatcher(0);
	}	
}
