package caio.portfolio.livraria.service.country.implementation.save;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import caio.portfolio.livraria.exception.custom.country.ConcurrentCountryException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
import caio.portfolio.livraria.service.country.model.create.CountryExceptionCreator;

@ExtendWith(MockitoExtension.class)
class CountrySaverAndConcurrencyHandleImplTest {

	@InjectMocks private CountrySaverAndConcurrencyHandleImpl countrySaverAndConcurrencyHandleImpl;
	@Mock private CountryRepository repo;
	@Mock private CountryExceptionCreator countryExceptionCreator;
	
	private static final int BRAZIL_ID = 1;
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	
	private static final Country BRAZIL = Country.builder()
        .id(BRAZIL_ID)
        .isoAlpha2Code(BRAZIL_CODE)
        .name(BRAZIL_NAME)
        .build();
	
	private static final ConcurrentCountryException EXCEPTION = 
		new ConcurrentCountryException("Falha ao tentar criar país com `isoAlpha2Code`: `"+BRAZIL_CODE+"`");
	
	@Test
	@DisplayName("Deve salvar país com sucesso tratando cenário de concorrência")
	void saveAndDealingConcurrency_returnsCountry() {
		when(repo.saveAndFlush(any(Country.class)))
			.thenReturn(BRAZIL);
		Country brazil = countrySaverAndConcurrencyHandleImpl
			.saveAndDealingConcurrency(BRAZIL);
		assertNotNull(brazil);
		assertEquals(BRAZIL_NAME, brazil.getName());
		verify(repo, times(1)).saveAndFlush(any(Country.class));
	}
	
	@Test
	@DisplayName("Deve retornar país correto após falha de concorrência")
	void saveAndDealingConcurrency_returnsCountryAfterFail() {
		when(repo.saveAndFlush(any(Country.class)))
			.thenThrow(DataIntegrityViolationException.class);
		when(repo.findByIsoAlpha2Code(anyString()))
			.thenReturn(Optional.of(BRAZIL));
		Country brazil = countrySaverAndConcurrencyHandleImpl
			.saveAndDealingConcurrency(BRAZIL);
		assertNotNull(brazil);
		assertEquals(BRAZIL_NAME, brazil.getName());
		verify(repo, times(1)).saveAndFlush(any(Country.class));
		verify(repo, times(1)).findByIsoAlpha2Code(anyString());
	}
	
	@Test
	@DisplayName("Deve lançar 'ConcurrentCountryException' ao tentar salvar país")
	void saveAndDealingConcurrency_throwsException() {
		when(repo.saveAndFlush(any(Country.class)))
			.thenThrow(DataIntegrityViolationException.class);
		when(repo.findByIsoAlpha2Code(anyString()))
			.thenReturn(Optional.empty());
		when(countryExceptionCreator
				.createConcurrentCountryException(anyString()))
			.thenReturn(EXCEPTION);
		assertThrows(
			ConcurrentCountryException.class,
			() -> countrySaverAndConcurrencyHandleImpl
				.saveAndDealingConcurrency(BRAZIL));
		verify(repo, times(1)).saveAndFlush(any(Country.class));
		verify(repo, times(1)).findByIsoAlpha2Code(anyString());
		verify(countryExceptionCreator, times(1))
			.createConcurrentCountryException(anyString());
	}
}