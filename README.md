# Livraria - Backend API

Esta documentação detalha a API backend `Livraria`, um serviço completo para gerenciamento de uma livraria, focando em seus recursos, endpoints e configurações.

## Sumário
1. [Visão Geral](#1-visão-geral)
2. [Tecnologias Principais](#2-tecnologias-principais)
3. [Entidades Principais](#3-entidades-principais)
    - [3.1. `Country`](#31-country)
    - [3.2. `Publisher`](#32-publisher)
    - [3.3. `Author`](#33-author)
    - [3.4. `Book` (MappedSuperclass)](#34-book-mappedsuperclass)
    - [3.5. `SalableBook`](#35-salablebook)
4. [Enums](#4-enums)
    - [4.1. `Genre`](#41-genre)
5. [Repositórios (Acesso aos Dados)](#5-repositórios-acesso-a-dados)
    - [5.1. `CountryRepository`](#51-countryrepository)
    - [5.2. `AuthorRepository`](#52-authorrepository)
    - [5.3. `PublisherRepository`](#53-publisherrepository)
    - [5.4. `SalableBookRepository`](#54-salablebookrepository)
6. [DTOs (Data Transfer Objects)](#6-dtos-data-transfer-objects)
    - [6.1. `CreateCountryDTO`](#61-createcountrydto)
    - [6.2. `ResponseCountryDTO`](#62-responsecountrydto)
    - [6.3. `ResponseCountryDTOCreator`](#63-responsecountrydtocreator)
    - [6.4. `CountryResultImplDTO`](#64-countryresultimpldto)
    - [6.5. `CreateAuthorDTO`](#65-createauthordto)
    - [6.6. `ResponseAuthorDTO`](#66-responseauthordto)
    - [6.7. `UpdateAuthorDTO`](#67-updateauthordto)
    - [6.8. `CreatePublisherDTO`](#68-createpublisherdto)
    - [6.9. `ResponsePublisherDTO`](#69-responsepublisherdto)
    - [6.10. `UpdatePublisherDTO`](#610-updatepublisherdto)
    - [6.11. `CreateBookDTO`](#611-createbookdto)
    - [6.12. `ResponseBookDTO`](#612-responsebookdto)
    - [6.13. `UpdateBookDTO`](#613-updatebookdto)
    - [6.14. `CreateSalableBookDTO`](#614-createsalablebookdto)
    - [6.15. `ResponseSalableBookDTO`](#615-responsesalablebookdto)
    - [6.16. `UpdateSalableBookDTO`](#616-updatesalablebookdto)
    - [6.17. `BookSellDTO`](#617-bookselldto)
    - [6.18. `BookSellListDTO`](#618-bookselllistdto)
    - [6.19. `TitleAndAuthorUpdateDTO`](#619-titleandauthorupdatedto)
7. [Serviços (Lógica de Negócio)](#7-serviços-lógica-de-negócio)
    - [7.1. `CountryService`](#71-countryservice)
    - [7.2. `AuthorService`](#72-authorservice)
    - [7.3. `PublisherService`](#73-publisherservice)
    - [7.4. `SalableBookService`](#74-salablebookservice)
8. [Interfaces de Serviço](#8-interfaces-de-serviço)
    - [8.1. `CountryResult`](#81-countryresult)
    - [8.1.1. Implementação: `CountryResultImplDTO`](#811-implementação-countryresultimpldto)
    - [8.2. `CountryExceptionCreator`](#82-countryexceptioncreator)
    - [8.2.1. Implementação: `CountryExceptionCreatorImpl`](#821-implementação-countryexceptioncreatorimpl)
    - [8.3. `CreateOrFindCountryResolver`](#83-createorfindcountryresolver)
    - [8.3.1. Implementação: `CreateOrFindCountryResolverImpl`](#831-implementação-createorfindcountryresolverimpl)
    - [8.4. `ResponseCountryDTOCreator`](#84-responsecountrydtocreator)
    - [8.4.1. Implementação: `ResponseCountryDTOCreatorImpl`](#841-implementação-responsecountrydtocreatorimpl)
    - [8.5. `CountryFinder`](#85-countryfinder)
    - [8.5.1. Implementação: `CountryFinderImpl`](#851-implementação-countryfinderimpl)
    - [8.6. `CountrySaverAndConcurrencyHandle`](#86-countrysaverandconcurrencyhandle)
    - [8.6.1. Implementação: `CountrySaverAndConcurrencyHandleImpl`](#861-implementação-countrysaverandconcurrencyhandleimpl)
    - [8.7. `CountryNameValidator`](#87-countrynamevalidator)
    - [8.8. `IsoAlpha2CodeValidator`](#88-isoalpha2codevalidator)
    - [8.9. `CountryValidator`](#89-countryvalidator)
    - [8.9.1. Implementação: `CountryValidatorImpl`](#891-implementação-countryvalidatorimpl)
    - [8.10. `PublisherExceptionCreator`](#810-publisherexceptioncreator)
    - [8.10.1. Implementação: `PublisherExceptionCreatorImpl`](#8101-implementação-publisherexceptioncreatorimpl)
    - [8.11. `ResponsePublisherDTOCreator`](#811-responsepublisherdtocreator)
    - [8.11.1. Implementação: `ResponsePublisherDTOCreatorImpl`](#8111-implementação-responsepublisherdtocreatorimpl)
    - [8.12. `PublisherFinder`](#812-publisherfinder)
    - [8.12.1. Implementação: `PublisherFinderImpl`](#8121-implementação-publisherfinderimpl)
    - [8.13. `PublisherSaverAndConcurrencyHandle`](#813-publishersaverandconcurrencyhandle)
    - [8.13.1. Implementação: `PublisherSaverAndConcurrencyHandleImpl`](#8131-implementação-publishersaverandconcurrencyhandleimpl)
    - [8.14. `PublisherUpdateValidator`](#814-publisherupdatevalidator)
    - [8.14.1. Implementação: `PublisherUpdateValidatorImpl`](#8141-implementação-publisherupdatevalidatorimpl)
    - [8.15. `AuthorExceptionCreator`](#815-authorexceptioncreator)
    - [8.15.1. Implementação: `AuthorExceptionCreatorImpl`](#8151-implementação-authorexceptioncreatorimpl)
    - [8.16. `ResponseAuthorDTOCreator`](#816-responseauthordtocreator)
    - [8.16.1. Implementação: `ResponseAuthorDTOCreatorImpl`](#8161-implementação-responseauthordtocreatorimpl)
    - [8.17. `AuthorFinder`](#817-authorfinder)
    - [8.17.1. Implementação: `AuthorFinderImpl`](#8171-implementação-authorfinderimpl)
    - [8.18. `AuthorSaverAndConcurrencyHandle`](#818-authorsaverandconcurrencyhandle)
    - [8.18.1. Implementação: `AuthorSaverAndConcurrencyHandleImpl`](#8181-implementação-authorsaverandconcurrencyhandleimpl)
    - [8.19. `AuthorUpdateValidator`](#819-authorupdatevalidator)
    - [8.19.1. Implementação: `AuthorUpdateValidatorImpl`](#8191-implementação-authorupdatevalidatorimpl)
    - [8.20. `ResponseSalableBookDTOCreator`](#820-responsesalablebookdtocreator)
    - [8.20.1. Implementação: `ResponseSalableBookDTOCreatorImpl`](#8201-implementação-responsesalablebookdtocreatorimpl)
    - [8.21. `SalableBookExceptionCreator`](#821-salablebookexceptioncreator)
    - [8.21.1. Implementação: `SalableBookExceptionCreatorImpl`](#8211-implementação-salablebookexceptioncreatorimpl)
    - [8.22. `SalableBookFinder`](#822-salablebookfinder)
    - [8.22.1. Implementação: `SalableBookFinderImpl`](#8221-implementação-salablebookfinderimpl)
    - [8.23. `SalableBookSaverAndConcurrencyHandle`](#823-salablebooksaverandconcurrencyhandle)
    - [8.23.1. Implementação: `SalableBookSaverAndConcurrencyHandleImpl`](#8231-implementação-salablebooksavandconcurrencyhandleimpl)
    - [8.24. `BookSeller`](#824-bookseller)
    - [8.24.1. Implementação: `BookSellerImpl`](#8241-implementação-booksellerimpl)
    - [8.25. `SalableBookUniquenessValidator`](#825-salablebookuniquenessvalidator)
    - [8.25.1. Implementação: `SalableBookUniquenessValidatorImpl`](#8251-implementação-salablebookuniquenessvalidatorimpl)
    - [8.26. `SalableBookUpdateValidator`](#826-salablebookupdatevalidator)
    - [8.26.1. Implementação: `SalableBookUpdateValidatorImpl`](#8261-implementação-salablebookupdatevalidatorimpl)
    - [8.27. `SerializationExceptionCreator`](#827-serializationexceptioncreator)
    - [8.27.1. Implementação: `SerializationExceptionCreatorImpl`](#8271-implementação-serializationexceptioncreatorimpl)
    - [8.28. `ExceptionHandlerMessageCreator`](#828-exceptionhandlermessagecreator)
    - [8.28.1. Implementação: `ExceptionHandlerMessageCreatorImpl`](#8281-implementação-exceptionhandlermessagecreatorimpl)
    - [8.29. Interface: `UnitDecreaser`](#829-interface-unitdecreaser)
    - [8.29.1. Implementação: `SalableBook (UnitDecreaser)`](#8291-implementação-salablebook-unitdecreaser)
9. [Tratamento de Exceções](#9-tratamento-de-exceções)
    - [9.1. Exceções Customizadas](#91-exceções-customizadas)
10. [Serialização Customizada](#10-serialização-customizada)
    - [10.1. `LocalDateDesserializer`](#101-localdatedesserializer)
    - [10.2. `TrimmedStringDeserializer`](#102-trimmedstringdeserializer)
11. [Endpoints da API](#11-endpoints-da-api)
    - [11.1. Endpoints de Países (`/countries`)](#111-endpoints-de-países-countries)
    - [11.2. Endpoints de Autores (`/authors`)](#112-endpoints-de-autores-authors)
    - [11.3. Endpoints de Editoras (`/publishers`)](#113-endpoints-de-editoras-publishers)
    - [11.4. Endpoints de Livros Vendáveis (`/books/salable`)](#114-endpoints-de-livros-vendáveis-bookssalable)
12. [Configurações Essenciais (`application.properties`)](#12-configurações-essenciais-applicationproperties)
13. [Beans Customizados](#13-beans-customizados)
    - [13.1. Configuração de Validação de Países (`CountryValidatorConfig`)](#131-configuração-de-validação-de-países-countryvalidatorconfig)
14. [Mensagens da Aplicação](#14-mensagens-da-aplicação)
15. [Executando Localmente](#15-executando-localmente)
16. [Testes](#16-testes)

---

### 1. Visão Geral

A aplicação `Livraria` é um microserviço robusto, desenvolvido em Java com Spring Boot, projetado para gerenciar de forma abrangente as entidades essenciais para um sistema de livraria: países, editoras, autores e livros.

Ela oferece funcionalidades completas de cadastro e consulta, como a gestão de países utilizando códigos ISO Alpha-2, que servem como base fundamental para as entidades `Publisher` (Editora) e `Author` (Autor). Essas entidades, por sua vez, estão diretamente vinculadas à entidade `SalableBook`, que representa os livros comercializáveis e disponíveis no sistema.

O objetivo principal desta aplicação é estabelecer uma base sólida, escalável e bem-estruturada para futuras expansões, garantindo uma gestão eficiente e consistente das informações da livraria.

---

### 2. Tecnologias Principais

*   **Framework**: Spring Boot 3.x (versão `3.5.7`)
*   **Linguagem**: Java 21
*   **Persistência**: Spring Data JPA
*   **Banco de Dados**: H2 Database (persistido em arquivo)
*   **Auxiliares**: Lombok (para reduzir código boilerplate)
*   **Validação**: Spring Boot Starter Validation
*   **Build Tool**: Maven

---

### 3. Entidades Principais

As principais entidades que compõem o sistema são:

#### 3.1. `Country`

Representa um país, armazenando seu código ISO Alpha-2 e nome completo.

*   **id**: `Integer` (Gerado automaticamente, chave primária)
*   **isoAlpha2Code**: `String` (Código ISO Alpha-2 do país, 2 caracteres, obrigatório, único)
*   **name**: `String` (Nome completo do país, obrigatório, único)

#### 3.2. `Publisher`

Representa uma editora de livros.

*   **id**: `Long` (Gerado automaticamente, chave primária)
*   **name**: `String` (Nome da editora, `nullable=false`)
*   **country**: `Country` (País de origem da editora, `nullable=false`, relacionamento Many-to-One com `Country`)
*   **fullAddress**: `String` (Endereço completo da editora, `nullable=false`, `unique=true`)

#### 3.3. `Author`

Representa um autor de livros.

*   **id**: `Long` (Gerado automaticamente, chave primária)
*   **alias**: `String` (Pseudônimo ou nome artístico do autor, `nullable=false`, `unique=true`)
*   **fullName**: `String` (Nome completo do autor, `nullable=false`)
*   **birthday**: `LocalDate` (Data de nascimento do autor, `nullable=false`)
*   **country**: `Country` (Nacionalidade do autor, `nullable=false`, relacionamento Many-to-One com `Country`)

#### 3.4. `Book` (MappedSuperclass)

Classe abstrata base para entidades de livro, contendo atributos comuns. Não é uma entidade persistível por si só, mas suas propriedades são mapeadas nas entidades que a estendem.

*   **title**: `String` (Título do livro, `nullable=false`)
*   **author**: `Author` (Autor do livro, `nullable=false`, relacionamento Many-to-One com `Author`)
*   **publisher**: `Publisher` (Editora do livro, `nullable=false`, relacionamento Many-to-One com `Publisher`)
*   **genre**: `Genre` (Gênero do livro, `nullable=false`, armazenado como `String`)
*   **isbn**: `String` (ISBN do livro, `nullable=false`)

#### 3.5. `SalableBook`

Representa um livro que está disponível para venda, estendendo a funcionalidade base de `Book`.

*   **id**: `Long` (Gerado automaticamente, chave primária)
*   **price**: `BigDecimal` (Preço do livro, `nullable=false`, coluna `DECIMAL(8,2)` com validação `CHECK (price >= 0)`)
*   **units**: `Integer` (Quantidade de unidades em estoque, `nullable=false`, coluna `INT` com validação `CHECK (units >= 0)`)
*   **Constraint de Unicidade**: A combinação de `title` e `author_id` deve ser única (nome: `UK_salable_book_title_author`).
*   **Implementa**: `UnitDecreaser` (interface para gerenciamento de estoque)

---

### 4. Enums

Os enums (enumerações) são tipos de dados especiais que permitem que uma variável seja um conjunto de constantes pré-definidas. Eles são utilizados para representar um conjunto fixo de valores relacionados, aumentando a legibilidade e a segurança do tipo de código.

#### 4.1. `Genre`

O enum `Genre` representa os possíveis gêneros literários que um livro pode ter na aplicação. Cada constante do enum possui uma descrição associada que pode ser utilizada para exibição ou outras finalidades.

*   **Localização**: `caio.portfolio.livraria.model.enums.Genre`
*   **Campos**:
    *   `genreType`: `String` (Descrição textual do gênero)
*   **Valores Definidos**:
    *   `ROMANCE("Romance")`
    *   `FICTION("Ficção")`
    *   `FANTASY("Fantasia")`
    *   `SUSPENSE("Suspense")`
    *   `CHILDISH("Infantil")`
*   **Uso**: Este enum é utilizado na entidade `Book` (e por extensão em `SalableBook`) para categorizar os livros, garantindo que apenas gêneros válidos sejam associados a eles.

```java
package caio.portfolio.livraria.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Genre {
    ROMANCE("Romance"),
    FICTION("Ficção"),
    FANTASY("Fantasia"),
    SUSPENSE("Suspense"),
    CHILDISH("Infantil");
    
    private final String genreType;
}
```

---

### 5. Repositórios (Acesso a Dados)

Os repositórios são interfaces Spring Data JPA que fornecem a infraestrutura para operações de persistência de dados (CRUD) para as entidades. Eles estendem `JpaRepository`, o que significa que herdam métodos padrão para salvar, buscar, atualizar e deletar entidades, além de permitir a definição de métodos de busca personalizados.

#### 5.1. `CountryRepository`

Interface para acesso a dados da entidade `Country`.

*   **Estende**: `JpaRepository<Country, Integer>`
*   **Métodos Personalizados**:
    *   `Optional<Country> findByIsoAlpha2Code(String isoAlpha2Code)`: Busca um país pelo seu código ISO Alpha-2. Retorna um `Optional` para indicar a possibilidade de o país não ser encontrado.

#### 5.2. `AuthorRepository`

Interface para acesso a dados da entidade `Author`.

*   **Estende**: `JpaRepository<Author, Long>`
*   **Métodos Personalizados**:
    *   `Optional<Author> findByAlias(String alias)`: Busca um autor pelo seu pseudônimo (alias). Retorna um `Optional`.

#### 5.3. `PublisherRepository`

Interface para acesso a dados da entidade `Publisher`.

*   **Estende**: `JpaRepository<Publisher, Long>`
*   **Métodos Personalizados**:
    *   `Optional<Publisher> findByFullAddress(String fullAddress)`: Busca uma editora pelo seu endereço completo. Retorna um `Optional`.

#### 5.4. `SalableBookRepository`

Interface para acesso a dados da entidade `SalableBook`.

*   **Estende**: `JpaRepository<SalableBook, Long>`
*   **Métodos Personalizados**:
    *   `Optional<SalableBook> findByTitleAndAuthor(String title, Author author)`: Busca um livro vendável pelo seu título e autor. Utilizado para verificar unicidade.
    *   `List<SalableBook> findByAuthor(Author author)`: Busca todos os livros vendáveis associados a um determinado autor.
    *   `List<SalableBook> findByPublisher(Publisher publisher)`: Busca todos os livros vendáveis associados a uma determinada editora.
    *   `List<SalableBook> findByTitle(String title)`: Busca livros vendáveis que contenham o título especificado.
    *   `List<SalableBook> findByGenre(Genre genre)`: Busca todos os livros vendáveis de um gênero específico.
    *   `List<SalableBook> findByIsbn(String isbn)`: Busca livros vendáveis por seu ISBN.

---

### 6. DTOs (Data Transfer Objects)

#### 6.1. `CreateCountryDTO`

Utilizado para a criação de um novo `Country`.

*   **isoAlpha2Code**: `String` (Código ISO Alpha-2 do país.
    *   **Validações**: `NotBlank` (não pode ser vazio, nulo ou conter apenas espaços em branco, com a mensagem de erro configurada por `isoAlpha2Code.notBlank`), `Size(min=2, max=2)` para garantir que tenha exatamente 2 caracteres.
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos antes da validação.

#### 6.2. `ResponseCountryDTO`

Utilizado para retornar informações de um `Country` nas respostas da API.

*   **id**: `Integer`
*   **isoAlpha2Code**: `String`
*   **name**: `String`

#### 6.3. `ResponseCountryDTOCreator`

Interface e implementação responsáveis por mapear a entidade `Country` para seu respectivo `ResponseCountryDTO`. Isso centraliza a lógica de conversão, mantendo a responsabilidade de construção de DTOs separada das entidades e serviços, promovendo um design mais limpo e testável.

*   **Interface**: `ResponseCountryDTOCreator`
*   **Implementação**: `ResponseCountryDTOCreatorImpl` (anotado com `@Service`)
*   **Método Principal**:
    *   `toResponseCountryDTO(Country country)`: Recebe uma entidade `Country` e retorna uma instância de `ResponseCountryDTO` preenchida com os dados do país. Utiliza o padrão `builder` do `ResponseCountryDTO` para uma construção clara e imutável.

#### 6.4. `CountryResultImplDTO`

Implementação do DTO que encapsula o resultado de uma operação de criação ou busca de país, indicando se o país foi criado ou encontrado.

*   **Implementação**: `CountryResultImplDTO`
*   **Interfaces**: Implementa `CountryResult`.
*   **Campos**:
    *   **country**: `ResponseCountryDTO` (O DTO do país resultante da operação).
    *   **created**: `boolean` (Indica se o país foi recém-criado (`true`) ou se já existia (`false`)).
*   **Métodos**:
    *   `wasCreated()`: Retorna `true` se o país foi criado, `false` caso contrário.
    *   `wasFound()`: Retorna `true` se o país foi encontrado (ou seja, não foi criado), `false` caso contrário.
*   **Uso**: Este DTO é fundamental para o `CountryService` retornar o estado da operação de forma clara, permitindo que a camada de controle (controllers) tome decisões apropriadas (ex: retornar `201 Created` ou `200 OK`).

#### 6.5. `CreateAuthorDTO`

Utilizado para a criação de um novo `Author`.

*   **alias**: `String` (Pseudônimo ou nome artístico do autor)
    *   **Validações**: `@NotBlank` (não pode ser vazio, nulo ou conter apenas espaços em branco, com a mensagem de erro configurada por `alias.notBlank`).
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos antes da validação.
*   **fullName**: `String` (Nome completo do autor)
    *   **Validações**: `@NotBlank` (não pode ser vazio, nulo ou conter apenas espaços em branco, com a mensagem de erro configurada por `fullName.notBlank`).
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos antes da validação.
*   **birthday**: `LocalDate` (Data de nascimento do autor)
    *   **Validações**: `@NotNull` (não pode ser nulo, com a mensagem de erro configurada por `birthday.notNull`).
    *   **Desserialização**: Possui `LocalDateDesserializer` aplicado via `@JsonDeserialize` para desserializar a data de nascimento.
*   **countryId**: `Integer` (ID do país de nacionalidade do autor)
    *   **Validações**: `@NotNull` (não pode ser nulo, com a mensagem de erro configurada por `countryId.notNull`), `@Positive` (deve ser um número inteiro positivo, com a mensagem de erro configurada por `countryId.grather.than.zero`).

#### 6.6. `ResponseAuthorDTO`

Utilizado para retornar informações de um `Author` nas respostas da API.

*   **id**: `Long` (ID único do autor).
*   **alias**: `String` (Pseudônimo ou nome artístico do autor).
*   **fullName**: `String` (Nome completo do autor).
*   **birthday**: `LocalDate` (Data de nascimento do autor).
*   **countryId**: `Integer` (ID do país de nacionalidade do autor).

#### 6.7. `UpdateAuthorDTO`

Utilizado para atualizar informações de um `Author` existente. Todos os campos são opcionais para permitir atualizações parciais.

*   **birthday**: `LocalDate` (Data de nascimento do autor)
    *   **Formato**: `@JsonFormat(pattern="yyyy-MM-dd")` para especificar o formato da data durante a serialização e desserialização.
    *   **Desserialização**: Possui `LocalDateDesserializer` aplicado via `@JsonDeserialize` para desserializar a data de nascimento.
*   **countryId**: `Integer` (ID do país de nacionalidade do autor)
    *   **Validações**: `@Positive` (deve ser um número inteiro positivo, se presente, com a mensagem de erro configurada por `countryId.grather.than.zero`).
*   **alias**: `String` (Pseudônimo ou nome artístico do autor)
    *   **Validações**: `@Pattern(regexp="^(?!\s*$).+", message="{alias.notBlank}")` para garantir que o campo, se presente, não seja vazio ou composto apenas por espaços em branco.
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos.
*   **fullName**: `String` (Nome completo do autor)
    *   **Validações**: `@Pattern(regexp="^(?!\s*$).+", message="{fullName.notBlank}")` para garantir que o campo, se presente, não seja vazio ou composto apenas por espaços em branco.
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos.

#### 6.8. `CreatePublisherDTO`

Utilizado para a criação de uma nova `Publisher`.

*   **name**: `String` (Nome da editora)
    *   **Validações**: `@NotBlank` (não pode ser vazio, nulo ou conter apenas espaços em branco, com a mensagem de erro configurada por `name.notBlank`).
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos antes da validação.
*   **countryId**: `Integer` (ID do país de origem da editora)
    *   **Validações**: `@NotNull` (não pode ser nulo, com a mensagem de erro configurada por `countryId.notNull`), `@Positive` (deve ser um número inteiro positivo, com a mensagem de erro configurada por `countryId.grather.than.zero`).
*   **fullAddress**: `String` (Endereço completo da editora)
    *   **Validações**: `@NotBlank` (não pode ser vazio, nulo ou conter apenas espaços em branco, com a mensagem de erro configurada por `fullAddress.notBlank`).
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos antes da validação.

#### 6.9. `ResponsePublisherDTO`

Utilizado para retornar informações de uma `Publisher` nas respostas da API.

*   **id**: `Long` (ID único da editora).
*   **name**: `String` (Nome da editora).
*   **countryId**: `Integer` (ID do país de origem da editora).
*   **fullAddress**: `String` (Endereço completo da editora).

#### 6.10. `UpdatePublisherDTO`

Utilizado para atualizar informações de uma `Publisher` existente. Todos os campos são opcionais para permitir atualizações parciais.

*   **countryId**: `Integer` (ID do país de origem da editora)
    *   **Validações**: `@Positive` (deve ser um número inteiro positivo, se presente, com a mensagem de erro configurada por `countryId.grather.than.zero`).
*   **name**: `String` (Nome da editora)
    *   **Validações**: `@Pattern(regexp="^(?!\s*$).+", message="{name.notBlank}")` para garantir que o campo, se presente, não seja vazio ou composto apenas por espaços em branco.
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos.
*   **fullAddress**: `String` (Endereço completo da editora)
    *   **Validações**: `@Pattern(regexp="^(?!\s*$).+", message="{fullAddress.notBlank}")` para garantir que o campo, se presente, não seja vazio ou composto apenas por espaços em branco.
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos.

#### 6.11. `CreateBookDTO`

Classe abstrata base para DTOs de criação de livros, contendo atributos comuns.

*   **genre**: `Genre` (Gênero do livro)
    *   **Validações**: `@NotNull` (não pode ser nulo, com a mensagem de erro configurada por `genre.notNull`).
*   **authorId**: `Long` (ID do autor do livro)
    *   **Validações**: `@NotNull` (não pode ser nulo, com a mensagem de erro configurada por `authorId.notNull`), `@Positive` (deve ser um número inteiro positivo, com a mensagem de erro configurada por `authorId.grather.than.zero`).
*   **publisherId**: `Long` (ID da editora do livro)
    *   **Validações**: `@NotNull` (não pode ser nulo, com a mensagem de erro configurada por `publisherId.notNull`), `@Positive` (deve ser um número inteiro positivo, com a mensagem de erro configurada por `publisherId.grather.than.zero`).
*   **isbn**: `String` (ISBN do livro)
    *   **Validações**: `@NotBlank` (não pode ser vazio, nulo ou conter apenas espaços em branco, com a mensagem de erro configurada por `isbn.notBlank`).
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos antes da validação.
*   **title**: `String` (Título do livro)
    *   **Validações**: `@NotBlank` (não pode ser vazio, nulo ou conter apenas espaços em branco, com a mensagem de erro configurada por `title.notBlank`).
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos antes da validação.

#### 6.12. `ResponseBookDTO`

Classe base para DTOs de resposta de livros.

*   **title**: `String` (Título do livro).
*   **authorId**: `Long` (ID do autor do livro).
*   **publisherId**: `Long` (ID da editora do livro).
*   **genre**: `Genre` (Gênero do livro).
*   **isbn**: `String` (ISBN do livro).

#### 6.13. `UpdateBookDTO`

Classe abstrata base para DTOs de atualização de livros. Todos os campos são opcionais para permitir atualizações parciais.

*   **genre**: `Genre` (Gênero do livro).
*   **authorId**: `Long` (ID do autor do livro)
    *   **Validações**: `@Positive` (deve ser um número inteiro positivo, se presente, com a mensagem de erro configurada por `authorId.grather.than.zero`).
*   **publisherId**: `Long` (ID da editora do livro)
    *   **Validações**: `@Positive` (deve ser um número inteiro positivo, se presente, com a mensagem de erro configurada por `publisherId.grather.than.zero`).
*   **title**: `String` (Título do livro)
    *   **Validações**: `@Pattern(regexp="^(?!\s*$).+", message="{title.notBlank}")` para garantir que o campo, se presente, não seja vazio ou composto apenas por espaços em branco.
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos.
*   **isbn**: `String` (ISBN do livro)
    *   **Validações**: `@Pattern(regexp="^(?!\s*$).+", message="{isbn.notBlank}")` para garantir que o campo, se presente, não seja vazio ou composto apenas por espaços em branco.
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos.

#### 6.14. `CreateSalableBookDTO`

Utilizado para a criação de um novo `SalableBook`. Estende `CreateBookDTO`.

*   **price**: `BigDecimal` (Preço do livro)
    *   **Validações**: `@Min(value=0, message="{price.positive.number}")` (deve ser um número não negativo, com a mensagem de erro configurada por `price.positive.number`), `@NotNull` (não pode ser nulo, com a mensagem de erro configurada por `price.notNull`).
*   **units**: `Integer` (Quantidade de unidades em estoque)
    *   **Validações**: `@Min(value=0, message="{units.positive.number}")` (deve ser um número não negativo, com a mensagem de erro configurada por `units.positive.number`), `@NotNull` (não pode ser nulo, com a mensagem de erro configurada por `units.notNull`).

#### 6.15. `ResponseSalableBookDTO`

Utilizado para retornar informações de um `SalableBook` nas respostas da API. Estende `ResponseBookDTO`.

*   **id**: `Long` (ID único do livro vendável).
*   **price**: `BigDecimal` (Preço do livro).
*   **units**: `Integer` (Quantidade de unidades em estoque).

#### 6.16. `UpdateSalableBookDTO`

Utilizado para atualizar informações de um `SalableBook` existente. Estende `UpdateBookDTO`. Todos os campos são opcionais para permitir atualizações parciais.

*   **price**: `BigDecimal` (Preço do livro)
    *   **Validações**: `@Min(value=0, message="{price.positive.number}")` (deve ser um número não negativo, se presente, com a mensagem de erro configurada por `price.positive.number`).
*   **units**: `Integer` (Quantidade de unidades em estoque)
    *   **Validações**: `@Min(value=0, message="{units.positive.number}")` (deve ser um número não negativo, se presente, com a mensagem de erro configurada por `units.positive.number`).

#### 6.17. `BookSellDTO`

Utilizado para representar um item individual de venda de livro.

*   **bookId**: `Long` (ID do livro a ser vendido)
    *   **Validações**: `@NotNull` (não pode ser nulo, com a mensagem de erro configurada por `bookId.notNull`), `@Positive` (deve ser um número inteiro positivo, com a mensagem de erro configurada por `bookId.grather.than.zero`).
*   **units**: `int` (Quantidade de unidades do livro a ser vendida)
    *   **Validações**: `@Min(value=1, message="{units.grather.than.zero}")` (deve ser um número inteiro positivo maior ou igual a 1, com a mensagem de erro configurada por `units.grather.than.zero`).

#### 6.18. `BookSellListDTO`

Utilizado para encapsular uma lista de `BookSellDTO`s para operações de venda em lote.

*   **sellList**: `List<BookSellDTO>` (Lista de livros a serem vendidos)
    *   **Validações**: `@Valid` (garante que cada `BookSellDTO` na lista também seja validado), `@NotEmpty` (a lista não pode ser vazia, com a mensagem de erro configurada por `sellList.notEmpty`).

#### 6.19. `TitleAndAuthorUpdateDTO`

DTO interno utilizado para representar a combinação de um autor e um título, frequentemente usado em cenários de atualização ou busca onde esses dois campos são relevantes.

*   **author**: `Author` (A entidade `Author` associada).
*   **title**: `String` (O título do livro).

---

### 7. Serviços (Lógica de Negócio)

#### 7.1. `CountryService`

Responsável pela orquestração das operações de negócio relacionadas a `Country`, delegando responsabilidades a componentes especializados.

*   **`createOrFindCountry(CreateCountryDTO createCountryDTO)`**:
    *   Valida e normaliza o `isoAlpha2Code` fornecido.
    *   Utiliza `CreateOrFindCountryResolver` para verificar se o país já existe no banco de dados.
    *   Se existir, retorna um `CountryResultImplDTO` com o país encontrado e o indicador `isCreated` como `false`.
    *   Se não existir, resolve o nome do país com base no código ISO, cria e persiste a nova entidade `Country`.
    *   Retorna um `CountryResultImplDTO` com o país criado e o indicador `isCreated` como `true`.
*   **`getCountryById(Integer id)`**:
    *   Busca e retorna a entidade `Country` específica pelo seu ID único.
*   **`getResponseCountryDTOById(Integer id)`**:
    *   Busca e retorna um país específico pelo seu ID único, mapeado para `ResponseCountryDTO`.

##### 7.1.1. Dependências Injetadas e Suas Responsabilidades

A classe `CountryService` utiliza as seguintes dependências, injetadas via construtor (gracas ao `@RequiredArgsConstructor` do Lombok), para delegar responsabilidades e manter a coesão, alinhada aos princípios SOLID:

*   **`CountryRepository`**:
    *   Interface do Spring Data JPA utilizada para realizar operações de persistência e recuperação de dados da entidade `Country` no banco de dados.
*   **`CountryValidator`**:
    *   Componente responsável pela validação e normalização de códigos ISO Alpha-2, além da resolução de nomes de países.
*   **`ResponseCountryDTOCreator`**:
    *   Componente dedicado a mapear entidades `Country` para `ResponseCountryDTO`, garantindo uma representação consistente dos dados para o cliente.
*   **`CreateOrFindCountryResolver`**:
    *   Encapsula a lógica de negócio para determinar se um país já existe ou precisa ser criado, retornando um `CountryResultImplDTO` com o resultado da operação.
*   **`CountryFinder`**:
    *   Componente responsável por encapsular a lógica de busca de entidades `Country` por diferentes critérios (e.g., ID, código ISO Alpha-2), isolando o `CountryService` dos detalhes de acesso direto ao repositório para essas operações específicas de busca.

#### 7.2. `AuthorService`

Responsável pela orquestração das operações de negócio relacionadas a `Author`, delegando responsabilidades a componentes especializados para criação, busca, atualização e exclusão de autores, além de lidar com validações e tratamento de exceções.

*   **`createAuthor(CreateAuthorDTO dto)`**:
    *   Verifica a unicidade do alias do autor. Se já existir, lança uma exceção `AuthorAlreadyExistsException`.
    *   Cria uma nova entidade `Author` com base nos dados fornecidos, buscando o `Country` associado através do `CountryService`.
    *   Persiste o novo autor utilizando `AuthorSaverAndConcurrencyHandle`, que gerencia a persistência e possíveis problemas de concorrência.
    *   Retorna o autor criado como um `ResponseAuthorDTO`.
*   **`getAllResponseAuthorDTOs()`**:
    *   Retorna uma lista de todos os `Author`s registrados, mapeados para `ResponseAuthorDTO`.
*   **`getResponseAuthorDTOById(Long id)`**:
    *   Busca e retorna um autor específico pelo seu ID único, mapeado para `ResponseAuthorDTO`.
*   **`getResponseAuthorDTOByAlias(String alias)`**:
    *   Busca e retorna um autor específico pelo seu alias, mapeado para `ResponseAuthorDTO`.
*   **`updateAuthor(Long id, UpdateAuthorDTO dto)`**:
    *   Busca o autor existente pelo ID.
    *   Utiliza `AuthorUpdateValidator` para validar e aplicar as atualizações nos campos (alias, nome completo, data de nascimento e país), garantindo que as regras de negócio sejam respeitadas.
    *   Persiste o autor atualizado utilizando `AuthorSaverAndConcurrencyHandle`.
    *   Retorna o autor atualizado como um `ResponseAuthorDTO`.
*   **`getAuthorById(Long id)`**:
    *   Busca e retorna a entidade `Author` específica pelo seu ID único.
*   **`deleteAuthorById(Long id)`**:
    *   Verifica se o autor com o `id` fornecido existe. Se não existir, lança uma exceção `AuthorNotFoundException`.
    *   Remove o autor do banco de dados pelo seu ID.
    *   Retorna `true` se a exclusão for bem-sucedida.

##### 7.2.1. Dependências Injetadas e Suas Responsabilidades

A classe `AuthorService` utiliza as seguintes dependências, injetadas via construtor (graças ao `@RequiredArgsConstructor` do Lombok), para delegar responsabilidades e manter a coesão, alinhada aos princípios SOLID:

*   **`AuthorRepository`**:
    *   Interface do Spring Data JPA utilizada para realizar operações de persistência e recuperação de dados da entidade `Author` no banco de dados.
*   **`ResponseAuthorDTOCreator`**:
    *   Componente dedicado a mapear entidades `Author` para `ResponseAuthorDTO`, garantindo uma representação consistente dos dados para o cliente.
*   **`AuthorSaverAndConcurrencyHandle`**:
    *   Encapsula a lógica de salvamento e atualização de entidades `Author`, incluindo o tratamento de cenários de concorrência.
*   **`AuthorUpdateValidator`**:
    *   Componente especializado na validação e aplicação de alterações em campos de `Author` durante uma operação de atualização.
*   **`AuthorExceptionCreator`**:
    *   Componente responsável pela criação de exceções específicas relacionadas a `Author`, padronizando o tratamento de erros.
*   **`AuthorFinder`**:
    *   Componente que centraliza e abstrai a lógica de busca de entidades `Author` por diferentes critérios (ID, alias).
*   **`CountryService`**:
    *   Serviço utilizado para obter informações sobre países, necessário para associar um `Country` ao `Author` durante a criação ou atualização.

#### 7.3. `PublisherService`

Responsável pela orquestração das operações de negócio relacionadas a `Publisher`, delegando responsabilidades a componentes especializados para criação, busca, atualização e exclusão de editoras, além de lidar com validações e tratamento de exceções.

*   **`createPublisher(CreatePublisherDTO dto)`**:
    *   Verifica a unicidade do `fullAddress` da editora. Se já existir, lança uma exceção `PublisherAlreadyExistsException`.
    *   Cria uma nova entidade `Publisher` com base nos dados fornecidos, buscando o `Country` associado através do `CountryService`.
    *   Persiste a nova editora utilizando `PublisherSaverAndConcurrencyHandle`, que gerencia a persistência e possíveis problemas de concorrência.
    *   Retorna a editora criada como um `ResponsePublisherDTO`.
*   **`getAllResponsePublisherDTOs()`**:
    *   Retorna uma lista de todas as `Publisher`s registradas, mapeadas para `ResponsePublisherDTO`.
*   **`getResponsePublisherDTOByFullAddress(String fullAddress)`**:
    *   Busca e retorna uma editora específica pelo seu endereço completo (`fullAddress`), mapeada para `ResponsePublisherDTO`.
*   **`getResponsePublisherDTOById(Long id)`**:
    *   Busca e retorna uma editora específica pelo seu ID único, mapeada para `ResponsePublisherDTO`.
*   **`updatePublisher(Long id, UpdatePublisherDTO dto)`**:
    *   Busca a editora existente pelo ID.
    *   Utiliza `PublisherUpdateValidator` para validar e aplicar as atualizações nos campos (nome, país e endereço completo), garantindo que as regras de negócio sejam respeitadas.
    *   Persiste a editora atualizada utilizando `PublisherSaverAndConcurrencyHandle`.
    *   Retorna a editora atualizada como um `ResponsePublisherDTO`.
*   **`getPublisherById(Long id)`**:
    *   Busca e retorna a entidade `Publisher` específica pelo seu ID único.
*   **`deletePublisherById(Long id)`**:
    *   Verifica se a editora com o `id` fornecido existe. Se não existir, lança uma exceção `PublisherNotFoundException`.
    *   Remove a editora do banco de dados pelo seu ID.
    *   Retorna `true` se a exclusão for bem-sucedida.

##### 7.3.1. Dependências Injetadas e Suas Responsabilidades

A classe `PublisherService` utiliza as seguintes dependências, injetadas via construtor (graças ao `@RequiredArgsConstructor` do Lombok), para delegar responsabilidades e manter a coesão, alinhada aos princípios SOLID:

*   **`PublisherRepository`**:
    *   Interface do Spring Data JPA utilizada para realizar operações de persistência e recuperação de dados da entidade `Publisher` no banco de dados.
*   **`ResponsePublisherDTOCreator`**:
    *   Componente dedicado a mapear entidades `Publisher` para `ResponsePublisherDTO`, garantindo uma representação consistente dos dados para o cliente.
*   **`PublisherUpdateValidator`**:
    *   Componente especializado na validação e aplicação de alterações em campos de `Publisher` durante uma operação de atualização.
*   **`PublisherSaverAndConcurrencyHandle`**:
    *   Encapsula a lógica de salvamento e atualização de entidades `Publisher`, incluindo o tratamento de cenários de concorrência.
*   **`PublisherExceptionCreator`**:
    *   Componente responsável pela criação de exceções específicas relacionadas a `Publisher`, padronizando o tratamento de erros.
*   **`PublisherFinder`**:
    *   Componente que centraliza e abstrai a lógica de busca de entidades `Publisher` por diferentes critérios (ID, endereço completo).
*   **`CountryService`**:
    *   Serviço utilizado para obter informações sobre países, necessário para associar um `Country` à `Publisher` durante a criação ou atualização.

#### 7.4. `SalableBookService`

Responsável pela orquestração das operações de negócio relacionadas a `SalableBook`, delegando responsabilidades a componentes especializados para criação, busca, atualização, venda e exclusão de livros, além de lidar com validações e tratamento de exceções.

*   **`createSalableBook(CreateSalableBookDTO dto)`**:
    *   Obtém a entidade `Author` e `Publisher` correspondentes aos IDs fornecidos.
    *   Valida a unicidade da combinação autor e título para evitar duplicatas.
    *   Cria uma nova entidade `SalableBook` com base nos dados fornecidos.
    *   Persiste o novo livro utilizando `SalableBookSaverAndConcurrencyHandle`, que gerencia a persistência e possíveis problemas de concorrência.
    *   Retorna o livro criado como um `ResponseSalableBookDTO`.
*   **`getAllResponseSalableBookDTOs()`**:
    *   Retorna uma lista de todos os `SalableBook`s registrados, mapeados para `ResponseSalableBookDTO`.
*   **`getResponseSalableBookDTOById(Long id)`**:
    *   Busca e retorna um livro específico pelo seu ID único, mapeado para `ResponseSalableBookDTO`.
*   **`getResponseSalableBookDTOByAuthorId(Long authorId)`**:
    *   Busca todos os livros associados a um autor específico pelo seu ID, mapeados para `ResponseSalableBookDTO`.
*   **`getResponseSalableBookDTOByPublisherId(Long publisherId)`**:
    *   Busca todos os livros associados a uma editora específica pelo seu ID, mapeados para `ResponseSalableBookDTO`.
*   **`getResponseSalableBookDTOByTitle(String title)`**:
    *   Busca todos os livros que correspondem a um título específico, mapeados para `ResponseSalableBookDTO`.
*   **`getResponseSalableBookDTOByGenre(Genre genre)`**:
    *   Busca todos os livros que pertencem a um gênero específico, mapeados para `ResponseSalableBookDTO`.
*   **`getResponseSalableBookDTOByIsbn(String isbn)`**:
    *   Busca todos os livros que correspondem a um ISBN específico, mapeados para `ResponseSalableBookDTO`.
*   **`updateSalableBookById(Long id, UpdateSalableBookDTO dto)`**:
    *   Busca o livro existente pelo ID.
    *   Realiza a validação e atualização do título e autor, utilizando `SalableBookUpdateValidator` e `SalableBookUniquenessValidator` para garantir a unicidade.
    *   Aplica as demais atualizações nos campos do livro (gênero, editora, ISBN, preço, unidades) através do `SalableBookUpdateValidator`.
    *   Persiste o livro atualizado utilizando `SalableBookSaverAndConcurrencyHandle`.
    *   Retorna o livro atualizado como um `ResponseSalableBookDTO`.
*   **`sellBooks(BookSellListDTO bookListDTO)`**:
    *   Coordena a operação de venda de múltiplos livros, delegando ao `BookSeller`.
    *   Retorna o valor total da transação.
*   **`deleteSalableBookById(Long id)`**:
    *   Verifica se o livro com o `id` fornecido existe. Se não existir, lança uma exceção `SalableBookNotFoundException`.
    *   Remove o livro do banco de dados pelo seu ID.
    *   Retorna `true` se a exclusão for bem-sucedida.

##### 7.4.1. Dependências Injetadas e Suas Responsabilidades

A classe `SalableBookService` utiliza as seguintes dependências, injetadas via construtor (graças ao `@RequiredArgsConstructor` do Lombok), para delegar responsabilidades e manter a coesão, alinhada aos princípios SOLID:

*   **`SalableBookRepository`**:
    *   Interface do Spring Data JPA utilizada para realizar operações de persistência e recuperação de dados da entidade `SalableBook` no banco de dados.
*   **`ResponseSalableBookDTOCreator`**:
    *   Componente dedicado a mapear entidades `SalableBook` para `ResponseSalableBookDTO`, garantindo uma representação consistente dos dados para o cliente.
*   **`SalableBookUpdateValidator`**:
    *   Componente especializado na validação e aplicação de alterações em campos de `SalableBook` durante uma operação de atualização.
*   **`SalableBookSaverAndConcurrencyHandle`**:
    *   Encapsula a lógica de salvamento e atualização de entidades `SalableBook`, incluindo o tratamento de cenários de concorrência.
*   **`SalableBookUniquenessValidator`**:
    *   Componente responsável por validar a unicidade de livros, especialmente a combinação de autor e título, tanto na criação quanto na atualização.
*   **`SalableBookExceptionCreator`**:
    *   Componente responsável pela criação de exceções específicas relacionadas a `SalableBook`, padronizando o tratamento de erros.
*   **`BookSeller`**:
    *   Componente que encapsula a lógica de negócio para a venda de livros, incluindo a atualização de unidades disponíveis e cálculo do valor total.
*   **`SalableBookFinder`**:
    *   Componente que centraliza e abstrai a lógica de busca de entidades `SalableBook` por diferentes critérios (ID, autor, editora, título, gênero, ISBN).
*   **`AuthorService`**:
    *   Serviço utilizado para obter entidades `Author`, necessário para associar um autor ao livro.
*   **`PublisherService`**:
    *   Serviço utilizado para obter entidades `Publisher`, necessário para associar uma editora ao livro.

---

### 8. Interfaces de Serviço

Esta seção detalha as interfaces que definem contratos específicos para as operações de negócio, promovendo a modularidade e a aderência aos princípios SOLID, especialmente o Princípio de Segregação de Interfaces (ISP).

#### 8.1. `CountryResult`

Define o contrato para objetos que encapsulam o resultado de uma operação de criação ou busca de país, indicando se o país foi criado ou encontrado.

*   **Localização**: `caio.portfolio.livraria.service.country.dto.model`
*   **Métodos**:
    *   `boolean wasCreated()`: Retorna `true` se o país foi recém-criado na operação.
    *   `boolean wasFound()`: Retorna `true` se o país já existia e foi encontrado na operação.

##### 8.1.1. Implementação: `CountryResultImplDTO`

A classe `CountryResultImplDTO` é uma implementação de `CountryResult` e atua como um DTO para representar o resultado de uma operação de criação ou busca de um país. Ela encapsula o `ResponseCountryDTO` do país em questão e um indicador booleano (`created`) que sinaliza se o país foi recém-criado durante a operação.

*   **Localização**: `caio.portfolio.livraria.service.country.dto.implementation`
*   **Funcionalidade**:
    *   Armazena o `ResponseCountryDTO` do país.
    *   Possui um campo `created` que é `true` se o país foi criado e `false` se foi encontrado.
    *   Os métodos `wasCreated()` e `wasFound()` fornecem uma interface clara para verificar o estado da operação.
*   **Dependências**:
    *   `ResponseCountryDTO`: A representação do país no DTO.

#### 8.2. `CountryExceptionCreator`

Define métodos para a criação centralizada de diferentes tipos de exceções relacionadas a operações de `Country`, garantindo consistência nas mensagens de erro e facilitando a manutenção.

*   **Localização**: `caio.portfolio.livraria.service.country.model.create`
*   **Métodos**:
    *   `ConcurrentCountryException createConcurrentCountryException(String isoAlpha2Code)`: Cria uma exceção para o cenário onde há uma tentativa de criação de um país com um código ISO já existente (conflito de concorrência).
    *   `IllegalArgumentException createIllegalArgumentExceptionByBlank()`: Cria uma exceção para o cenário onde um código ISO Alpha-2 é fornecido como vazio ou em branco.
    *   `IllegalArgumentException createIllegalArgumentExceptionByInvalid(String isoAlpha2Code)`: Cria uma exceção para o cenário onde um código ISO Alpha-2 é inválido (não reconhecido).

##### 8.2.1. Implementação: `CountryExceptionCreatorImpl`

A classe `CountryExceptionCreatorImpl` é a implementação da interface `CountryExceptionCreator`. Ela é um `@Component` que utiliza o `MessageSource` injetado (`countryMessageSource`) para gerar mensagens de exceção internacionalizadas e localizadas, baseando-se no `LocaleContextHolder.getLocale()`. Isso garante que as mensagens de erro sejam configuráveis e apresentadas no idioma correto para o usuário, promovendo a consistência e a facilidade de manutenção das mensagens de erro na aplicação.

*   **Localização**: `caio.portfolio.livraria.service.country.implementation.create`
*   **Funcionalidade**: Centraliza a criação de exceções para operações de país, provendo mensagens dinâmicas e localizadas.
*   **Dependências**: `MessageSource` (para internacionalização das mensagens).

#### 8.3. `CreateOrFindCountryResolver`

Define o contrato para resolver a operação de criação ou busca de um país, delegando a responsabilidade de construir o resultado (`CountryResultImplDTO`) após a lógica de verificação de existência.

*   **Localização**: `caio.portfolio.livraria.service.country.model.create`
*   **Métodos**:
    *   `CountryResultImplDTO returnResultWithExistentCountry(String isoAlpha2Code)`: Retorna um `CountryResultImplDTO` indicando que um país existente foi encontrado.
    *   `CountryResultImplDTO returnResultWithNewCountry(String isoAlpha2Code)`: Retorna um `CountryResultImplDTO` indicando que um novo país foi criado.

##### 8.3.1. Implementação: `CreateOrFindCountryResolverImpl`

A classe `CreateOrFindCountryResolverImpl` é a implementação concreta da interface `CreateOrFindCountryResolver`. Este componente `@Component` é responsável por orquestrar a lógica para determinar se um país já existe no banco de dados ou se precisa ser criado, e então construir o `CountryResultImplDTO` apropriado.

*   **Localização**: `caio.portfolio.livraria.service.country.implementation.create`
*   **Funcionalidade**:
    *   **`returnResultWithExistentCountry(String isoAlpha2Code)`**:
        *   Consulta o `CountryRepository` para verificar a existência de um país com o `isoAlpha2Code` fornecido.
        *   Se o país for encontrado, utiliza o `ResponseCountryDTOCreator` para mapear a entidade `Country` para um `ResponseCountryDTO` e retorna um `CountryResultImplDTO` com `created = false`.
        *   Retorna `null` se o país não for encontrado por este método (indicando que ele precisa ser criado).
    *   **`returnResultWithNewCountry(String isoAlpha2Code)`**:
        *   Utiliza o `CountryValidator` para obter o nome válido do país a partir do `isoAlpha2Code`.
        *   Cria uma nova entidade `Country` com o código ISO e o nome resolvido.
        *   Persiste a nova entidade `Country` no banco de dados usando `CountrySaverAndConcurrencyHandle`, que também lida com possíveis conflitos de concorrência.
        *   Mapeia a entidade `Country` persistida para um `ResponseCountryDTO` através do `ResponseCountryDTOCreator`.
        *   Retorna um `CountryResultImplDTO` com o `ResponseCountryDTO` do país recém-criado e `created = true`.
*   **Dependências**:
    *   `CountryRepository`: Para acesso a dados de países.
    *   `CountryValidator`: Para validar códigos ISO e resolver nomes de países.
    *   `ResponseCountryDTOCreator`: Para converter entidades `Country` em `ResponseCountryDTO`s.
    *   `CountrySaverAndConcurrencyHandle`: Para salvar e tratar concorrência na persistência de `Country`.

#### 8.4. `ResponseCountryDTOCreator`

Define o contrato para mapear a entidade `Country` para seu respectivo `ResponseCountryDTO`. Isso centraliza a lógica de conversão, mantendo a responsabilidade de construção de DTOs separada das entidades e serviços, promovendo um design mais limpo e testável.

*   **Localização**: `caio.portfolio.livraria.service.country.model.create`
*   **Métodos**:
    *   `ResponseCountryDTO toResponseCountryDTO(Country country)`: Recebe uma entidade `Country` e retorna uma instância de `ResponseCountryDTO` preenchida com os dados do país.

##### 8.4.1. Implementação: `ResponseCountryDTOCreatorImpl`

A classe `ResponseCountryDTOCreatorImpl` é a implementação da interface `ResponseCountryDTOCreator`. Este componente `@Component` é responsável por realizar o mapeamento direto de uma entidade `Country` para o seu DTO de resposta correspondente, `ResponseCountryDTO`. Ele garante que apenas os dados relevantes da entidade sejam expostos na camada de API, seguindo o padrão DTO e promovendo a separação de responsabilidades.

*   **Localização**: `caio.portfolio.livraria.service.country.implementation.create`
*   **Funcionalidade**:
    *   **`toResponseCountryDTO(Country country)`**: Transforma uma entidade `Country` em um `ResponseCountryDTO`, copiando os atributos `id`, `isoAlpha2Code` e `name` para o DTO.
*   **Dependências**: Nenhuma dependência externa injetada, mas utiliza o padrão `builder` da classe `ResponseCountryDTO`.

#### 8.5. `CountryFinder`

Define métodos para buscar entidades `Country` por diferentes critérios, abstraindo a camada de acesso a dados para a recuperação de países.

*   **Localização**: `caio.portfolio.livraria.service.country.model.find`
*   **Métodos**:
    *   `Country findByIsoAlpha2Code(String isoAlpha2Code)`: Busca e retorna uma entidade `Country` pelo seu código ISO Alpha-2.
    *   `Country findById(Integer id)`: Busca e retorna uma entidade `Country` pelo seu ID único.

##### 8.5.1. Implementação: `CountryFinderImpl`

A classe `CountryFinderImpl` é a implementação da interface `CountryFinder`. Este `@Component` é responsável por realizar as operações de busca de entidades `Country` no banco de dados e lançar exceções `CountryNotFoundException` customizadas caso o país não seja encontrado. As mensagens de exceção são obtidas através do `MessageSource` para garantir localização e configurabilidade.

*   **Localização**: `caio.portfolio.livraria.service.country.implementation.find`
*   **Funcionalidade**:
    *   **`findByIsoAlpha2Code(String isoAlpha2Code)`**:
        *   Tenta encontrar um `Country` utilizando o `CountryRepository` pelo `isoAlpha2Code`.
        *   Caso não encontre, lança uma `CountryNotFoundException` com uma mensagem localizada (`country.not.found.iso`) que inclui o código ISO fornecido.
    *   **`findById(Integer id)`**:
        *   Tenta encontrar um `Country` utilizando o `CountryRepository` pelo `id` único.
        *   Caso não encontre, lança uma `CountryNotFoundException` com uma mensagem localizada (`country.not.found.id`) que inclui o ID fornecido.
*   **Dependências**:
    *   `CountryRepository`: Para acesso direto ao banco de dados.
    *   `MessageSource`: Para a obtenção de mensagens de erro localizadas.

#### 8.6. `CountrySaverAndConcurrencyHandle`

Define o contrato para a operação de salvar uma entidade `Country`, incorporando a lógica de tratamento de concorrência que pode surgir durante a persistência.

*   **Localização**: `caio.portfolio.livraria.service.country.model.save`
*   **Métodos**:
    *   `Country saveAndDealingConcurrency(Country country)`: Salva a entidade `Country` no banco de dados, tratando possíveis problemas de concorrência e retornando a entidade persistida.

#### 8.6. `CountrySaverAndConcurrencyHandle`

Define o contrato para a operação de salvar uma entidade `Country`, incorporando a lógica de tratamento de concorrência que pode surgir durante a persistência.

*   **Localização**: `caio.portfolio.livraria.service.country.model.save`
*   **Métodos**:
    *   `Country saveAndDealingConcurrency(Country country)`: Salva a entidade `Country` no banco de dados, tratando possíveis problemas de concorrência e retornando a entidade persistida.

##### 8.6.1. Implementação: `CountrySaverAndConcurrencyHandleImpl`

A classe `CountrySaverAndConcurrencyHandleImpl` é a implementação da interface `CountrySaverAndConcurrencyHandle`. Este `@Component` é crucial para garantir a integridade dos dados e um tratamento robusto de cenários de concorrência ao persistir entidades `Country`.

*   **Localização**: `caio.portfolio.livraria.service.country.implementation.save`
*   **Funcionalidade**:
    *   **`saveAndDealingConcurrency(Country country)`**:
        *   Tenta salvar e sincronizar (`saveAndFlush`) a entidade `Country` no banco de dados.
        *   Em caso de `DataIntegrityViolationException` (geralmente indicando uma violação de unicidade, como um `isoAlpha2Code` duplicado), a implementação assume que o conflito se deu por concorrência (outro processo salvou o mesmo país).
        *   Nesse cenário de exceção, ela tenta buscar o país que gerou o conflito pelo `isoAlpha2Code`.
        *   Se o país for encontrado (confirmando a concorrência), retorna essa instância existente para que a operação possa continuar sem erro.
        *   Se, por algum motivo, o país não for encontrado mesmo após a `DataIntegrityViolationException`, lança uma `ConcurrentCountryException` usando o `CountryExceptionCreator`, indicando uma situação inesperada de concorrência.
*   **Dependências**:
    *   `CountryRepository`: Para operações de persistência e busca.
    *   `CountryExceptionCreator`: Para criar exceções padronizadas de concorrência.

#### 8.7. `CountryNameValidator`

Define os contratos para a resolução de nomes de países a partir de seus códigos ISO Alpha-2.

*   **Localização**: `caio.portfolio.livraria.service.country.model.validate`
*   **Métodos**:
    *   `String resolveNameByIsoAlpha2Code(String isoAlpha2Code)`: Primeiro, chama `processIsoAlpha2Code` (definido em `IsoAlpha2CodeValidator`) para validar e normalizar o `isoAlpha2Code` fornecido. Em seguida, resolve o nome completo do país a partir do código ISO Alpha-2 validado, utilizando a funcionalidade `Locale.getDisplayName(Locale.ENGLISH)`.
    *   `String getNameByValidatedAndNormalizedIsoAlpha2Code(String isoAlpha2Code)`: Método interno que, a partir de um `isoAlpha2Code` já validado e normalizado, constrói um `Locale` e retorna o nome do país em inglês (`Locale.ENGLISH`).

#### 8.8. `IsoAlpha2CodeValidator`

Define o contrato para a validação e normalização de códigos ISO Alpha-2.

*   **Localização**: `caio.portfolio.livraria.service.country.model.validate`
*   **Métodos**:
    *   `String processIsoAlpha2Code(String isoAlpha2Code)`: Recebe um código ISO Alpha-2 e realiza as validações e normalizações: verifica se o código é `null` ou em branco, normaliza (remove espaços, converte para maiúsculas) e verifica se está presente no conjunto de códigos ISO válidos.

#### 8.9. `CountryValidator`

Interface composta que estende `IsoAlpha2CodeValidator` e `CountryNameValidator`, agrupando suas responsabilidades em um único contrato para validação de país.

*   **Localização**: `caio.portfolio.livraria.service.country.model.validate`
*   **Estende**: `IsoAlpha2CodeValidator`, `CountryNameValidator`

##### 8.9.1. Implementação: `CountryValidatorImpl`

A classe `CountryValidatorImpl` é a implementação principal da interface composta `CountryValidator`, responsável por consolidar a validação de códigos ISO Alpha-2 e a resolução de nomes de países. É um `@Service` que atua como um validador centralizado para o domínio de `Country`.

*   **Localização**: `caio.portfolio.livraria.service.country.implementation.validate`
*   **Funcionalidade**:
    *   **`processIsoAlpha2Code(String isoAlpha2Code)`**:
        *   Implementa a lógica para validar e normalizar um código ISO Alpha-2.
        *   Verifica se o código é `null` ou em branco, lançando uma exceção via `CountryExceptionCreator`.
        *   Normaliza o código (remove espaços e converte para maiúsculas).
        *   Verifica se o código normalizado está presente no conjunto de `validIsoCodes` (injetado via configuração, vide seção 13.1). Se não estiver, lança uma exceção.
        *   Retorna o código ISO Alpha-2 validado e normalizado.
    *   **`resolveNameByIsoAlpha2Code(String isoAlpha2Code)`**:
        *   Primeiramente, chama `processIsoAlpha2Code` para validar e normalizar o código.
        *   Em seguida, utiliza `getNameByValidatedAndNormalizedIsoAlpha2Code` para obter o nome completo do país (em inglês) a partir do código ISO validado.
    *   **`getNameByValidatedAndNormalizedIsoAlpha2Code(String validIsoAlpha2Code)`**:
        *   A partir de um código ISO Alpha-2 já validado e normalizado, constrói um objeto `Locale` e retorna o nome do país no idioma inglês (`Locale.ENGLISH`).
*   **Dependências**:
    *   `Set<String> validIsoCodes`: Conjunto de códigos ISO Alpha-2 válidos para comparação.
    *   `CountryExceptionCreator`: Para criar exceções padronizadas relacionadas a validações de país.

#### 8.10. `PublisherExceptionCreator`

Define métodos para a criação centralizada de diferentes tipos de exceções relacionadas a operações de `Publisher`, garantindo consistência nas mensagens de erro e facilitando a manutenção.

*   **Localização**: `caio.portfolio.livraria.service.publisher.model.create`
*   **Métodos**:
    *   `ConcurrentPublisherException createConcurrentPublisherException(String publisherName)`: Cria uma exceção para o cenário onde há uma tentativa de criação de uma editora com o mesmo nome em um contexto concorrente.
    *   `PublisherAlreadyExistsException createPublisherAlreadyExistsException(String fullAddress)`: Cria uma exceção para o cenário onde uma editora com o mesmo endereço completo já existe.
    *   `PublisherNotFoundException createPublisherNotFoundException(Long id)`: Cria uma exceção para o cenário onde uma editora com o ID especificado não é encontrada.

##### 8.10.1. Implementação: `PublisherExceptionCreatorImpl`

A classe `PublisherExceptionCreatorImpl` é a implementação da interface `PublisherExceptionCreator`. Este `@Component` é responsável por instanciar as exceções customizadas (`ConcurrentPublisherException`, `PublisherAlreadyExistsException`, `PublisherNotFoundException`) específicas para o domínio de `Publisher`. Ele utiliza o `MessageSource` injetado (`publisherMessageSource`) para obter mensagens de erro localizadas, garantindo respostas padronizadas e suporte à internacionalização.

*   **Localização**: `caio.portfolio.livraria.service.publisher.implementation.create`
*   **Funcionalidade**:
    *   **`createConcurrentPublisherException(String publisherName)`**: Cria uma `ConcurrentPublisherException` com uma mensagem localizada, indicando um conflito de concorrência ao manipular uma editora.
    *   **`createPublisherAlreadyExistsException(String fullAddress)`**: Cria uma `PublisherAlreadyExistsException` com uma mensagem localizada, informando que uma editora com o `fullAddress` já existe.
    *   **`createPublisherNotFoundException(Long id)`**: Cria uma `PublisherNotFoundException` com uma mensagem localizada, sinalizando que a editora com o ID especificado não foi encontrada.
*   **Dependências**:
    *   `MessageSource`: Para a obtenção de mensagens de erro localizadas.

#### 8.11. `ResponsePublisherDTOCreator`

Define o contrato para mapear a entidade `Publisher` para seu respectivo `ResponsePublisherDTO`. Isso centraliza a lógica de conversão, mantendo a responsabilidade de construção de DTOs separada das entidades e serviços, promovendo um design mais limpo e testável.

*   **Localização**: `caio.portfolio.livraria.service.publisher.model.create`
*   **Métodos**:
    *   `ResponsePublisherDTO toResponsePublisherDTO(Publisher publisher)`: Recebe uma entidade `Publisher` e retorna uma instância de `ResponsePublisherDTO` preenchida com os dados da editora.

##### 8.11.1. Implementação: `ResponsePublisherDTOCreatorImpl`

A classe `ResponsePublisherDTOCreatorImpl` é a implementação da interface `ResponsePublisherDTOCreator`. Este `@Component` é responsável por converter uma entidade `Publisher` em um `ResponsePublisherDTO`, que é a representação dos dados da editora para ser retornada na API. Ele realiza o mapeamento dos atributos `id`, `name`, `fullAddress` e o `id` da entidade `Country` associada para o DTO.

*   **Localização**: `caio.portfolio.livraria.service.publisher.implementation.create`
*   **Funcionalidade**:
    *   **`toResponsePublisherDTO(Publisher publisher)`**: Transforma uma entidade `Publisher` em um `ResponsePublisherDTO`, copiando os atributos `id`, `name`, `fullAddress` e o `id` do `Country` associado.
*   **Dependências**: Nenhuma dependência externa injetada, mas utiliza o padrão `builder` da classe `ResponsePublisherDTO`.

#### 8.12. `PublisherFinder`

Define métodos para buscar entidades `Publisher` por diferentes critérios, abstraindo a camada de acesso a dados para a recuperação de editoras.

*   **Localização**: `caio.portfolio.livraria.service.publisher.model.find`
*   **Métodos**:
    *   `Publisher findByFullAddress(String fullAddress)`: Busca e retorna uma entidade `Publisher` pelo seu endereço completo.
    *   `Publisher findById(Long id)`: Busca e retorna uma entidade `Publisher` pelo seu ID único.

##### 8.12.1. Implementação: `PublisherFinderImpl`

A classe `PublisherFinderImpl` é a implementação da interface `PublisherFinder`. Este `@Component` é responsável por realizar as operações de busca de entidades `Publisher` no banco de dados, utilizando o `PublisherRepository`. Em caso de a editora não ser encontrada por ID ou endereço completo, ele lança uma `PublisherNotFoundException` customizada, garantindo que a aplicação responda de forma consistente. As mensagens de erro são obtidas através do `MessageSource` para suportar internacionalização.

*   **Localização**: `caio.portfolio.livraria.service.publisher.implementation.find`
*   **Funcionalidade**:
    *   **`findByFullAddress(String fullAddress)`**:
        *   Tenta encontrar um `Publisher` utilizando o `PublisherRepository` pelo `fullAddress`.
        *   Caso não encontre, lança uma `PublisherNotFoundException` com uma mensagem localizada (`publisher.not.found.fullAddress`) que inclui o endereço fornecido.
    *   **`findById(Long id)`**:
        *   Tenta encontrar um `Publisher` utilizando o `PublisherRepository` pelo `id`.
        *   Caso não encontre, lança uma `PublisherNotFoundException` com uma mensagem localizada (`publisher.not.found.id`) que inclui o ID fornecido.
*   **Dependências**:
    *   `PublisherRepository`: Para acesso direto ao banco de dados.
    *   `MessageSource`: Para a obtenção de mensagens de erro localizadas.

#### 8.13. `PublisherSaverAndConcurrencyHandle`

Define o contrato para a operação de salvar uma entidade `Publisher`, incorporando a lógica de tratamento de concorrência que pode surgir durante a persistência.

*   **Localização**: `caio.portfolio.livraria.service.publisher.model.save`
*   **Métodos**:
    *   `Publisher saveAndHandlePublisherConcurrency(Publisher publisher)`: Salva a entidade `Publisher` no banco de dados, tratando possíveis problemas de concorrência e retornando a entidade persistida.

##### 8.13.1. Implementação: `PublisherSaverAndConcurrencyHandleImpl`

A classe `PublisherSaverAndConcurrencyHandleImpl` é a implementação da interface `PublisherSaverAndConcurrencyHandle`. Este `@Component` é responsável por persistir entidades `Publisher` e lidar proativamente com cenários de concorrência durante o processo de salvamento.

*   **Localização**: `caio.portfolio.livraria.service.publisher.implementation.save`
*   **Funcionalidade**:
    *   **`saveAndHandlePublisherConcurrency(Publisher publisher)`**:
        *   Tenta salvar e sincronizar (`saveAndFlush`) a entidade `Publisher` no banco de dados.
        *   Em caso de `DataIntegrityViolationException` (geralmente indicando uma violação de unicidade, como um `fullAddress` duplicado), a implementação assume que o conflito se deu por concorrência (outra transação salvou a mesma editora).
        *   Nesse cenário, ela tenta buscar a editora que gerou o conflito pelo `fullAddress`.
        *   Se a editora for encontrada (confirmando a concorrência), retorna essa instância existente, permitindo que a operação continue sem erro.
        *   Se, por algum motivo, a editora não for encontrada mesmo após a `DataIntegrityViolationException`, lança uma `ConcurrentPublisherException` utilizando o `PublisherExceptionCreator`, indicando uma situação inesperada de concorrência.
*   **Dependências**:
    *   `PublisherRepository`: Para operações de persistência e busca de editoras.
    *   `PublisherExceptionCreator`: Para criar exceções padronizadas de concorrência.

#### 8.14. `PublisherUpdateValidator`

Define o contrato para validações específicas durante a atualização de uma entidade `Publisher`, permitindo a verificação de campos como nome, país e endereço completo.

*   **Localização**: `caio.portfolio.livraria.service.publisher.model.validate`
*   **Métodos**:
    *   `String validateName(String currentName, String newName)`: Valida e retorna o nome da editora após uma tentativa de atualização, considerando o nome atual e o novo nome.
    *   `Country validateCountry(Country currentCountry, Integer newCountryId)`: Valida e retorna a entidade `Country` associada à editora após uma tentativa de atualização do país, considerando o país atual e o ID do novo país.
    *   `String validateFullAddress(String currentAddress, String newAddress)`: Valida e retorna o endereço completo da editora após uma tentativa de atualização, considerando o endereço atual e o novo endereço.

##### 8.14.1. Implementação: `PublisherUpdateValidatorImpl`

A classe `PublisherUpdateValidatorImpl` é a implementação da interface `PublisherUpdateValidator`. Este `@Component` é responsável por validar cada campo de uma entidade `Publisher` durante uma operação de atualização, aplicando regras de negócio e verificando a unicidade quando necessário. Ele garante que apenas dados válidos e não conflitantes sejam aplicados à entidade `Publisher` existente.

*   **Localização**: `caio.portfolio.livraria.service.publisher.implementation.validate`
*   **Funcionalidade**:
    *   **`validateName(String currentName, String newName)`**:
        *   Verifica se um `newName` foi fornecido e se é diferente do `currentName`.
        *   Se sim, retorna `newName`. Caso contrário, retorna `currentName`.
    *   **`validateCountry(Country currentCountry, Integer newCountryId)`**:
        *   Verifica se um `newCountryId` foi fornecido e se é diferente do ID do `currentCountry`.
        *   Se sim, utiliza o `CountryService` para buscar a entidade `Country` correspondente ao `newCountryId`.
        *   Se `newCountryId` for nulo ou igual ao existente, retorna `currentCountry`.
        *   (Assumindo que `countryService.getCountryById` lançará uma exceção se o ID do país não for encontrado).
    *   **`validateFullAddress(String currentAddress, String newAddress)`**:
        *   Verifica se um `newAddress` foi fornecido e se é diferente do `currentAddress`.
        *   Se sim, consulta o `PublisherRepository` para verificar se o `newAddress` já está sendo utilizado por outra editora.
        *   Se o endereço já existe, lança uma `PublisherAlreadyExistsException` usando o `PublisherExceptionCreator`.
        *   Caso contrário, retorna `newAddress`. Se `newAddress` for nulo ou igual ao existente, retorna `currentAddress`.
*   **Dependências**:
    *   `PublisherRepository`: Para verificar a unicidade do `fullAddress`.
    *   `PublisherExceptionCreator`: Para criar exceções relacionadas a conflitos de `fullAddress`.
    *   `CountryService`: Para buscar entidades `Country` por ID.

#### 8.15. `AuthorExceptionCreator`

Define métodos para a criação centralizada de diferentes tipos de exceções relacionadas a operações de `Author`, garantindo consistência nas mensagens de erro e facilitando a manutenção.

*   **Localização**: `caio.portfolio.livraria.service.author.model.create`
*   **Métodos**:
    *   `AuthorNotFoundException createAuthorNotFoundException(Long id)`: Cria uma exceção para o cenário onde um autor com o ID especificado não é encontrado.
    *   `ConcurrentAuthorException createConcurrentAuthorException(String authorFullName)`: Cria uma exceção para o cenário onde há uma tentativa de criação ou atualização concorrente de um autor, geralmente com base no nome completo.
    *   `AuthorAlreadyExistsException createAuthorAlreadyExistsException(String alias, String authorFullName)`: Cria uma exceção para o cenário onde um autor com o mesmo pseudônimo (alias) ou nome completo já existe no sistema.

##### 8.15.1. Implementação: `AuthorExceptionCreatorImpl`

A classe `AuthorExceptionCreatorImpl` é a implementação da interface `AuthorExceptionCreator`. Este `@Component` é responsável por criar instâncias de exceções customizadas (`ConcurrentAuthorException`, `AuthorAlreadyExistsException`, `AuthorNotFoundException`) específicas para o domínio de `Author`. Ele utiliza o `MessageSource` injetado (`authorMessageSource`) para obter mensagens de erro localizadas e formatadas, garantindo que as respostas de erro sejam claras e padronizadas, além de suportar internacionalização.

*   **Localização**: `caio.portfolio.livraria.service.author.implementation.create`
*   **Funcionalidade**:
    *   **`createConcurrentAuthorException(String authorFullName)`**: Cria uma `ConcurrentAuthorException` com uma mensagem localizada, indicando um conflito de concorrência ao manipular um autor.
    *   **`createAuthorAlreadyExistsException(String alias, String authorFullName)`**: Cria uma `AuthorAlreadyExistsException` com uma mensagem localizada, informando que um autor com o alias ou nome completo já existe.
    *   **`createAuthorNotFoundException(Long id)`**: Cria uma `AuthorNotFoundException` com uma mensagem localizada, sinalizando que o autor com o ID especificado não foi encontrado.
*   **Dependências**:
    *   `MessageSource`: Para a obtenção de mensagens de erro localizadas.

#### 8.16. `ResponseAuthorDTOCreator`

Define o contrato para mapear a entidade `Author` para seu respectivo `ResponseAuthorDTO`. Isso centraliza a lógica de conversão, mantendo a responsabilidade de construção de DTOs separada das entidades e serviços, promovendo um design mais limpo e testável.

*   **Localização**: `caio.portfolio.livraria.service.author.model.create`
*   **Métodos**:
    *   `ResponseAuthorDTO toResponseAuthorDTO(Author author)`: Recebe uma entidade `Author` e retorna uma instância de `ResponseAuthorDTO` preenchida com os dados do autor.

##### 8.16.1. Implementação: `ResponseAuthorDTOCreatorImpl`

A classe `ResponseAuthorDTOCreatorImpl` é a implementação da interface `ResponseAuthorDTOCreator`. Este `@Component` é responsável por mapear uma entidade `Author` para o seu DTO de resposta, `ResponseAuthorDTO`. Ele realiza a cópia dos atributos relevantes da entidade (`id`, `alias`, `fullName`, `birthday`) para o DTO, incluindo o `countryId` obtido da entidade `Country` associada. Esta implementação garante que a representação do autor na API seja consistente e contenha as informações necessárias.

*   **Localização**: `caio.portfolio.livraria.service.author.implementation.create`
*   **Funcionalidade**:
    *   **`toResponseAuthorDTO(Author author)`**: Converte uma entidade `Author` em um `ResponseAuthorDTO`, populando os campos correspondentes, incluindo o `id` do país (`country.getId()`).
*   **Dependências**: Nenhuma dependência externa injetada, mas utiliza o padrão `builder` da classe `ResponseAuthorDTO`.

#### 8.17. `AuthorFinder`

Define métodos para buscar entidades `Author` por diferentes critérios, abstraindo a camada de acesso a dados para a recuperação de autores.

*   **Localização**: `caio.portfolio.livraria.service.author.model.find`
*   **Métodos**:
    *   `Author findById(Long id)`: Busca e retorna uma entidade `Author` pelo seu ID único.
    *   `Author findByAlias(String alias)`: Busca e retorna uma entidade `Author` pelo seu pseudônimo (alias).

##### 8.17.1. Implementação: `AuthorFinderImpl`

A classe `AuthorFinderImpl` é a implementação da interface `AuthorFinder`. Este `@Component` é responsável por realizar as operações de busca de entidades `Author` no banco de dados, utilizando o `AuthorRepository`. Em caso de o autor não ser encontrado por ID ou alias, ele lança uma `AuthorNotFoundException` customizada, garantindo que a aplicação responda de forma consistente. As mensagens de erro são obtidas através do `MessageSource` para suportar internacionalização.

*   **Localização**: `caio.portfolio.livraria.service.author.implementation.find`
*   **Funcionalidade**:
    *   **`findById(Long id)`**:
        *   Tenta encontrar um `Author` utilizando o `AuthorRepository` pelo `id`.
        *   Caso não encontre, lança uma `AuthorNotFoundException` com uma mensagem localizada (`author.not.found.id`) que inclui o ID fornecido.
    *   **`findByAlias(String alias)`**:
        *   Tenta encontrar um `Author` utilizando o `AuthorRepository` pelo `alias`.
        *   Caso não encontre, lança uma `AuthorNotFoundException` com uma mensagem localizada (`author.not.found.alias`) que inclui o alias fornecido.
*   **Dependências**:
    *   `AuthorRepository`: Para acesso direto ao banco de dados.
    *   `MessageSource`: Para a obtenção de mensagens de erro localizadas.

#### 8.18. `AuthorSaverAndConcurrencyHandle`

Define o contrato para a operação de salvar uma entidade `Author`, incorporando a lógica de tratamento de concorrência que pode surgir durante a persistência.

*   **Localização**: `caio.portfolio.livraria.service.author.model.save`
*   **Métodos**:
    *   `Author saveAndHandleConcurrentyAuthor(Author author)`: Salva a entidade `Author` no banco de dados, tratando possíveis problemas de concorrência e retornando a entidade persistida.

##### 8.18.1. Implementação: `AuthorSaverAndConcurrencyHandleImpl`

A classe `AuthorSaverAndConcurrencyHandleImpl` é a implementação da interface `AuthorSaverAndConcurrencyHandle`. Este `@Component` lida com a persistência de entidades `Author` e é especialmente projetado para gerenciar cenários de concorrência durante o salvamento.

*   **Localização**: `caio.portfolio.livraria.service.author.implementation.save`
*   **Funcionalidade**:
    *   **`saveAndHandleConcurrentyAuthor(Author author)`**:
        *   Tenta salvar e sincronizar (`saveAndFlush`) a entidade `Author` no banco de dados.
        *   Em caso de `DataIntegrityViolationException` (indicando uma violação de unicidade, como um `alias` duplicado), assume que o conflito ocorreu devido a uma operação concorrente.
        *   Tenta, então, buscar o autor existente usando o `alias` fornecido.
        *   Se o autor for encontrado (confirmando que outro processo o salvou), retorna essa instância existente para que o fluxo da aplicação continue sem interrupção.
        *   Se, por alguma razão, o autor não for encontrado mesmo após a `DataIntegrityViolationException`, ele lança uma `ConcurrentAuthorException` customizada, criada pelo `AuthorExceptionCreator`, indicando um problema de concorrência.
*   **Dependências**:
    *   `AuthorRepository`: Para operações de persistência e busca de autores.
    *   `AuthorExceptionCreator`: Para criar exceções padronizadas de concorrência.

#### 8.19. `AuthorUpdateValidator`

Define o contrato para validações específicas durante a atualização de uma entidade `Author`, permitindo a verificação de campos como pseudônimo, nome completo, data de nascimento e país.

*   **Localização**: `caio.portfolio.livraria.service.author.model.validate`
*   **Métodos**:
    *   `String validateAlias(String existingAlias, String aliasToUpdate)`: Valida e retorna o pseudônimo do autor após uma tentativa de atualização, considerando o pseudônimo existente e o novo pseudônimo.
    *   `String validateFullName(String existingFullName, String fullNameToUpdate)`: Valida e retorna o nome completo do autor após uma tentativa de atualização, considerando o nome completo existente e o novo nome completo.
    *   `LocalDate validateBirthday(LocalDate existingBirthday, LocalDate birthdayToUpdate)`: Valida e retorna a data de nascimento do autor após uma tentativa de atualização, considerando a data de nascimento existente e a nova data.
    *   `Country validateCountry(Country existingCountry, Integer countryIdToUpdate)`: Valida e retorna a entidade `Country` associada ao autor após uma tentativa de atualização do país, considerando o país existente e o ID do novo país.

##### 8.19.1. Implementação: `AuthorUpdateValidatorImpl`

A classe `AuthorUpdateValidatorImpl` é a implementação da interface `AuthorUpdateValidator`. Este `@Component` é responsável por validar cada campo de um `Author` durante uma operação de atualização, aplicando regras de negócio e verificando a unicidade quando necessário. Ele garante que apenas dados válidos e não conflitantes sejam aplicados à entidade `Author` existente.

*   **Localização**: `caio.portfolio.livraria.service.author.implementation.validate`
*   **Funcionalidade**:
    *   **`validateAlias(String existingAlias, String aliasToUpdate)`**:
        *   Verifica se um novo `aliasToUpdate` foi fornecido e se é diferente do `existingAlias`.
        *   Se sim, consulta o `AuthorRepository` para verificar se o `aliasToUpdate` já está sendo utilizado por outro autor.
        *   Se o alias já existe, lança uma `AuthorAlreadyExistsException` usando o `AuthorExceptionCreator`.
        *   Caso contrário, retorna o `aliasToUpdate`. Se `aliasToUpdate` for nulo ou igual ao existente, retorna `existingAlias`.
    *   **`validateFullName(String existingFullName, String fullNameToUpdate)`**:
        *   Se `fullNameToUpdate` for diferente de `null` e diferente de `existingFullName`, retorna `fullNameToUpdate`.
        *   Caso contrário, retorna `existingFullName`.
    *   **`validateBirthday(LocalDate existingBirthday, LocalDate birthdayToUpdate)`**:
        *   Se `birthdayToUpdate` for diferente de `null` e diferente de `existingBirthday`, retorna `birthdayToUpdate`.
        *   Caso contrário, retorna `existingBirthday`.
    *   **`validateCountry(Country existingCountry, Integer countryIdToUpdate)`**:
        *   Verifica se um `countryIdToUpdate` foi fornecido e se é diferente do ID do `existingCountry`.
        *   Se sim, utiliza o `CountryService` para buscar a entidade `Country` correspondente ao `countryIdToUpdate`.
        *   Se `countryIdToUpdate` for nulo ou igual ao existente, retorna `existingCountry`.
        *   (Assumindo que `countryService.getCountryById` lançará uma exceção se o ID do país não for encontrado).
*   **Dependências**:
    *   `AuthorRepository`: Para verificar a unicidade do alias.
    *   `AuthorExceptionCreator`: Para criar exceções relacionadas a conflitos de alias.
    *   `CountryService`: Para buscar entidades `Country` por ID.

#### 8.20. `ResponseSalableBookDTOCreator`

Define o contrato para mapear a entidade `SalableBook` para seu respectivo `ResponseSalableBookDTO`. Isso centraliza a lógica de conversão, mantendo a responsabilidade de construção de DTOs separada das entidades e serviços, promovendo um design mais limpo e testável.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.model.create`
*   **Métodos**:
    *   `ResponseSalableBookDTO toResponseSalableBookDTO(SalableBook book)`: Recebe uma entidade `SalableBook` e retorna uma instância de `ResponseSalableBookDTO` preenchida com os dados do livro vendável.

##### 8.20.1. Implementação: `ResponseSalableBookDTOCreatorImpl`

A classe `ResponseSalableBookDTOCreatorImpl` é a implementação da interface `ResponseSalableBookDTOCreator`. Este `@Component` é responsável por converter uma entidade `SalableBook` em um `ResponseSalableBookDTO`, que é a representação dos dados do livro vendável para ser retornada na API. Ele realiza o mapeamento dos atributos como `id`, `title`, `genre`, `isbn`, `price`, `units`, e os IDs das entidades `Author` e `Publisher` associadas para o DTO.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.implementation.create`
*   **Funcionalidade**:
    *   **`toResponseSalableBookDTO(SalableBook book)`**: Transforma uma entidade `SalableBook` em um `ResponseSalableBookDTO`, copiando os atributos relevantes e os IDs das entidades relacionadas.
*   **Dependências**: Nenhuma dependência externa injetada, mas utiliza o padrão `builder` da classe `ResponseSalableBookDTO`.

#### 8.21. `SalableBookExceptionCreator`

Define métodos para a criação centralizada de diferentes tipos de exceções relacionadas a operações de `SalableBook`, garantindo consistência nas mensagens de erro e facilitando a manutenção.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.model.create`
*   **Métodos**:
    *   `SalableBookAlreadyExistsException createSalableBookAlreadyExistsException(String authorName, String title)`: Cria uma exceção para o cenário onde um livro vendável com o mesmo autor e título já existe no sistema.
    *   `ConcurrentSalableBookException createConcurrentSalableBookException(String title)`: Cria uma exceção para o cenário onde há uma tentativa de criação ou atualização concorrente de um livro vendável, geralmente com base no título.
    *   `SalableBookNotFoundException createSalableBookNotFoundException(Long id)`: Cria uma exceção para o cenário onde um livro vendável com o ID especificado não é encontrado.
    *   `InsuficientSalableBookUnitsException createInsuficientSalableBookUnitsException(int units)`: Cria uma exceção para o cenário onde a quantidade de unidades disponíveis para venda é insuficiente.

##### 8.21.1. Implementação: `SalableBookExceptionCreatorImpl`

A classe `SalableBookExceptionCreatorImpl` é a implementação da interface `SalableBookExceptionCreator`. Este `@Component` é responsável por instanciar as exceções customizadas (`SalableBookAlreadyExistsException`, `ConcurrentSalableBookException`, `SalableBookNotFoundException`, `InsuficientSalableBookUnitsException`) específicas para o domínio de `SalableBook`. Ele utiliza o `MessageSource` injetado (`salableBookMessageSource`) para obter mensagens de erro localizadas, garantindo respostas padronizadas e suporte à internacionalização.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.implementation.create`
*   **Funcionalidade**:
    *   **`createSalableBookAlreadyExistsException(String authorName, String title)`**: Cria uma `SalableBookAlreadyExistsException` com uma mensagem localizada que informa sobre a duplicidade de um livro pelo título e autor.
    *   **`createConcurrentSalableBookException(String title)`**: Cria uma `ConcurrentSalableBookException` com uma mensagem localizada, indicando um conflito de concorrência ao manipular um livro vendável.
    *   **`createSalableBookNotFoundException(Long id)`**: Cria uma `SalableBookNotFoundException` com uma mensagem localizada, sinalizando que o livro vendável com o ID especificado não foi encontrado.
    *   **`createInsuficientSalableBookUnitsException(int units)`**: Cria uma `InsuficientSalableBookUnitsException` com uma mensagem localizada, indicando que a quantidade de unidades de livro é insuficiente para uma operação.
*   **Dependências**:
    *   `MessageSource`: Para a obtenção de mensagens de erro localizadas.

#### 8.22. `SalableBookFinder`

Define métodos para buscar entidades `SalableBook` por diferentes critérios, abstraindo a camada de acesso a dados para a recuperação de livros vendáveis.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.model.find`
*   **Métodos**:
    *   `SalableBook findById(Long id)`: Busca e retorna uma entidade `SalableBook` pelo seu ID único.
    *   `List<SalableBook> findByAuthor(Author author)`: Busca e retorna uma lista de `SalableBook`s associados a um determinado autor.
    *   `List<SalableBook> findByPublisher(Publisher publisher)`: Busca e retorna uma lista de `SalableBook`s associados a uma determinada editora.
    *   `List<SalableBook> findByTitle(String title)`: Busca e retorna uma lista de `SalableBook`s que contenham o título especificado.
    *   `List<SalableBook> findByGenre(Genre genre)`: Busca e retorna uma lista de `SalableBook`s de um gênero específico.
    *   `List<SalableBook> findByIsbn(String isbn)`: Busca e retorna uma lista de `SalableBook`s pelo seu ISBN.
    *   `boolean existsById(Long bookId)`: Verifica a existência de um `SalableBook` pelo seu ID.

##### 8.22.1. Implementação: `SalableBookFinderImpl`

A classe `SalableBookFinderImpl` é a implementação da interface `SalableBookFinder`. Este `@Component` é responsável por realizar as operações de busca de entidades `SalableBook` no banco de dados, utilizando o `SalableBookRepository`. Em caso de o livro não ser encontrado por qualquer critério de busca (ID, autor, editora, título, gênero, ISBN), ele lança uma `SalableBookNotFoundException` customizada, garantindo que a aplicação responda de forma consistente. As mensagens de erro são obtidas através do `MessageSource` para suportar internacionalização.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.implementation.find`
*   **Funcionalidade**:
    *   **`findById(Long id)`**: Tenta encontrar um `SalableBook` pelo ID. Lança `SalableBookNotFoundException` se não encontrado.
    *   **`findByAuthor(Author author)`**: Busca livros por `Author`. Lança `SalableBookNotFoundException` se a lista estiver vazia.
    *   **`findByPublisher(Publisher publisher)`**: Busca livros por `Publisher`. Lança `SalableBookNotFoundException` se a lista estiver vazia.
    *   **`findByTitle(String title)`**: Busca livros por título. Lança `SalableBookNotFoundException` se a lista estiver vazia.
    *   **`findByGenre(Genre genre)`**: Busca livros por gênero. Lança `SalableBookNotFoundException` se a lista estiver vazia.
    *   **`findByIsbn(String isbn)`**: Busca livros por ISBN. Lança `SalableBookNotFoundException` se a lista estiver vazia.
    *   **`existsById(Long bookId)`**: Verifica a existência de um livro pelo ID. Lança `SalableBookNotFoundException` se não existir.
*   **Dependências**:
    *   `SalableBookRepository`: Para acesso direto ao banco de dados.
    *   `MessageSource`: Para a obtenção de mensagens de erro localizadas.

#### 8.23. `SalableBookSaverAndConcurrencyHandle`

Define o contrato para a operação de salvar uma entidade `SalableBook`, incorporando a lógica de tratamento de concorrência que pode surgir durante a persistência.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.model.save`
*   **Métodos**:
    *   `SalableBook saveAndHandleConcurrency(SalableBook book)`: Salva a entidade `SalableBook` no banco de dados, tratando possíveis problemas de concorrência e retornando a entidade persistida.

##### 8.23.1. Implementação: `SalableBookSaverAndConcurrencyHandleImpl`

A classe `SalableBookSaverAndConcurrencyHandleImpl` é a implementação da interface `SalableBookSaverAndConcurrencyHandle`. Este `@Component` é responsável por persistir entidades `SalableBook` e lidar proativamente com cenários de concorrência durante o processo de salvamento.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.implementation.save`
*   **Funcionalidade**:
    *   **`saveAndHandleConcurrency(SalableBook book)`**:
        *   Tenta salvar e sincronizar (`saveAndFlush`) a entidade `SalableBook` no banco de dados.
        *   Em caso de `DataIntegrityViolationException` (geralmente indicando uma violação de unicidade, como um par `title` e `author` duplicado), a implementação assume que o conflito se deu por concorrência (outra transação salvou o mesmo livro).
        *   Nesse cenário, ela tenta buscar o `SalableBook` que gerou o conflito pelo `title` e `author`.
        *   Se um `SalableBook` correspondente for encontrado, isso indica que o livro já existe. Então, ele lança uma `SalableBookAlreadyExistsException` utilizando o `SalableBookExceptionCreator`.
        *   Se, por algum motivo, um `SalableBook` não for encontrado mesmo após a `DataIntegrityViolationException` (indicando um tipo diferente de violação de integridade ou um cenário de concorrência mais complexo), lança uma `ConcurrentSalableBookException` utilizando o `SalableBookExceptionCreator`.
*   **Dependências**:
    *   `SalableBookRepository`: Para operações de persistência e busca de livros.
    *   `SalableBookExceptionCreator`: Para criar exceções padronizadas de existência ou concorrência de livros.

#### 8.24. `BookSeller`

Define o contrato para a lógica de negócio principal de venda de livros, recebendo uma lista de livros a serem vendidos e retornando o valor total da venda.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.model.sell`
*   **Métodos**:
    *   `BigDecimal sellBooks(BookSellListDTO bookListDTO)`: Processa a venda de uma lista de livros e retorna o valor total da transação.

##### 8.24.1. Implementação: `BookSellerImpl`

A classe `BookSellerImpl` é a implementação da interface `BookSeller`. Este `@Component` é responsável por gerenciar a lógica de venda de livros. Ele itera sobre uma lista de itens de venda (`BookSellDTO`), verifica a disponibilidade de cada livro (`SalableBook`), diminui as unidades do estoque e calcula o valor total da transação. Em caso de unidades insuficientes, uma exceção é lançada.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.implementation.sell`
*   **Funcionalidade**:
    *   **`sellBook(Long bookId, int units)` (Método Privado)**:
        *   Localiza o `SalableBook` pelo `bookId` usando `SalableBookFinder`.
        *   Tenta diminuir a quantidade de `units` do estoque do livro.
        *   Se a quantidade em estoque for insuficiente (`IllegalArgumentException` é capturada), lança uma `InsuficientSalableBookUnitsException` usando `SalableBookExceptionCreator`.
        *   Calcula o valor parcial da venda (unidades * preço do livro).
    *   **`sellBooks(BookSellListDTO bookListDTO)`**:
        *   Percorre a lista de `BookSellDTO`s fornecida.
        *   Para cada item, chama o método privado `sellBook` para processar a venda individual.
        *   Acumula os valores parciais de cada venda para retornar o valor total final da lista de livros vendidos.
*   **Dependências**:
    *   `SalableBookFinder`: Para buscar a entidade `SalableBook` pelo ID.
    *   `SalableBookExceptionCreator`: Para criar exceções quando há unidades insuficientes ou livro não encontrado.

#### 8.25. `SalableBookUniquenessValidator`

Define o contrato para validações de unicidade de `SalableBook`s, especificamente para a combinação de título e autor, tanto na criação quanto na atualização.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.model.validate`
*   **Métodos**:
    *   `void validateUniquenessOnUpdate(TitleAndAuthorUpdateDTO titleAndAuthorUpdateDTO, String title, Long authorId)`: Valida a unicidade de um livro vendável durante uma operação de atualização, verificando se a nova combinação de título e autor já existe.
    *   `void validateUniquenessOnCreate(Author author, String title)`: Valida a unicidade de um livro vendável durante uma operação de criação, verificando se a combinação de autor e título já existe.

##### 8.25.1. Implementação: `SalableBookUniquenessValidatorImpl`

A classe `SalableBookUniquenessValidatorImpl` é a implementação da interface `SalableBookUniquenessValidator`. Este `@Component` é responsável por aplicar regras de negócio para garantir que não haja duplicidade de `SalableBook` na base de dados, considerando a combinação de título e autor como chave de unicidade. Ele lança exceções específicas (`SalableBookAlreadyExistsException`) quando uma violação de unicidade é detectada.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.implementation.validate`
*   **Funcionalidade**:
    *   **`validateUniquenessOnUpdate(TitleAndAuthorUpdateDTO titleAndAuthorUpdateDTO, String title, Long authorId)`**:
        *   Verifica se o novo título ou o novo ID do autor (presentes no `titleAndAuthorUpdateDTO`) são diferentes dos valores atuais (`title`, `authorId`) do livro que está sendo atualizado.
        *   Se houver mudança na combinação título/autor, busca no `SalableBookRepository` se já existe um livro com o novo título e autor.
        *   Se um livro existente for encontrado, lança uma `SalableBookAlreadyExistsException` usando o `SalableBookExceptionCreator`.
    *   **`validateUniquenessOnCreate(Author author, String title)`**:
        *   Busca no `SalableBookRepository` se já existe um livro com o `title` e `author` fornecidos.
        *   Se um livro existente for encontrado, lança uma `SalableBookAlreadyExistsException` usando o `SalableBookExceptionCreator`.
*   **Dependências**:
    *   `SalableBookRepository`: Para verificar a existência de livros.
    *   `SalableBookExceptionCreator`: Para criar exceções padronizadas de livros já existentes.

#### 8.26. `SalableBookUpdateValidator`

Define o contrato para validações específicas durante a atualização de uma entidade `SalableBook`, permitindo a verificação de campos como título, autor, editora, gênero, ISBN, preço e unidades.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.model.validate`
*   **Métodos**:
    *   `TitleAndAuthorUpdateDTO validateTitleAndAuthor(String currentTitle, String newTitle, Author currentAuthor, Long newAuthorId)`: Valida e retorna um DTO contendo o título e autor atualizados, considerando os valores existentes e os novos.
    *   `Publisher validatePublisher(Publisher currentPublisher, Long newPublisherId)`: Valida e retorna a entidade `Publisher` associada ao livro após uma tentativa de atualização da editora.
    *   `Genre validateGenre(Genre currentGenre, Genre newGenre)`: Valida e retorna o gênero do livro após uma tentativa de atualização.
    *   `String validateIsbn(String currentIsbn, String newIsbn)`: Valida e retorna o ISBN do livro após uma tentativa de atualização.
    *   `BigDecimal validatePrice(BigDecimal currentPrice, BigDecimal newPrice)`: Valida e retorna o preço do livro após uma tentativa de atualização.
    *   `Integer validateUnits(Integer currentUnits, Integer newUnits)`: Valida e retorna a quantidade de unidades em estoque do livro após uma tentativa de atualização.

##### 8.26.1. Implementação: `SalableBookUpdateValidatorImpl`

A classe `SalableBookUpdateValidatorImpl` é a implementação da interface `SalableBookUpdateValidator`. Este `@Component` é responsável por validar cada campo de um `SalableBook` que está sendo atualizado. Para cada campo, ele verifica se um novo valor foi fornecido e se é diferente do valor atual. Se for diferente, o novo valor é retornado; caso contrário, o valor atual é mantido. Para `Author` e `Publisher`, ele utiliza os serviços correspondentes para buscar a entidade pelo ID.

*   **Localização**: `caio.portfolio.livraria.service.book.salable.implementation.validate`
*   **Funcionalidade**:
    *   **`validateTitleAndAuthor(String title, String newTitle, Author author, Long newAuthorId)`**:
        *   Determina o título e o autor a serem usados na atualização. Se `newTitle` ou `newAuthorId` forem fornecidos e diferentes, eles substituem os valores atuais.
        *   Caso o `newAuthorId` seja fornecido e diferente, o `AuthorService` é utilizado para obter a entidade `Author` correspondente.
        *   Retorna um `TitleAndAuthorUpdateDTO` com os valores finais.
    *   **`validatePublisher(Publisher currentPublisher, Long newPublisherId)`**:
        *   Se `newPublisherId` for fornecido e diferente do ID do `currentPublisher`, usa `publisherService` para buscar o novo `Publisher`.
        *   Caso contrário, retorna `currentPublisher`.
    *   **`validateGenre(Genre currentGenre, Genre newGenre)`**:
        *   Retorna `newGenre` se este for fornecido e diferente de `currentGenre`, caso contrário, retorna `currentGenre`.
    *   **`validateIsbn(String currentIsbn, String newIsbn)`**:
        *   Retorna `newIsbn` se este for fornecido e diferente de `currentIsbn`, caso contrário, retorna `currentIsbn`.
    *   **`validatePrice(BigDecimal currentPrice, BigDecimal newPrice)`**:
        *   Retorna `newPrice` se este for fornecido e diferente de `currentPrice`, caso contrário, retorna `currentPrice`.
    *   **`validateUnits(Integer currentUnits, Integer newUnits)`**:
        *   Retorna `newUnits` se este for fornecido e diferente de `currentUnits`, caso contrário, retorna `currentUnits`.
*   **Dependências**:
    *   `AuthorService`: Para buscar entidades `Author` por ID.
    *   `PublisherService`: Para buscar entidades `Publisher` por ID.

#### 8.27. `SerializationExceptionCreator`

Define métodos para a criação de exceções do tipo `InputMismatchException` relacionadas a erros de serialização ou desserialização de dados, como quando um formato de entrada inesperado é encontrado.

*   **Localização**: `caio.portfolio.livraria.serialization.model`
*   **Métodos**:
    *   `InputMismatchException createInputMismatchException()`: Cria uma exceção genérica para erros de incompatibilidade de entrada.
    *   `InputMismatchException createInputMismatchException(String value)`: Cria uma exceção para erros de incompatibilidade de entrada, fornecendo uma mensagem mais específica sobre o valor que causou o problema.

##### 8.27.1. Implementação: `SerializationExceptionCreatorImpl`

A classe `SerializationExceptionCreatorImpl` é a implementação da interface `SerializationExceptionCreator`. Este `@Component` é especializado na criação de exceções do tipo `InputMismatchException` para lidar com falhas na serialização ou desserialização de dados. Ele utiliza o `MessageSource` injetado para obter mensagens de erro localizadas, garantindo que as respostas da API sejam claras e padronizadas mesmo em cenários de dados malformados.

*   **Localização**: `caio.portfolio.livraria.serialization.implementation`
*   **Funcionalidade**:
    *   **`createInputMismatchException()`**: Cria uma `InputMismatchException` com uma mensagem localizada indicando um erro genérico de formato de entrada (ex: "input.mismatch.notBlank").
    *   **`createInputMismatchException(String value)`**: Cria uma `InputMismatchException` com uma mensagem localizada que inclui o `value` que causou o problema de formato (ex: "input.mismatch.invalid.format", [value]).
*   **Dependências**:
    *   `MessageSource`: Para resolver e formatar mensagens de erro localizadas.

#### 8.28. `ExceptionHandlerMessageCreator`

Define o contrato para a criação padronizada de mensagens de erro específicas para o `ExceptionHandlerController`, garantindo consistência e localização das mensagens de exceção na aplicação.

*   **Localização**: `caio.portfolio.livraria.exception.model`
*   **Métodos**:
    *   `String assertionFailureCreateMsg()`: Retorna a mensagem de erro para falhas de asserção.
    *   `String noResourceFoundCreateMsg()`: Retorna a mensagem de erro quando um recurso não é encontrado.
    *   `String httpMessageConversionCreateMsg()`: Retorna a mensagem de erro para falhas na conversão de mensagens HTTP.
    *   `String methodArgumentNotValidCreateMsg()`: Retorna a mensagem de erro para validação de argumentos de método.
    *   `String illegalArgumentCreateMsg()`: Retorna a mensagem de erro para argumentos inválidos.
    *   `String countryNotFoundCreateMsg()`: Retorna a mensagem de erro quando um país não é encontrado.
    *   `String validationCreateMsg()`: Retorna a mensagem de erro genérica para falhas de validação.
    *   `String concurrentCountryCreateMsg()`: Retorna a mensagem de erro para exceções de concorrência ao criar um país.
    *   `String authorAlreadyExistsCreateMsg()`: Retorna a mensagem de erro quando um autor já existe.
    *   `String authorNotFoundCreateMsg()`: Retorna a mensagem de erro quando um autor não é encontrado.
    *   `String concurrentAuthorCreateMsg()`: Retorna a mensagem de erro para exceções de concorrência ao criar/atualizar um autor.
    *   `String messageNotReadableCreateMsg()`: Retorna a mensagem de erro quando uma mensagem HTTP não pode ser lida.
    *   `String publisherAlreadyExistsCreateMsg()`: Retorna a mensagem de erro quando uma editora já existe.
    *   `String concurrentPublisherCreateMsg()`: Retorna a mensagem de erro para exceções de concorrência ao criar/atualizar uma editora.
    *   `String publisherNotFoundCreateMsg()`: Retorna a mensagem de erro quando uma editora não é encontrada.
    *   `String salableBookAlreadyExistsCreateMsg()`: Retorna a mensagem de erro quando um livro vendável já existe.
    *   `String concurrentSalableBookCreateMsg()`: Retorna a mensagem de erro para exceções de concorrência ao criar/atualizar um livro vendável.
    *   `String salableBookNotFoundCreateMsg()`: Retorna a mensagem de erro quando um livro vendável não é encontrado.
    *   `String insuficientSalableBookUnitsCreateMsg()`: Retorna a mensagem de erro para quando há unidades insuficientes de um livro vendável.
    *   `String missingParameterCreateMsg(String parameterName)`: Retorna a mensagem de erro para um parâmetro ausente, incluindo o nome do parâmetro.

##### 8.28.1. Implementação: `ExceptionHandlerMessageCreatorImpl`

A classe `ExceptionHandlerMessageCreatorImpl` é a implementação da interface `ExceptionHandlerMessageCreator`. Este `@Component` é o componente central para a criação de mensagens de exceção padronizadas e internacionalizadas que serão apresentadas aos usuários ou registradas. Ele utiliza o `MessageSource` injetado (`exceptionHandlerMessageSource`) para resolver as chaves das mensagens de erro a partir de arquivos de propriedades, considerando o `LocaleContextHolder` para aplicar a localização correta.

*   **Localização**: `caio.portfolio.livraria.exception.implementation`
*   **Funcionalidade**:
    *   Implementa todos os métodos da interface `ExceptionHandlerMessageCreator`, buscando as mensagens de erro correspondentes em um arquivo de recursos de mensagens.
    *   Garante que as mensagens de erro sejam consistentes, legíveis e possam ser facilmente traduzidas para diferentes idiomas.
    *   O método `missingParameterCreateMsg` demonstra a capacidade de incluir parâmetros dinâmicos nas mensagens.
*   **Dependências**:
    *   `MessageSource`: Para resolver e formatar mensagens de erro localizadas.

#### 8.29. Interface: `UnitDecreaser`

Define o contrato para qualquer entidade que possua uma quantidade de unidades que possa ser diminuída. Esta interface é um exemplo do Princípio da Segregação de Interfaces (ISP), garantindo que as classes implementem apenas os métodos que realmente utilizam, promovendo designs mais coesos e maleáveis.

*   **Localização**: `caio.portfolio.livraria.infrastructure.entity.book.salable.model`
*   **Métodos**:
    *   `void decreaseUnits(int unitsToDecrease)`: Assina o contrato para que a entidade diminua uma quantidade específica de unidades. A implementação deve incluir a lógica para verificar a suficiência das unidades e realizar a subtração.

##### 8.29.1. Implementação: `SalableBook (UnitDecreaser)`

A entidade `SalableBook` implementa a interface `UnitDecreaser` para gerenciar a lógica de diminuição do estoque de livros.

*   **Localização**: `caio.portfolio.livraria.infrastructure.entity.book.salable`
*   **Implementação de `UnitDecreaser`**:
    *   **`decreaseUnits(int unitsToDecrease)`**: Este método verifica se a quantidade a ser diminuída (`unitsToDecrease`) é maior do que as unidades disponíveis (`units`) no estoque. Se a quantidade em estoque for insuficiente, uma `IllegalArgumentException` é lançada. Caso contrário, o número de unidades em estoque é reduzido pelo valor fornecido.

---

### 9. Tratamento de Exceções

A classe `ExceptionHandlerController` gerencia as exceções lançadas pela aplicação, provendo respostas padronizadas e informativas aos clientes da API.

*   **`HandlerMethodValidationException`**:
    *   **Status HTTP**: `400 BAD_REQUEST`
    *   **Mensagem**: "Erro de validação nos parâmetros" (conforme `exceptionHandlerMessageCreator.validationCreateMsg()`)
    *   **Detalhes**: Lista as mensagens de erro dos parâmetros de método que falharam na validação.
*   **`IllegalArgumentException`**:
    *   **Status HTTP**: `400 BAD_REQUEST`
    *   **Mensagem**: Mensagem específica da exceção (`e.getMessage()`).
    *   **Detalhes**: "Argumento inválido fornecido na requisição" (conforme `exceptionHandlerMessageCreator.illegalArgumentCreateMsg()`).
*   **`MethodArgumentNotValidException`**:
    *   **Status HTTP**: `400 BAD_REQUEST`
    *   **Mensagem**: "Erro de validação nos campos da requisição" (conforme `exceptionHandlerMessageCreator.methodArgumentNotValidCreateMsg()`)
    *   **Detalhes**: Lista os campos que falharam na validação e suas respectivas mensagens de erro no formato `campo: mensagem`.
*   **`HttpMessageNotReadableException`**:
    *   **Status HTTP**: `400 BAD_REQUEST`
    *   **Descrição**: Lançada quando o corpo da requisição HTTP não pode ser lido ou é malformado (ex: JSON inválido, tipo de dado incorreto).
    *   **Mensagem**: Mensagem de erro indicando que a mensagem HTTP não pode ser lida (conforme `exceptionHandlerMessageCreator.messageNotReadableCreateMsg()`).
    *   **Detalhes**: Mensagem da exceção original (`e.getMessage()`).
*   **`HttpMessageConversionException`**:
    *   **Status HTTP**: `400 BAD_REQUEST`
    *   **Descrição**: Uma classe base para exceções que ocorrem durante a conversão de mensagens HTTP (ex: tipo de dados não compatível).
    *   **Mensagem**: Mensagem de erro de conversão de mensagem HTTP (conforme `exceptionHandlerMessageCreator.httpMessageConversionCreateMsg()`).
    *   **Detalhes**: Mensagem da exceção original (`e.getMessage()`).
*   **`NoResourceFoundException`**:
    *   **Status HTTP**: `404 NOT_FOUND`
    *   **Descrição**: Lançada quando nenhum recurso (endpoint) é encontrado para a URL da requisição.
    *   **Mensagem**: Mensagem indicando que o recurso não foi encontrado (conforme `exceptionHandlerMessageCreator.noResourceFoundCreateMsg()`).
    *   **Detalhes**: `null`.
*   **`AssertionFailure`**:
    *   **Status HTTP**: `500 INTERNAL_SERVER_ERROR`
    *   **Descrição**: Indica uma falha de asserção interna no sistema, geralmente um erro de programação inesperado que requer atenção.
    *   **Mensagem**: Mensagem de falha de asserção (conforme `exceptionHandlerMessageCreator.assertionFailureCreateMsg()`).
    *   **Detalhes**: `null`.
*   **`MissingServletRequestParameterException`**:
    *   **Status HTTP**: `400 BAD_REQUEST`
    *   **Descrição**: Lançada quando um parâmetro de requisição obrigatório está ausente na URL.
    *   **Mensagem**: Mensagem detalhando o parâmetro ausente (conforme `exceptionHandlerMessageCreator.missingParameterCreateMsg(String parameterName)`).
    *   **Detalhes**: Mensagem da exceção original (`e.getMessage()`).

#### 9.1. Exceções Customizadas

Além das exceções padrão do Spring e Java, a aplicação define exceções customizadas para cenários de negócio específicos, fornecendo maior clareza sobre a causa do erro. O `ExceptionHandlerController` as mapeia para status HTTP apropriados:

*   **`CountryNotFoundException`**
    *   **Status HTTP**: `404 NOT_FOUND`
    *   **Descrição**: Lançada quando uma operação tenta buscar um `Country` que não existe no sistema.
    *   **Mensagem**: "O país solicitado não foi encontrado no sistema" (ou mensagem específica configurada).
*   **`ConcurrentCountryException`**
    *   **Status HTTP**: `409 CONFLICT`
    *   **Descrição**: Lançada quando há uma tentativa de criar um `Country` com um `isoAlpha2Code` que já existe, indicando um conflito de concorrência ou duplicidade de recurso.
*   **`AuthorNotFoundException`**
    *   **Status HTTP**: `404 NOT_FOUND`
    *   **Descrição**: Lançada quando uma operação tenta buscar um `Author` que não existe no sistema.
*   **`AuthorAlreadyExistsException`**
    *   **Status HTTP**: `409 CONFLICT`
    *   **Descrição**: Lançada quando há uma tentativa de criar um `Author` com um `alias` ou `fullName` que já existe, indicando um conflito de concorrência ou duplicidade de recurso.
*   **`ConcurrentAuthorException`**
    *   **Status HTTP**: `409 CONFLICT`
    *   **Descrição**: Lançada quando há uma tentativa de criar ou atualizar um `Author` de forma concorrente, resultando em um conflito de estado.
*   **`PublisherNotFoundException`**
    *   **Status HTTP**: `404 NOT_FOUND`
    *   **Descrição**: Lançada quando uma operação tenta buscar um `Publisher` que não existe no sistema.
*   **`PublisherAlreadyExistsException`**
    *   **Status HTTP**: `409 CONFLICT`
    *   **Descrição**: Lançada quando há uma tentativa de criar um `Publisher` com um `fullAddress` que já existe, indicando um conflito de concorrência ou duplicidade de recurso.
*   **`ConcurrentPublisherException`**
    *   **Status HTTP**: `409 CONFLICT`
    *   **Descrição**: Lançada quando há uma tentativa de criar ou atualizar um `Publisher` de forma concorrente, resultando em um conflito de estado.
*   **`SalableBookNotFoundException`**
    *   **Status HTTP**: `404 NOT_FOUND`
    *   **Descrição**: Lançada quando uma operação tenta buscar um `SalableBook` que não existe no sistema.
*   **`SalableBookAlreadyExistsException`**
    *   **Status HTTP**: `409 CONFLICT`
    *   **Descrição**: Lançada quando há uma tentativa de criar um `SalableBook` com a mesma combinação de `title` e `author` que já existe, indicando um conflito de concorrência ou duplicidade de recurso.
*   **`ConcurrentSalableBookException`**
    *   **Status HTTP**: `409 CONFLICT`
    *   **Descrição**: Lançada quando há uma tentativa de criar ou atualizar um `SalableBook` de forma concorrente, resultando em um conflito de estado.
*   **`InsuficientSalableBookUnitsException`**
    *   **Status HTTP**: `400 BAD_REQUEST`
    *   **Descrição**: Lançada durante uma operação de venda de livros quando a quantidade de unidades solicitadas excede o estoque disponível.

O corpo da resposta de erro segue um padrão JSON contendo: `timestamp`, `status`, `error`, `message`, `path` e `details` (o conteúdo de `details` varia conforme o tipo de exceção).

Exemplo de corpo de erro para `MethodArgumentNotValidException`:
```json
{
    "timestamp": "2025-10-18T21:06:00.903",
    "status": 400,
    "error": "Bad Request",
    "message": "Erro de validação nos campos da requisição",
    "path": "/countries",
    "details": [
        "isoAlpha2Code: O campo 'isoAlpha2Code' não pode estar vazio"
    ]
}
```

---

### 10. Serialização Customizada

Para garantir a consistência e a integridade dos dados durante a comunicação com a API, a aplicação `Livraria` emprega desserializadores customizados. Esses componentes interceptam e processam os dados recebidos via JSON antes que sejam vinculados aos DTOs, garantindo que estejam no formato esperado e normalizados. Essa abordagem aumenta a robustez da aplicação, prevenindo erros de validação e formatando dados de entrada de maneira padronizada.

#### 10.1. `LocalDateDesserializer`

Este desserializador customizado é responsável por converter strings de data no formato "yyyy-MM-dd" em objetos `java.time.LocalDate`. Ele é essencial para processar corretamente datas como a data de nascimento de um autor, garantindo que a API interprete o formato de data de forma consistente e trate erros de parse de maneira controlada.

*   **Localização**: `caio.portfolio.livraria.serialization.LocalDateDesserializer`
*   **Funcionalidade**:
    *   **Entrada**: Recebe uma `String` do corpo da requisição JSON.
    *   **Validação Inicial**: Verifica se a string é `null` ou vazia (após remover espaços em branco). Se for, lança um `InputMismatchException` genérico usando o `SerializationExceptionCreator`.
    *   **Parse**: Tenta converter a string para um `LocalDate` usando o `DateTimeFormatter` predefinido para "yyyy-MM-dd".
    *   **Tratamento de Erro**: Em caso de falha no parse (ex: formato de data inválido, `DateTimeParseException`), captura a exceção e lança um `InputMismatchException` mais específico via `SerializationExceptionCreator`, incluindo o valor da string que causou o erro.
*   **Uso**: Anotado com `@JsonDeserialize(using = LocalDateDesserializer.class)` em campos `LocalDate` dentro dos DTOs, como `CreateAuthorDTO` e `UpdateAuthorDTO`.

#### 10.2. `TrimmedStringDeserializer`

Este desserializador tem como principal responsabilidade a normalização de todas as strings recebidas nas requisições JSON. Ele garante que qualquer espaço em branco inicial ou final (`leading/trailing whitespace`) seja removido automaticamente. Essa funcionalidade é vital para evitar erros de validação causados por entradas como " Livro Teste " em vez de "Livro Teste", além de manter a consistência dos dados armazenados e processados.

*   **Localização**: `caio.portfolio.livraria.serialization.TrimmedStringDeserializer`
*   **Funcionalidade**:
    *   **Entrada**: Recebe uma `String` do corpo da requisição JSON.
    *   **Processamento**: Se a string não for `null`, o método `trim()` é invocado para remover espaços em branco iniciais e finais.
    *   **Saída**: Retorna a string processada ou `null` se a entrada original for `null`.
*   **Uso**: Aplicado via `@JsonDeserialize(using = TrimmedStringDeserializer.class)` em campos `String` de diversos DTOs que se beneficiam dessa normalização, incluindo `CreateCountryDTO`, `CreateAuthorDTO`, `UpdateAuthorDTO`, `CreatePublisherDTO`, `UpdatePublisherDTO`, `CreateBookDTO`, e `UpdateBookDTO`.

---

### 11. Endpoints da API

A API é organizada por recursos, atualmente disponibiliza serviços para gerenciamento de países que posteriormente serão utilizados em campos de entidades.

#### 11.1. Endpoints de Países (`/countries`)

Base: `/countries`

*   `POST /countries`
    *   **Descrição**: Cria um novo país ou retorna um existente se o `isoAlpha2Code` já estiver cadastrado. O nome do país é determinado automaticamente com base no código ISO.
    *   **Método**: `POST`
    *   **Corpo da Requisição**: `CreateCountryDTO` (JSON)
    ```json
    {
        "isoAlpha2Code": "BR"
    }
    ```
    *   **Resposta**:
        *   `201 Created` com o `ResponseCountryDTO` se o país for criado.
        *   `200 OK` com o `ResponseCountryDTO` se o país já existir e for encontrado.

*   `GET /countries`
    *   **Descrição**: Lista todos os países registrados no sistema.
    *   **Método**: `GET`
    *   **Resposta**: `200 OK` com uma lista de `ResponseCountryDTO`.

*   `GET /countries/iso/{isoAlpha2Code}`
    *   **Descrição**: Busca um país específico pelo seu código ISO Alpha-2.
    *   **Método**: `GET`
    *   **Parâmetros de Rota**: 
        *   `isoAlpha2Code`: String - Código ISO Alpha-2 do país (ex: "BR", "US")
    *   **Resposta**: `200 OK` com o `ResponseCountryDTO` do país encontrado.

*   `GET /countries/{id}`
    *   **Descrição**: Busca um país específico pelo seu ID único.
    *   **Método**: `GET`
    *   **Parâmetros de Rota**: 
        *   `id`: Integer - ID único do país (deve ser um número positivo maior que 0)
    *   **Validações**: O ID deve ser um número maior que '0'
    *   **Resposta**: `200 OK` com o `ResponseCountryDTO` do país encontrado.

#### 11.2. Endpoints de Autores (`/authors`)

Base: `/authors`

*   `POST /authors`
    *   **Descrição**: Cria um novo autor.
    *   **Método**: `POST`
    *   **Corpo da Requisição**: `CreateAuthorDTO` (JSON)
    ```json
    {
        "name": "J.K. Rowling",
        "alias": "jkrowling"
    }
    ```
    *   **Resposta**: `201 Created` com o `ResponseAuthorDTO` do autor criado.

*   `GET /authors`
    *   **Descrição**: Lista todos os autores registrados no sistema.
    *   **Método**: `GET`
    *   **Resposta**: `200 OK` com uma lista de `ResponseAuthorDTO`.

*   `GET /authors/{id}`
    *   **Descrição**: Busca um autor específico pelo seu ID único.
    *   **Método**: `GET`
    *   **Parâmetros de Rota**:
        *   `id`: `Long` - ID único do autor (deve ser um número positivo maior que 0)
    *   **Validações**: O ID deve ser um número maior que '0'.
    *   **Resposta**: `200 OK` com o `ResponseAuthorDTO` do autor encontrado.

*   `GET /authors/alias`
    *   **Descrição**: Busca um autor específico pelo seu alias.
    *   **Método**: `GET`
    *   **Parâmetros de Query**:
        *   `alias`: `String` - Alias do autor (ex: "jkrowling")
    *   **Resposta**: `200 OK` com o `ResponseAuthorDTO` do autor encontrado.

*   `PUT /authors/{id}`
    *   **Descrição**: Atualiza um autor existente pelo seu ID.
    *   **Método**: `PUT`
    *   **Parâmetros de Rota**:
        *   `id`: `Long` - ID único do autor a ser atualizado (deve ser um número positivo maior que 0)
    *   **Validações**: O ID deve ser um número maior que '0'.
    *   **Corpo da Requisição**: `UpdateAuthorDTO` (JSON)
    ```json
    {
        "name": "Joanne Kathleen Rowling",
        "alias": "joannekrowling"
    }
    ```
    *   **Resposta**: `200 OK` com o `ResponseAuthorDTO` atualizado.

*   `DELETE /authors/{id}`
    *   **Descrição**: Exclui um autor específico pelo seu ID.
    *   **Método**: `DELETE`
    *   **Parâmetros de Rota**:
        *   `id`: `Long` - ID único do autor a ser excluído (deve ser um número positivo maior que 0)
    *   **Validações**: O ID deve ser um número maior que '0'.
    *   **Resposta**: `200 OK` com `Boolean` indicando o sucesso da operação.

#### 11.3. Endpoints de Editoras (`/publishers`)

Base: `/publishers`

*   `POST /publishers`
    *   **Descrição**: Cria uma nova editora.
    *   **Método**: `POST`
    *   **Corpo da Requisição**: `CreatePublisherDTO` (JSON)
    ```json
    {
        "name": "Editora do Caio",
        "fullAddress": "Rua Exemplo, 123, Cidade, Estado, CEP"
    }
    ```
    *   **Resposta**: `201 Created` com o `ResponsePublisherDTO` da editora criada.

*   `GET /publishers`
    *   **Descrição**: Lista todas as editoras registradas no sistema.
    *   **Método**: `GET`
    *   **Resposta**: `200 OK` com uma lista de `ResponsePublisherDTO`.

*   `GET /publishers/full-address`
    *   **Descrição**: Busca uma editora específica pelo seu endereço completo.
    *   **Método**: `GET`
    *   **Parâmetros de Query**:
        *   `fullAddress`: `String` - Endereço completo da editora (ex: "Rua Exemplo, 123, Cidade, Estado, CEP")
    *   **Resposta**: `200 OK` com o `ResponsePublisherDTO` da editora encontrada.

*   `GET /publishers/{id}`
    *   **Descrição**: Busca uma editora específica pelo seu ID único.
    *   **Método**: `GET`
    *   **Parâmetros de Rota**:
        *   `id`: `Long` - ID único da editora (deve ser um número positivo maior que 0)
    *   **Validações**: O ID deve ser um número maior que '0'.
    *   **Resposta**: `200 OK` com o `ResponsePublisherDTO` da editora encontrada.

*   `PUT /publishers/{id}`
    *   **Descrição**: Atualiza uma editora existente pelo seu ID.
    *   **Método**: `PUT`
    *   **Parâmetros de Rota**:
        *   `id`: `Long` - ID único da editora a ser atualizada (deve ser um número positivo maior que 0)
    *   **Validações**: O ID deve ser um número maior que '0'.
    *   **Corpo da Requisição**: `UpdatePublisherDTO` (JSON)
    ```json
    {
        "name": "Editora do Caio S/A",
        "fullAddress": "Rua Nova, 456, Outra Cidade, Outro Estado, Outro CEP"
    }
    ```
    *   **Resposta**: `200 OK` com o `ResponsePublisherDTO` atualizado.

*   `DELETE /publishers/{id}`
    *   **Descrição**: Exclui uma editora específica pelo seu ID.
    *   **Método**: `DELETE`
    *   **Parâmetros de Rota**:
        *   `id`: `Long` - ID único da editora a ser excluída (deve ser um número positivo maior que 0)
    *   **Validações**: O ID deve ser um número maior que '0'.
    *   **Resposta**: `200 OK` com `Boolean` indicando o sucesso da operação.

#### 11.4. Endpoints de Livros Vendáveis (`/books/salable`)

Base: `/books/salable`

*   `POST /books/salable`
    *   **Descrição**: Cria um novo livro vendável.
    *   **Método**: `POST`
    *   **Corpo da Requisição**: `CreateSalableBookDTO` (JSON)
    ```json
    {
        "title": "O Senhor dos Anéis",
        "isbn": "978-85-333-0227-2",
        "genre": "FANTASY",
        "price": 59.90,
        "amount": 10,
        "authorId": 1,
        "publisherId": 1
    }
    ```
    *   **Resposta**: `201 Created` com o `ResponseSalableBookDTO` do livro criado.

*   `GET /books/salable`
    *   **Descrição**: Lista todos os livros vendáveis registrados no sistema.
    *   **Método**: `GET`
    *   **Resposta**: `200 OK` com uma lista de `ResponseSalableBookDTO`.

*   `GET /books/salable/{id}`
    *   **Descrição**: Busca um livro vendável específico pelo seu ID único.
    *   **Método**: `GET`
    *   **Parâmetros de Rota**:
        *   `id`: `Long` - ID único do livro vendável (deve ser um número positivo maior que 0)
    *   **Validações**: O ID deve ser um número maior que '0'.
    *   **Resposta**: `200 OK` com o `ResponseSalableBookDTO` do livro encontrado.

*   `GET /books/salable/author/{authorId}`
    *   **Descrição**: Busca todos os livros vendáveis de um autor específico pelo ID do autor.
    *   **Método**: `GET`
    *   **Parâmetros de Rota**:
        *   `authorId`: `Long` - ID único do autor (deve ser um número positivo maior que 0)
    *   **Validações**: O ID deve ser um número maior que '0'.
    *   **Resposta**: `200 OK` com uma lista de `ResponseSalableBookDTO`.

*   `GET /books/salable/publisher/{publisherId}`
    *   **Descrição**: Busca todos os livros vendáveis de uma editora específica pelo ID da editora.
    *   **Método**: `GET`
    *   **Parâmetros de Rota**:
        *   `publisherId`: `Long` - ID único da editora (deve ser um número positivo maior que 0)
    *   **Validações**: O ID deve ser um número maior que '0'.
    *   **Resposta**: `200 OK` com uma lista de `ResponseSalableBookDTO`.

*   `GET /books/salable/title`
    *   **Descrição**: Busca livros vendáveis pelo título.
    *   **Método**: `GET`
    *   **Parâmetros de Query**:
        *   `title`: `String` - Título do livro (não pode ser vazio ou em branco)
    *   **Validações**: O título não pode ser vazio ou em branco.
    *   **Resposta**: `200 OK` com uma lista de `ResponseSalableBookDTO`.

*   `GET /books/salable/genre`
    *   **Descrição**: Busca livros vendáveis por gênero.
    *   **Método**: `GET`
    *   **Parâmetros de Query**:
        *   `genre`: `Enum (Genre)` - Gênero do livro (ex: FANTASY, FICTION, SCIENCE_FICTION, etc.)
    *   **Resposta**: `200 OK` com uma lista de `ResponseSalableBookDTO`.

*   `GET /books/salable/isbn`
    *   **Descrição**: Busca livros vendáveis por ISBN.
    *   **Método**: `GET`
    *   **Parâmetros de Query**:
        *   `isbn`: `String` - ISBN do livro (não pode ser vazio ou em branco)
    *   **Validações**: O ISBN não pode ser vazio ou em branco.
    *   **Resposta**: `200 OK` com uma lista de `ResponseSalableBookDTO`.

*   `PUT /books/salable/{id}`
    *   **Descrição**: Atualiza um livro vendável existente pelo seu ID.
    *   **Método**: `PUT`
    *   **Parâmetros de Rota**:
        *   `id`: `Long` - ID único do livro vendável a ser atualizado (deve ser um número positivo maior que 0)
    *   **Validações**: O ID deve ser um número maior que '0'.
    *   **Corpo da Requisição**: `UpdateSalableBookDTO` (JSON)
    ```json
    {
        "title": "O Senhor dos Anéis: A Sociedade do Anel",
        "price": 65.50,
        "amount": 12
    }
    ```
    *   **Resposta**: `200 OK` com o `ResponseSalableBookDTO` atualizado.

*   `PUT /books/salable/sell-books`
    *   **Descrição**: Realiza a venda de um ou mais livros, atualizando o estoque e calculando o valor total da venda.
    *   **Método**: `PUT`
    *   **Corpo da Requisição**: `BookSellListDTO` (JSON)
    ```json
    {
        "bookSales": [
            {
                "id": 1,
                "amount": 2
            },
            {
                "id": 2,
                "amount": 1
            }
        ]
    }
    ```
    *   **Resposta**: `200 OK` com o `BigDecimal` representando o valor total da venda.

*   `DELETE /books/salable/{id}`
    *   **Descrição**: Exclui um livro vendável específico pelo seu ID.
    *   **Método**: `DELETE`
    *   **Parâmetros de Rota**:
        *   `id`: `Long` - ID único do livro vendável a ser excluído (deve ser um número positivo maior que 0)
    *   **Validações**: O ID deve ser um número maior que '0'.
    *   **Resposta**: `200 OK` com `Boolean` indicando o sucesso da operação.

---

### 12. Configurações Essenciais (`application.properties`)

As configurações da aplicação são definidas no arquivo `application.properties`:

*   **Nome da Aplicação**:
    *   `spring.application.name=Livraria`
*   **Console H2 Database**:
    *   `spring.h2.console.enabled=true`
    *   `spring.h2.console.path=/h2-console`
*   **Configurações de Banco de Dados H2**:
    *   `spring.datasource.driver-class-name=org.h2.Driver`
    *   `spring.datasource.url=jdbc:h2:file:./livraria-database;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE` (Banco de dados persistido em arquivo `livraria-database.mv.db` no diretório raiz do projeto)
    *   `spring.datasource.username=caio`
    *   `spring.datasource.password=1234`
*   **JPA (atualização do banco de dados)**:
    *   `spring.jpa.hibernate.ddl-auto=update` (Atualiza o schema do banco de dados automaticamente)
    *   `spring.jpa.show-sql=true` (Exibe as queries SQL no console)
    *   `spring.jpa.properties.hibernate.format_sql=true` (Formata as queries SQL exibidas para melhor legibilidade)

---

### 13. Beans Customizados

Esta seção descreve os beans customizados configurados na aplicação, que estendem ou modificam o comportamento padrão do Spring Framework para atender a requisitos específicos do projeto.

#### 13.1. Configuração de Validação de Países (`CountryValidatorConfig`)

A aplicação define beans customizados para gerenciar a lista de códigos ISO Alpha-2 válidos, que é utilizada pelo `CountryValidator`. Esta configuração é encapsulada na classe `@Configuration` `caio.portfolio.livraria.config.CountryValidatorConfig`.

**Detalhes da Configuração:**

*   **Bean `validIsoCodes()` (Modo Padrão - Produção/Desenvolvimento)**:
    *   Este bean é definido como o padrão para a lista de códigos ISO Alpha-2 válidos.
    *   Utiliza `@ConditionalOnMissingBean` para garantir que ele seja criado apenas se nenhum outro bean `Set<String>` (compatível com a injeção necessária para `CountryValidatorImpl`) já tiver sido definido.
    *   Retorna um `Set<String>` contendo todos os códigos ISO Alpha-2 oficiais reconhecidos globalmente, obtidos através de `Locale.getISOCountries()`. Esta lista abrangente garante que a validação do `isoAlpha2Code` opere com base em padrões internacionais.
    *   É ativo quando nenhum perfil específico é definido ou quando perfis como `dev` ou `prod` estão ativos, e nenhum outro bean `Set<String>` foi explicitamente configurado.

*   **Bean `validIsoCodesTest()` (Modo de Teste - `@Profile("test")`)**:
    *   Este bean é ativado especificamente quando o perfil `test` do Spring Boot está ativo.
    *   Quando o perfil `test` está ativo, este bean **substitui** o bean `validIsoCodes()` padrão (devido à especificidade do perfil), fornecendo uma lista de códigos ISO Alpha-2 válida, porém mais restrita e pré-definida (atualmente: "BR", "IT", "AR", "FR", "US").
    *   Essa configuração é projetada para ambientes de teste, permitindo um controle mais granular e previsível sobre quais códigos de país são considerados válidos, sem carregar o conjunto completo de códigos de localidade, otimizando o desempenho e a previsibilidade dos testes.

---

### 14. Mensagens da Aplicação

Para promover a padronização e facilitar a manutenção, todas as mensagens da aplicação são centralizadas em arquivos `.properties` localizados no diretório `src/main/resources/message`. Essa abordagem garante um *clean code* e uma fácil gestão de textos, facilitando futuras internacionalizações ou alterações de mensagens.

Os arquivos de mensagem atualmente configurados são:

*   `author_messages.properties`
*   `controller_messages.properties`
*   `country_messages.properties`
*   `exception_handler_messages.properties`
*   `publisher_messages.properties`
*   `salable_book_messages.properties`
*   `serialization_messages.properties`

---

### 15. Executando Localmente

A aplicação utiliza Maven para construção e pode ser executada diretamente como uma aplicação Spring Boot.

*   **Pré-requisitos**:
    *   Java 21 instalado
    *   Maven instalado
*   **Passos para Execução**:
    1.  Navegue até a raiz do projeto (onde está o arquivo `pom.xml`).
    2.  Execute a aplicação usando o comando Maven: `mvn clean install` (para construir o projeto e baixar dependências)
    3.  Em seguida, execute: `mvnw spring-boot:run`
    4.  A aplicação estará disponível em `http://localhost:8080` (porta padrão do Spring Boot).
    5.  O console do H2 Database estará acessível em `http://localhost:8080/h2-console`.
        *   **JDBC URL**: `jdbc:h2:file:./livraria-database`
        *   **User Name**: `caio`
        *   **Password**: `1234`

---

### 16. Testes

A aplicação `Livraria` possui uma suíte de testes robusta para garantir a qualidade e o correto funcionamento das funcionalidades implementadas. Utilizamos:

*   **JUnit 5**: Para a estrutura de testes de unidade e integração.
*   **AssertJ**: Para asserções fluidas e legíveis.
*   **Mockito**: Para simulação de dependências em testes de unidade.
*   **Spring Boot Test**: Para facilitar a escrita de testes de integração, carregando partes do contexto Spring.

#### Como Executar os Testes

Para executar todos os testes da aplicação, navegue até a raiz do projeto (onde está o `pom.xml`) e utilize o seguinte comando Maven:

```bash
mvnw test
```