package caio.portfolio.livraria.service.country.model.validate;

public interface CountryNameValidator {
	String resolveNameByIsoAlpha2Code(String isoAlpha2Code);
	String getNameByValidatedAndNormalizedIsoAlpha2Code(String isoAlpha2Code);
}
