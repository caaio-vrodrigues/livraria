# Livraria - Backend API

Esta documentação detalha a API backend `Livraria`, um serviço completo para gerenciamento de uma livraria, focando em seus recursos, endpoints e configurações.

## Sumário
1. [Visão Geral](#1-visão-geral)
2. [Tecnologias Principais](#2-tecnologias-principais)
3. [Entidades Principais](#3-entidades-principais)
    - [3.1. `Country`](#31-country)
4. [DTOs (Data Transfer Objects)](#4-dtos-data-transfer-objects)
    - [4.1. `CreateCountryDTO`](#41-createcountrydto)
    - [4.2. `ResponseCountryDTO`](#42-responsecountrydto)
    - [4.3. `TrimmedStringDeserializer`](#43-trimmedmedstringdeserializer)
    - [4.4. `ResponseCountryDTOCreator`](#44-responsecountrydtocreator)
    - [4.5. `CountryResult`](#45-countryresult)
    - [4.6. `CountryResultImplDTO`](#46-countryresultimpldto)
5. [Serviços (Lógica de Negócio)](#5-serviços-lógica-de-negócio)
    - [5.1. `CountryService`](#51-countryservice)
    - [5.2. `CountryValidator`](#52-countryvalidator)
6. [Tratamento de Exceções](#6-tratamento-de-exceções)
7. [Endpoints da API](#7-endpoints-da-api)
    - [7.1. Endpoints de Países (`/countries`)](#71-endpoints-de-países-countries)
8. [Configurações Essenciais (`application.properties`)](#8-configurações-essenciais-applicationproperties)
9. [Executando Localmente](#9-executando-localmente)
10. [Testes](#10-testes)

---

### 1. Visão Geral

A aplicação `Livraria` é um serviço backend ainda em desenvolvimento utilizando Spring Boot. Atualmente, ela gerencia informações de países, permitindo o cadastro e consulta de países utilizando códigos ISO Alpha-2. O objetivo é fornecer uma base para um sistema de livraria robusto.

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

As principais entidades que compõem o sistema até o momento são:

#### 3.1. `Country`

Representa um país, armazenando seu código ISO Alpha-2 e nome completo.

*   **id**: `Integer` (Gerado automaticamente, chave primária)
*   **isoAlpha2Code**: `String` (Código ISO Alpha-2 do país, 2 caracteres, obrigatório, único)
*   **name**: `String` (Nome completo do país, obrigatório, único)

#### 3.2. `CountryRepository`

Interface de repositório Spring Data JPA responsável pelo acesso e manipulação de dados da entidade `Country` no banco de dados.

*   Estende `JpaRepository<Country, Integer>`, provendo operações CRUD básicas para a entidade `Country`, utilizando `Integer` como tipo da chave primária.
*   Define um método de consulta customizado:
    *   `Optional<Country> findByIsoAlpha2Code(String isoAlpha2Code)`: Permite buscar um país pelo seu código ISO Alpha-2, retornando um `Optional` para indicar a possível ausência do país.

---

### 4. DTOs (Data Transfer Objects)

#### 4.1. `CreateCountryDTO`

Utilizado para a criação de um novo `Country`.

*   **isoAlpha2Code**: `String` (Código ISO Alpha-2 do país.
    *   **Validações**: `NotBlank` (não pode ser vazio, nulo ou conter apenas espaços em branco, com a mensagem de erro padrão "O campo 'isoAlpha2Code' não pode estar vazio"), `Size(min=2, max=2)` para garantir que tenha exatamente 2 caracteres.
    *   **Desserialização**: Possui `TrimmedStringDeserializer` aplicado via `@JsonDeserialize` para garantir que espaços em branco sejam removidos antes da validação.

#### 4.2. `ResponseCountryDTO`

Utilizado para retornar informações de um `Country` nas respostas da API.

*   **id**: `Integer`
    *   **Validações**: `@NotNull` (não pode ser nulo, com a mensagem de erro "O campo 'id' não pode estar nulo").
*   **isoAlpha2Code**: `String`
    *   **Validações**: `@NotEmpty` (não pode estar vazio, com a mensagem de erro "O campo 'isoAlpha2Code' não pode estar vazio"), `@Size(min=2, max=2)` (deve ter exatamente 2 caracteres).
*   **name**: `String`
    *   **Validações**: `@NotEmpty` (não pode estar vazio, com a mensagem de erro "O campo 'name' não pode estar vazio").

#### 4.3. `TrimmedStringDeserializer`

Este custom `JsonDeserializer` da Jackson é utilizado para remover automaticamente espaços em branco (leading/trailing) de campos `String` durante a desserialização de JSON. Ele garante que valores de string recebidos em DTOs sejam tratados antes de validações ou persistência, prevenindo inconsistências.

*   **Implementação**: Estende `JsonDeserializer<String>`, sobrescrevendo o método `deserialize` para aplicar a operação `trim()`.
*   **Funcionalidade**: Quando um campo `String` é desserializado, este componente garante que seu valor final não contenha espaços em branco nas extremidades. Se o valor for `null`, ele é retornado como `null`.
*   **Uso**: É aplicado a campos de DTOs usando a anotação `@JsonDeserialize(using = TrimmedStringDeserializer.class)`, como visto no campo `isoAlpha2Code` do `CreateCountryDTO`.

#### 4.4. `ResponseCountryDTOCreator`

Interface e implementação responsáveis por mapear a entidade `Country` para seu respectivo `ResponseCountryDTO`. Isso centraliza a lógica de conversão, mantendo a responsabilidade de construção de DTOs separada das entidades e serviços, promovendo um design mais limpo e testável.

*   **Interface**: `ResponseCountryDTOCreator`
*   **Implementação**: `ResponseCountryDTOCreatorImpl` (anotado com `@Service`)
*   **Método Principal**:
    *   `toResponseCountryDTO(Country country)`: Recebe uma entidade `Country` e retorna uma instância de `ResponseCountryDTO` preenchida com os dados do país. Utiliza o padrão `builder` do `ResponseCountryDTO` para uma construção clara e imutável.

#### 4.5. `CountryResult`

Interface que define o contrato para objetos que encapsulam o resultado de uma operação de criação ou busca de país, indicando o status da operação.

*   **`wasCreated()`**: Retorna `true` se o país foi criado como parte da operação, `false` caso contrário.
*   **`wasFound()`**: Retorna `true` se o país já existia e foi encontrado como parte da operação, `false` caso contrário.

#### 4.6. `CountryResultImplDTO`

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

---

### 5. Serviços (Lógica de Negócio)

#### 5.1. `CountryService`

Responsável pela orquestração das operações de negócio relacionadas a `Country`.

*   **`createOrFindCountry(CreateCountryDTO createCountryDTO)`**:
    *   Valida e normaliza o `isoAlpha2Code` fornecido.
    *   Verifica se o país já existe no banco de dados.
    *   Se existir, retorna o país encontrado com um indicador de que não foi criado.
    *   Se não existir, resolve o nome do país com base no código ISO, cria e persiste a nova entidade `Country`.
    *   Retorna o país criado com um indicador de que foi criado.
*   **`getAllCountries()`**:
    *   Retorna uma lista de todos os `Country`s registrados, mapeados para `ResponseCountryDTO`.
*   **`getCountryByIsoAlpha2Code(String isoAlpha2Code)`**:
    *   Busca e retorna um país específico pelo seu código ISO Alpha-2, mapeado para `ResponseCountryDTO`.
*   **`getCountryById(Integer id)`**:
    *   Busca e retorna um país específico pelo seu ID único, mapeado para `ResponseCountryDTO`.

#### 5.2. `CountryValidator`

Componente de serviço responsável pela validação de códigos ISO Alpha-2 e resolução de nomes de países. A interface `CountryValidator` é uma interface composta que estende `IsoAlpha2CodeValidator` e `CountryNameValidator`, agrupando suas responsabilidades.

##### 5.2.1. Interface `IsoAlpha2CodeValidator`

Define o contrato para a validação e normalização de códigos ISO Alpha-2.

*   **`processIsoAlpha2Code(String isoAlpha2Code)`**:
    *   Recebe um código ISO Alpha-2 e realiza as seguintes validações e normalizações:
        *   Verifica se o código é `null` ou composto apenas por espaços em branco, lançando `IllegalArgumentException` com a mensagem "O campo 'isoAlpha2Code' não pode estar vazio" se for o caso.
        *   Normaliza o código: remove espaços em branco (`trim()`) e converte para letras maiúsculas (`toUpperCase()`).
        *   Verifica se o código normalizado está presente no `Set` de `validIsoCodes` (cuja configuração é detalhada na seção 8.1). Se não estiver, lança `IllegalArgumentException` com a mensagem "O código '`[isoAlpha2Code]`' não corresponde a um código válido.".
    *   Retorna o `isoAlpha2Code` validado e normalizado.

##### 5.2.2. Interface `CountryNameValidator`

Define os contratos para a resolução de nomes de países a partir de seus códigos ISO Alpha-2.

*   **`resolveNameByIsoAlpha2Code(String isoAlpha2Code)`**:
    *   Primeiro, chama `processIsoAlpha2Code` (definido em `IsoAlpha2CodeValidator`) para validar e normalizar o `isoAlpha2Code` fornecido.
    *   Em seguida, resolve o nome completo do país a partir do código ISO Alpha-2 validado, utilizando a funcionalidade `Locale.getDisplayName(Locale.ENGLISH)`. Isso significa que o nome do país será retornado em inglês.

*   **`getNameByValidatedAndNormalizedIsoAlpha2Code(String validIsoAlpha2Code)`**:
    *   Método interno que, a partir de um `isoAlpha2Code` já validado e normalizado, constrói um `Locale` e retorna o nome do país em inglês (`Locale.ENGLISH`).

##### 5.2.3. Implementação: `CountryValidatorImpl`

A classe `CountryValidatorImpl` implementa a interface composta `CountryValidator`, consolidando as funcionalidades de validação de códigos ISO Alpha-2 e resolução de nomes de países.
Internamente, utiliza um `Set<String>` (`validIsoCodes`) injetado via Spring, que contém os códigos ISO Alpha-2 considerados válidos pela aplicação (cuja configuração é detalhada na seção 8.1).

*   **`processIsoAlpha2Code(String isoAlpha2Code)`**: Implementa a lógica detalhada na interface `IsoAlpha2CodeValidator`.
*   **`resolveNameByIsoAlpha2Code(String isoAlpha2Code)`**: Implementa a lógica detalhada na interface `CountryNameValidator`.
*   **`getNameByValidatedAndNormalizedIsoAlpha2Code(String validIsoAlpha2Code)`**: Implementa a lógica detalhada na interface `CountryNameValidator`.

---

### 6. Tratamento de Exceções

A classe `ExceptionHandlerController` gerencia as exceções lançadas pela aplicação, provendo respostas padronizadas e informativas aos clientes da API.

*   **`HandlerMethodValidationException`**:
    *   **Status HTTP**: `400 BAD_REQUEST`
    *   **Mensagem**: "Erro de validação nos parâmetros"
    *   **Detalhes**: Lista as mensagens de erro dos parâmetros de método que falharam na validação.
*   **`CountryNotFoundException`**:
    *   **Status HTTP**: `404 NOT_FOUND`
    *   **Mensagem**: Mensagem específica da exceção.
    *   **Detalhes**: "O país solicitado não foi encontrado no sistema".
*   **`IllegalArgumentException`**:
    *   **Status HTTP**: `400 BAD_REQUEST`
    *   **Mensagem**: Mensagem específica da exceção (`e.getMessage()`).
    *   **Detalhes**: "Argumento inválido fornecido na requisição".
*   **`MethodArgumentNotValidException`**:
    *   **Status HTTP**: `400 BAD_REQUEST`
    *   **Mensagem**: "Erro de validação nos campos da requisição"
    *   **Detalhes**: Lista os campos que falharam na validação e suas respectivas mensagens de erro no formato `campo: mensagem`.

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

### 7. Endpoints da API

A API é organizada por recursos, atualmente disponibiliza serviços para gerenciamento de países que posteriormente serão utilizados em campos de entidades.

#### 7.1. Endpoints de Países (`/countries`)

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

---

### 8. Configurações Essenciais (`application.properties`)

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

#### 8.1. Configuração de Validação de Países (`CountryValidatorConfig`)

A aplicação utiliza uma configuração dedicada para gerenciar a lista de códigos ISO Alpha-2 válidos, que é utilizada pelo `CountryValidator`.

*   **Modo Padrão (Produção/Desenvolvimento)**:
    *   Por padrão, um `Set<String>` contendo todos os códigos ISO Alpha-2 oficiais reconhecidos globalmente (`Locale.getISOCountries()`) é disponibilizado. Esta lista abrangente garante que a validação do `isoAlpha2Code` opere com base em padrões internacionais.
    *   Esta configuração é ativa quando nenhum perfil específico é definido ou quando perfis como `dev` ou `prod` estão ativos, e nenhum outro bean `Set<String>` foi explicitamente configurado.

*   **Modo de Teste (`@Profile("test")`)**:
    *   Quando o perfil `test` do Spring Boot está ativo, uma lista de códigos ISO Alpha-2 válida, porém mais restrita e pré-definida, é utilizada (atualmente: "BR", "IT", "AR", "FR", "US").
    *   Essa configuração é projetada para ambientes de teste, permitindo um controle mais granular e previsível sobre quais códigos de país são considerados válidos, sem carregar o conjunto completo de códigos de localidade.

---

### 9. Executando Localmente

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

### 10. Testes

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