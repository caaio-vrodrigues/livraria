package caio.portfolio.livraria.service.country;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.exception.custom.CountryNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.country.dto.CreateCountryDTO;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
import caio.portfolio.livraria.service.country.dto.CountryResultImplDTO;
import caio.portfolio.livraria.service.country.model.CountryValidator;
import caio.portfolio.livraria.service.country.model.ResponseCountryDTOCreator;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

	@Mock private ResponseCountryDTOCreator responseCountryDTOCreator;
	@Mock private CountryRepository repo;
	@Mock private CountryValidator countryValidator; 
	@InjectMocks private CountryService service;

	String rawBrazilCode = "br";
	String validBrazilCode = "BR";
	String validCountryName = "Brazil";
	
	Country brazil = Country.builder()
        .id(1).isoAlpha2Code("BR").name("Brazil").build();
	
	ResponseCountryDTO responseBrazilDTO = ResponseCountryDTO.builder()
        .id(1).isoAlpha2Code("BR").name("Brazil").build();
	
	Country ireland = Country.builder()
	    .id(2).isoAlpha2Code("IR").name("Ireland").build();
		
	ResponseCountryDTO responseIrelandDTO = ResponseCountryDTO.builder()
        .id(2).isoAlpha2Code("IR").name("Ireland").build();
	
	CreateCountryDTO createBrazilDTO = CreateCountryDTO.builder()
	    .isoAlpha2Code(rawBrazilCode)
	    .build();
	
	List<Country> mockCountries = List.of(brazil, ireland);
	
	 @Test
	 @DisplayName("Deve retornar país ao buscar com 'id' existente")
	 void getCountryById_returnsCountry() {
		 Mockito.when(repo.findById(1)).thenReturn(Optional.of(brazil));
		 Mockito.when(responseCountryDTOCreator.toResponseCountryDTO(brazil))
	     	.thenReturn(responseBrazilDTO);
		 ResponseCountryDTO result = service.getCountryById(1);
	        Assertions.assertNotNull(result);
	        Assertions.assertEquals(1, result.getId());
	 }
	 
	 @Test
	 @DisplayName("Deve retornar 'CountryNotFoundException' ao buscar com 'id' não existente")
	 void getCountryById_returnsException() {
		 Mockito.when(repo.findById(100)).thenReturn(Optional.empty());
		 Assertions.assertThrows(
            CountryNotFoundException.class,
            () -> service.getCountryById(100)
        );
	 }
	 
	 @Test
	 @DisplayName("Deve retornar país ao buscar com 'isoAlpha2Code' existente")
	 void getCountryByIsoAlpha2Code_returnsCountry() {
		 Mockito.when(countryValidator.processIsoAlpha2Code(rawBrazilCode)).thenReturn(validBrazilCode);
		 Mockito.when(repo.findByIsoAlpha2Code(validBrazilCode)).thenReturn(Optional.of(brazil));
		 Mockito.when(responseCountryDTOCreator.toResponseCountryDTO(brazil)).thenReturn(responseBrazilDTO);
		 ResponseCountryDTO result = service.getCountryByIsoAlpha2Code(rawBrazilCode);
		 Assertions.assertEquals("BR", result.getIsoAlpha2Code());
		 Assertions.assertEquals("Brazil", result.getName());
	 }
	 
	 @Test
	 @DisplayName("Deve retornar 'CountryNotFoundException' ao buscar com 'isoAlpha2Code' não existente")
	 void getCountryByIsoAlpha2Code_usesOriginalCodeInErrorMessage() {
	     Mockito.when(countryValidator.processIsoAlpha2Code(rawBrazilCode)).thenReturn(validBrazilCode);
	     Mockito.when(repo.findByIsoAlpha2Code(validBrazilCode)).thenReturn(Optional.empty());
	     Assertions.assertThrows(
             CountryNotFoundException.class,
             () -> service.getCountryByIsoAlpha2Code(rawBrazilCode)
         );
	 }
	 
	 @Test
	 @DisplayName("Deve retornar lista de dto's de países quando existem países cadastrados")
	 void getAllCountries_returnsResponseCountryDTOList() {
		 Mockito.when(repo.findAll()).thenReturn(mockCountries);
		 Mockito.when(responseCountryDTOCreator.toResponseCountryDTO(brazil))
	     	.thenReturn(responseBrazilDTO);
		 Mockito.when(responseCountryDTOCreator.toResponseCountryDTO(ireland))
	        .thenReturn(responseIrelandDTO);
		 List<ResponseCountryDTO> result = service.getAllCountries();
		 Assertions.assertNotNull(result);
		 Assertions.assertEquals(2, result.size());
		 Assertions.assertEquals("BR", result.get(0).getIsoAlpha2Code());
		 Assertions.assertEquals("IR", result.get(1).getIsoAlpha2Code());
	 }
	 
	 @Test
	 @DisplayName("Deve retornar lista vazia quando não existem países cadastrados")
	 void getAllCountries_returnsEmptyList() {
		 Mockito.when(repo.findAll()).thenReturn(List.of());
		 List<ResponseCountryDTO> result = service.getAllCountries();
		 Assertions.assertNotNull(result);
		 Assertions.assertTrue(result.isEmpty());
		 Assertions.assertEquals(0, result.size());
	 }
	 
	 @Test
	 @DisplayName("Deve retornar país existente quando 'isoAlpha2Code' já está cadastrado")
	 void createOrFindCountry_returnsExistingCountry() {
	     Mockito.when(countryValidator.processIsoAlpha2Code(rawBrazilCode))
	         .thenReturn(validBrazilCode);
	     Mockito.when(repo.findByIsoAlpha2Code(validBrazilCode))
	         .thenReturn(Optional.of(brazil));
	     Mockito.when(responseCountryDTOCreator.toResponseCountryDTO(brazil))
	         .thenReturn(responseBrazilDTO);
	     CountryResultImplDTO result = service.createOrFindCountry(createBrazilDTO);
	     Assertions.assertNotNull(result);
	     Assertions.assertEquals("BR", result.getCountry().getIsoAlpha2Code());
	     Assertions.assertEquals("Brazil", result.getCountry().getName());
	     Assertions.assertFalse(result.isCreated());
	 }
	 
	 @Test
	 @DisplayName("Deve criar novo país quando 'isoAlpha2Code' não existe")
	 void createOrFindCountry_createsNewCountry() {
	     Mockito.when(countryValidator.processIsoAlpha2Code(rawBrazilCode))
	         .thenReturn(validBrazilCode);
	     Mockito.when(repo.findByIsoAlpha2Code(validBrazilCode))
	         .thenReturn(Optional.empty());
	     Mockito.when(countryValidator.getNameByValidatedAndNormalizedIsoAlpha2Code(validBrazilCode))
	         .thenReturn(validCountryName);
	     Mockito.when(responseCountryDTOCreator.toResponseCountryDTO(Mockito.any(Country.class)))
	         .thenReturn(responseBrazilDTO);
	     CountryResultImplDTO result = service.createOrFindCountry(createBrazilDTO);
	     Assertions.assertNotNull(result);
	     Assertions.assertEquals("BR", result.getCountry().getIsoAlpha2Code());
	     Assertions.assertEquals("Brazil", result.getCountry().getName());
	     Assertions.assertTrue(result.isCreated());
	     Mockito.verify(repo, Mockito.times(1)).saveAndFlush(Mockito.any(Country.class));
	 }
	 
	 @Test
	 @DisplayName("Não deve salvar quando país já existe")
	 void createOrFindCountry_doesNotSaveWhenCountryExists() {
	     Mockito.when(countryValidator.processIsoAlpha2Code(rawBrazilCode))
	         .thenReturn(validBrazilCode);
	     Mockito.when(repo.findByIsoAlpha2Code(validBrazilCode))
	         .thenReturn(Optional.of(brazil));
	     Mockito.when(responseCountryDTOCreator.toResponseCountryDTO(brazil))
	         .thenReturn(responseBrazilDTO);
	     service.createOrFindCountry(createBrazilDTO);
	     Mockito.verify(repo, Mockito.never()).saveAndFlush(Mockito.any(Country.class));
	 }
}
