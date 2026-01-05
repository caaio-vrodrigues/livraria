package caio.portfolio.livraria.service.publisher.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.ResponsePublisherDTO;
import caio.portfolio.livraria.service.publisher.implementation.create.ResponsePublisherDTOCreatorImpl;

@ExtendWith(MockitoExtension.class)
class ResponsePublisherDTOCreatorImplTest {

	@InjectMocks ResponsePublisherDTOCreatorImpl responsePublisherDTOCreatorImpl;
	
	private static final Long ROCCO_ID = 1L;
	private static final String ROCCO_NAME = "Rocco";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_ISO_CODE = "BR";
	private static final String FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final Integer BRAZIL_ID = 2;
	
	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.name(BRAZIL_NAME)
		.isoAlpha2Code(BRAZIL_ISO_CODE)
		.build(); 
	
	private static final Publisher ROCCO_PUBLISHER = Publisher.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.country(BRAZIL)
		.fullAddress(FULL_ADDRESS)
		.build() ; 
	
	@Test
	@DisplayName("Deve receber um 'Publisher' e retornar 'ResponsePublisherDTO' após conversão")
	void toResponsePublisherDTO_returnsResponsePublisherDTO() {
		ResponsePublisherDTO responsePublisherDTO = responsePublisherDTOCreatorImpl
			.toResponsePublisherDTO(ROCCO_PUBLISHER);
		assertNotNull(responsePublisherDTO);
		assertEquals(
			ROCCO_PUBLISHER.getId(), 
			responsePublisherDTO.getId()
		);
		assertEquals(
			ROCCO_PUBLISHER.getName(), 
			responsePublisherDTO.getName()
		);
		assertEquals(
			ROCCO_PUBLISHER.getFullAddress(), 
			responsePublisherDTO.getFullAddress()
		);
		assertEquals(
			ROCCO_PUBLISHER.getCountry().getId(), 
			responsePublisherDTO.getCountryId()
		);
	}
}
