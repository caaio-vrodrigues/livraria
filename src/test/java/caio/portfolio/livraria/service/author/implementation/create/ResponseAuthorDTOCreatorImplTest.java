package caio.portfolio.livraria.service.author.implementation.create;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.country.Country;

@ExtendWith(MockitoExtension.class)
class ResponseAuthorDTOCreatorImplTest {
	
	@InjectMocks private ResponseAuthorDTOCreatorImpl responseAuthorDTOCreatorImpl;
	
	private static final String BRAZIL_CODE = "BR";
	private static final String CAIO_ALIAS = "CVR";
	private static final String CAIO_FULL_NAME = "Caio Vinicius Rodrigues";
	private static final LocalDate CAIO_BIRTHDAY = LocalDate.of(1992, 03, 20);
	
	private static final Country BRAZIL = Country.builder()
		.isoAlpha2Code(BRAZIL_CODE)	
		.build();
	
	private static final Author CAIO_VR = Author.builder()
		.alias(CAIO_ALIAS)
		.fullName(CAIO_FULL_NAME)
		.birthday(CAIO_BIRTHDAY)
		.country(BRAZIL)
		.build();
	
	@Test
	@DisplayName("Deve converter 'Author' para 'ResponseAuthorDTO' e retorna-lo")
	void toResponseAuthorDTO_returnsResponseAuthorDTO() {
		ResponseAuthorDTO responseAuthorDTO = responseAuthorDTOCreatorImpl
			.toResponseAuthorDTO(CAIO_VR);
		assertNotNull(responseAuthorDTO);
		assertEquals(
			CAIO_FULL_NAME, 
			responseAuthorDTO.getFullName());
		assertEquals(
			CAIO_ALIAS, 
			responseAuthorDTO.getAlias());
		assertEquals(
			CAIO_BIRTHDAY, 
			responseAuthorDTO.getBirthday());
	}
}
