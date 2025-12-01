package caio.portfolio.livraria.service.publisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import caio.portfolio.livraria.exception.custom.publisher.PublisherAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.CreatePublisherDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.ResponsePublisherDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.UpdatePublisherDTO;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class PublisherServiceIntegrationTest {

	@Autowired private PublisherService service;
	
	private static final Long ROCCO_ID = 1L;
	private static final String ROCCO_NAME = "Rocco";
	private static final String GLOBAL_BOOKS_NAME = "Global Books Inc.";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_ISO_CODE = "BR";
	private static final String ROCCO_FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final String ROCCO_NEW_FULL_ADDRESS = "Rua do Passeio, 888, 8º andar, no Passeio Corporate";
	private static final Integer BRAZIL_ID = 1;
	
	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.name(BRAZIL_NAME)
		.isoAlpha2Code(BRAZIL_ISO_CODE)
		.build(); 
	
	private static final Publisher ROCCO_PUBLISHER = Publisher.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.country(BRAZIL)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.build();
	
	private static final CreatePublisherDTO CREATE_ROCCO_DTO = CreatePublisherDTO.builder()
		.name(ROCCO_NAME)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.countryId(BRAZIL_ID)
		.build();
	
	private static final ResponsePublisherDTO RESPONSE_ROCCO_DTO = ResponsePublisherDTO.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.countryId(BRAZIL_ID)
		.build();
	
	private static final UpdatePublisherDTO UPDATE_ROCCO_DTO = UpdatePublisherDTO.builder()
		.name(ROCCO_NAME)
		.countryId(null)
		.fullAddress(ROCCO_NEW_FULL_ADDRESS)
		.build();

	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve receber um 'CreatePublisherDTO' e retornar um 'ResponsePublisherDTO' após criação")
	void createPublisher_returnsResponsePublisherDTO() {
		ResponsePublisherDTO responsePublisherDTO = service.createPublisher(CREATE_ROCCO_DTO);
		assertNotNull(responsePublisherDTO);
		assertEquals(
			RESPONSE_ROCCO_DTO.getId(), 
			responsePublisherDTO.getId());
		assertEquals(
			RESPONSE_ROCCO_DTO.getName(), 
			responsePublisherDTO.getName());
		assertEquals(
			RESPONSE_ROCCO_DTO.getFullAddress(), 
			responsePublisherDTO.getFullAddress());
		assertEquals(
			RESPONSE_ROCCO_DTO.getCountryId(), 
			responsePublisherDTO.getCountryId());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@DisplayName("Deve receber um 'CreatePublisherDTO' e lançar 'PublisherAlreadyExistsException' por 'fullAddress' já em uso")
	void createPublisher_throwsPublisherAlreadyExistsException() {
		assertThrows(
			PublisherAlreadyExistsException.class, 
			() -> service.createPublisher(CREATE_ROCCO_DTO));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@DisplayName("Deve retornar lista de 'ResponsePublisherDTO' ao chamar método")
	void getAllPublishers_returnsResponsePublisherDTOList() {
		List<ResponsePublisherDTO> responsePublisherDTOListResult = service.getAllResponsePublisherDTOs();
		assertEquals(2, responsePublisherDTOListResult.size());
		assertEquals(ROCCO_NAME, responsePublisherDTOListResult.get(0).getName());
		assertEquals(GLOBAL_BOOKS_NAME, responsePublisherDTOListResult.get(1).getName());
	}
	
	@Test
	@DisplayName("Deve retornar lista vazia ao chamar método")
	void getAllPublishers_returnsEmptyList() {
		List<ResponsePublisherDTO> responsePublisherDTOListResult = service.getAllResponsePublisherDTOs();
		assertEquals(0, responsePublisherDTOListResult.size());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@DisplayName("Deve receber 'fullAddress' para buscar editora e retornar 'ResponsePublisherDTO'")
	void getPublisherByFullAddress_returnsResponsePublisherDTO() {
		ResponsePublisherDTO responsePublisherDTO = service
			.getResponsePublisherDTOByFullAddress(ROCCO_FULL_ADDRESS);
		assertNotNull(responsePublisherDTO);
		assertEquals(
			ROCCO_PUBLISHER.getId(), 
			responsePublisherDTO.getId());
		assertEquals(
			ROCCO_PUBLISHER.getName(), 
			responsePublisherDTO.getName());
		assertEquals(
			ROCCO_PUBLISHER.getFullAddress(), 
			responsePublisherDTO.getFullAddress());
		assertEquals(
			ROCCO_PUBLISHER.getCountry().getId(), 
			responsePublisherDTO.getCountryId());
	}
	
	@Test
	@DisplayName("Deve receber 'fullAddress' para buscar editora e lançar 'PublisherNotFoundException' por não encontrar")
	void getPublisherByFullAddress_throwsPublisherNotFoundException() {
		assertThrows(
			PublisherNotFoundException.class,
			() -> service.getResponsePublisherDTOByFullAddress(ROCCO_FULL_ADDRESS));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@DisplayName("Deve receber 'id' para buscar editora e retornar 'ResponsePublisherDTO'")
	void getPublisherById_returnsResponsePublisherDTO() {
		ResponsePublisherDTO responsePublisherDTO = service
			.getResponsePublisherDTOById(ROCCO_ID);
		assertNotNull(responsePublisherDTO);
		assertEquals(
			ROCCO_PUBLISHER.getId(), 
			responsePublisherDTO.getId());
		assertEquals(
			ROCCO_PUBLISHER.getName(), 
			responsePublisherDTO.getName());
		assertEquals(
			ROCCO_PUBLISHER.getFullAddress(), 
			responsePublisherDTO.getFullAddress());
		assertEquals(
			ROCCO_PUBLISHER.getCountry().getId(), 
			responsePublisherDTO.getCountryId());
	}
	
	@Test
	@DisplayName("Deve receber 'id' para buscar editora e lançar 'PublisherNotFoundException' por não encontrar")
	void getPublisherById_throwsPublisherNotFoundException() {
		assertThrows(
			PublisherNotFoundException.class,
			() -> service.getResponsePublisherDTOById(ROCCO_ID));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@DisplayName("Deve receber 'id' de editora existente e 'UpdatePublisherDTO' para atualização e retorno de 'ResponsePublisherDTO'")
	void updatePublisher_returnsResponsePublisherDTO() {
		ResponsePublisherDTO responsePublisherDTO = service.updatePublisher(
			ROCCO_ID, 
			UPDATE_ROCCO_DTO);
		assertNotNull(responsePublisherDTO);
		assertEquals(
			ROCCO_PUBLISHER.getId(), 
			responsePublisherDTO.getId());
		assertEquals(
			ROCCO_PUBLISHER.getName(), 
			responsePublisherDTO.getName());
		assertEquals(
			ROCCO_NEW_FULL_ADDRESS, 
			responsePublisherDTO.getFullAddress());
		assertEquals(
			ROCCO_PUBLISHER.getCountry().getId(), 
			responsePublisherDTO.getCountryId());
	}
	
	@Test
	@DisplayName("Deve receber 'id' de editora não existente e lançar 'PublisherNotFoundException'")
	void updatePublisher_throwsPublisherNotFoundException() {
		assertThrows(
			PublisherNotFoundException.class, 
			() -> service.updatePublisher(ROCCO_ID, UPDATE_ROCCO_DTO));
	}
}
