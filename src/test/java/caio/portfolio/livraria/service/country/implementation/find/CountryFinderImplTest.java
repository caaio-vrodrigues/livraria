package caio.portfolio.livraria.service.country.implementation.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import caio.portfolio.livraria.exception.custom.country.CountryNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;

@ExtendWith(MockitoExtension.class)
class CountryFinderImplTest {
	
	@InjectMocks CountryFinderImpl countryFinderImpl;
	@Mock private CountryRepository repo;
	@Mock private MessageSource countryMessageSource;
	
	private static final String BRAZIL_CODE = "BR";
	private static final String COUNTRY_NOT_FOUND_ISO_MSG = "Não foi possível encontrar país com o `isoAlpha2Code`: `"+BRAZIL_CODE+"`";
	private static final String BRAZIL_NAME = "Brazil";
	private static final Integer BRAZIL_ID = 1;
	
	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.isoAlpha2Code(BRAZIL_CODE)
		.name(BRAZIL_NAME)
		.build();
	
	@Test
	@DisplayName("Deve buscar país por código iso e retorná-lo")
	void findByIsoAlpha2Code_returnsCountry() {
		when(countryMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(COUNTRY_NOT_FOUND_ISO_MSG);
		when(repo.findByIsoAlpha2Code(anyString()))
			.thenReturn(Optional.of(BRAZIL));
		Country brazil = countryFinderImpl.findByIsoAlpha2Code(BRAZIL_CODE);
		assertNotNull(brazil);
		assertEquals(BRAZIL_NAME, brazil.getName());
		assertEquals(BRAZIL_CODE, brazil.getIsoAlpha2Code());
		verify(countryMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class),
			any(Locale.class));
		verify(repo, times(1)).findByIsoAlpha2Code(anyString());
	}
	
	@Test
	@DisplayName("Deve lançar 'CountryNotFoundException' ao buscar país por código iso")
	void findByIsoAlpha2Code_throwsCountryNotFoundException() {
		when(countryMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(COUNTRY_NOT_FOUND_ISO_MSG);
		when(repo.findByIsoAlpha2Code(anyString()))
			.thenReturn(Optional.empty());
		assertThrows(
			CountryNotFoundException.class,
			() -> countryFinderImpl.findByIsoAlpha2Code(BRAZIL_CODE));
		verify(countryMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class),
			any(Locale.class));
		verify(repo, times(1)).findByIsoAlpha2Code(anyString());
	}
	
	@Test
	@DisplayName("Deve buscar país por 'id' e retorná-lo")
	void findById_returnsCountry() {
		when(countryMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(COUNTRY_NOT_FOUND_ISO_MSG);
		when(repo.findById(anyInt()))
			.thenReturn(Optional.of(BRAZIL));
		Country brazil = countryFinderImpl.findById(BRAZIL_ID);
		assertNotNull(brazil);
		assertEquals(BRAZIL_NAME, brazil.getName());
		assertEquals(BRAZIL_CODE, brazil.getIsoAlpha2Code());
		verify(countryMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class),
			any(Locale.class));
		verify(repo, times(1)).findById(anyInt());
	}
	
	@Test
	@DisplayName("Deve lançar 'CountryNotFoundException' ao buscar país por 'id'")
	void findById_throwsCountryNotFoundException() {
		when(countryMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(COUNTRY_NOT_FOUND_ISO_MSG);
		when(repo.findById(anyInt()))
			.thenReturn(Optional.empty());
		assertThrows(
			CountryNotFoundException.class,
			() -> countryFinderImpl.findById(BRAZIL_ID));
		verify(countryMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class),
			any(Locale.class));
		verify(repo, times(1)).findById(anyInt());
	}
}