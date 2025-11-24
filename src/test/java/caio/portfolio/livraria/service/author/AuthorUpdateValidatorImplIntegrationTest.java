package caio.portfolio.livraria.service.author;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.country.CountryNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;

@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace=Replace.ANY)
class AuthorUpdateValidatorImplIntegrationTest {
    
    @Autowired private AuthorUpdateValidatorImpl authorUpdateValidatorImpl;
    
    private static final String AUTHOR_ALIAS = "O Mago";
    private static final String EXISTING_ALIAS = "machado.ass";
    private static final String NEW_ALIAS = "CVR";
    private static final String BRAZIL_NAME = "Brazil";
    private static final String BRAZIL_CODE = "BR";
    private static final Integer BRAZIL_ID = 1;
    private static final Integer ITALY_ID = 2;
    private static final Integer NON_EXISTENT_COUNTRY_ID = 100;
    
    private static final Country BRAZIL = Country.builder()
        .id(BRAZIL_ID)
        .isoAlpha2Code(BRAZIL_CODE)
        .name(BRAZIL_NAME)
        .build();

    @Test
    @Sql("/sql/country/insert_brazil.sql")
    @Sql("/sql/author/insert_author_list.sql")
    @DisplayName("Deve validar 'alias' diferente do atual e retorna-lo se não houver conflito")
    void validateAlias_returnsNewAlias(){
        String updatedAlias = authorUpdateValidatorImpl.validateAlias(AUTHOR_ALIAS, NEW_ALIAS);
        assertNotNull(updatedAlias);
        assertEquals(NEW_ALIAS, updatedAlias);
    }
    
    @Test
    @Sql("/sql/country/insert_brazil.sql")
    @Sql("/sql/author/insert_author_list.sql")
    @DisplayName("Deve lançar 'AuthorAlreadyExistsException' se o novo 'alias' já estiver em uso")
    void validateAlias_throwsAuthorAlreadyExistsException() {
        AuthorAlreadyExistsException thrown = assertThrows(
            AuthorAlreadyExistsException.class,
            () -> authorUpdateValidatorImpl.validateAlias(AUTHOR_ALIAS, EXISTING_ALIAS));
        assertTrue(thrown.getMessage().contains(EXISTING_ALIAS));
    }
    
    @Test
    @Sql("/sql/country/insert_country_list.sql")
    @DisplayName("Deve receber 'countryId' diferente do 'id' contido no país do autor e retornar país diferente")
    void validateCountry_returnsNewCountry() {
        Country updatedCountry = authorUpdateValidatorImpl.validateCountry(BRAZIL, ITALY_ID);
        assertNotNull(updatedCountry);
        assertEquals(ITALY_ID, updatedCountry.getId());
    }
    
    @Test
    @DisplayName("Deve lançar 'CountryNotFoundException' se o 'countryId' para atualização não for encontrado")
    void validateCountry_throwsCountryNotFoundException() {
        CountryNotFoundException thrown = assertThrows(
            CountryNotFoundException.class,
            () -> authorUpdateValidatorImpl.validateCountry(BRAZIL, NON_EXISTENT_COUNTRY_ID));
        assertTrue(thrown.getMessage().contains(" "+NON_EXISTENT_COUNTRY_ID));
    }
}