package caio.portfolio.livraria.service.publisher.implementation.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import caio.portfolio.livraria.exception.custom.publisher.PublisherNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class PublisherFinderImplIntegrationTest {

	@Autowired private PublisherFinderImpl publisherFinderImpl;
	
	private static final Long ROCCO_ID = 1L;
	private static final String ROCCO_NAME = "Rocco";
	private static final String ROCCO_END = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@DisplayName("Deve buscar editora por 'fullAddres' e retorna-la")
	void findByFullAddress_returnPublisher() {
		Publisher rocco = publisherFinderImpl.findByFullAddress(ROCCO_END);
		assertNotNull(rocco);
		assertEquals(ROCCO_NAME, rocco.getName());
		assertEquals(ROCCO_END, rocco.getFullAddress());
	}
	
	@Test
	@DisplayName("Deve lançar 'PublisherNotFoundException' ao buscar editora por 'fullAddres'")
	void findByFullAddress_throwsPublisherNotFoundException() {
		assertThrows(
			PublisherNotFoundException.class,
			() -> publisherFinderImpl.findByFullAddress(ROCCO_END));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@DisplayName("Deve buscar editora por 'id' e retorna-la")
	void findById_returnPublisher() {
		Publisher rocco = publisherFinderImpl.findById(ROCCO_ID);
		assertNotNull(rocco);
		assertEquals(ROCCO_NAME, rocco.getName());
		assertEquals(ROCCO_END, rocco.getFullAddress());
	}
	
	@Test
	@DisplayName("Deve lançar 'PublisherNotFoundException' ao buscar editora por 'id'")
	void findById_throwsPublisherNotFoundException() {
		assertThrows(
			PublisherNotFoundException.class,
			() -> publisherFinderImpl.findById(ROCCO_ID));
	}
}