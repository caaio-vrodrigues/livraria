package caio.portfolio.livraria.service.author;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
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
	
	private static final String EXPECTED_NAME = "Caio Vinicius Rodrigues";
	private static final String EXPECTED_ALIAS = "Caio VR";
	private static final LocalDate EXPECTED_BIRTHDAY = LocalDate.of(1992, 03, 20);
	
	private static final Country BRAZIL = Country.builder()
		.isoAlpha2Code("br")	
		.build();
	
	private static final Author CAIO_VR = Author.builder()
		.alias("Caio VR")
		.fullName("Caio Vinicius Rodrigues")
		.birthday(LocalDate.of(1992, 03, 20))
		.country(BRAZIL)
		.build();
	
	@Test
	@DisplayName("Deve converter 'Author' para 'ResponseAuthorDTO' e retorna-lo")
	void toResponseAuthorDTO_returnsResponseAuthorDTO() {
		ResponseAuthorDTO responseAuthorDTO = responseAuthorDTOCreatorImpl.toResponseAuthorDTO(CAIO_VR);
		Assertions.assertNotNull(responseAuthorDTO);
		Assertions.assertEquals(EXPECTED_NAME, responseAuthorDTO.getFullName());
		Assertions.assertEquals(EXPECTED_ALIAS, responseAuthorDTO.getAlias());
		Assertions.assertEquals(EXPECTED_BIRTHDAY, responseAuthorDTO.getBirthday());
	}
}
