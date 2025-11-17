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
5. [Serviços (Lógica de Negócio)](#5-serviços-lógica-de-negócio)
    - [5.1. `CountryService`](#51-countryservice)
    - [5.2. `CountryValidator`](#52-countryvalidator)
6. [Tratamento de Exceções](#6-tratamento-de-exceções)
7. [Endpoints da API](#7-endpoints-da-api)
    - [7.1. Endpoints de Países (`/countries`)](#71-endpoints-de-países-countries)
8. [Configurações Essenciais (`application.properties`)](#8-configurações-essenciais-applicationproperties)
9. [Executando Localmente](#9-executando-localmente)

---

### 1. Visão Geral

A aplicação `Livraria` é um serviço backend ainda em desenvolvimento utilizando Spring Boot. Atualmente, ela gerencia informações de países, permitindo o cadastro e consulta de países utilizando códigos ISO Alpha-2. O objetivo é fornecer uma base para um sistema de livraria robusto.

### 2. Tecnologias Principais

*   **Framework**: Spring Boot 3.x (versão `3.5.7`)
*   **Linguagem**: Java 21
*   **Persistência**: Spring Data JPA
*   **Banco de Dados**: H2 Database (persistido em arquivo)
*   **Auxiliares**: Lombok (para reduzir código boilerplate)
*   **Validação**: Spring Boot Starter Validation
*   **Build Tool**: Maven

### 3. Entidades Principais

As principais entidades que compõem o sistema até o momento são:

#### 3.1. `Country`

Representa um país, armazenando seu código ISO Alpha-2 e nome completo.

*   **id**: `Integer` (Gerado automaticamente, chave primária)
*   **isoAlpha2Code**: `String` (Código ISO Alpha-2 do país, 2 caracteres, obrigatório, único)
*   **name**: `String` (Nome completo do país, obrigatório, único)

### 4. DTOs (Data Transfer Objects)

#### 4.1. `CreateCountryDTO`

Utilizado para a criação de um novo `Country`.

*   **isoAlpha2Code**: `String` (Código ISO Alpha-2 do país. `NotBlank`, tamanho 2, e tem o trim aplicado durante a desserialização via `TrimmedStringDeserializer`).

#### 4.2. `ResponseCountryDTO`

Utilizado para retornar informações de um `Country` nas respostas da API.

*   **id**: `Integer` (`NotNull`)
*   **isoAlpha2Code**: `String` (`NotEmpty`, tamanho 2)
*   **name**: `String` (`NotEmpty`)

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

#### 5.2. `CountryValidator`

Componente de serviço que lida com a validação e resolução de nomes de países.

*   **`processIsoAlpha2Code(String isoAlpha2Code)`**: Valida se o `isoAlpha2Code` é não nulo/vazio, o normaliza (trim, uppercase) e verifica se é um código ISO 2-letras válido. Lança `IllegalArgumentException` se inválido.
*   **`resolveNameByIsoAlpha2Code(String isoAlpha2Code)`**: Resolve o nome completo do país a partir de um `isoAlpha2Code` (internamente usa `Locale.getISOCountries()` para validação e `Locale` para o nome).

### 6. Tratamento de Exceções

A classe `ExceptionHandlerController` gerencia as exceções lançadas pela aplicação, provendo respostas padronizadas e informativas aos clientes da API.

*   **`IllegalArgumentException`**:
    *   **Status HTTP**: `400 BAD_REQUEST`
    *   **Mensagem**: "Argumento inválido fornecido na requisição" e a mensagem específica da exceção.
*   **`MethodArgumentNotValidException`**:
    *   **Status HTTP**: `400 BAD_REQUEST`
    *   **Mensagem**: "Erro de validação nos campos da requisição"
    *   **Detalhes**: Lista os campos que falharam na validação e suas respectivas mensagens de erro.

O corpo da resposta de erro segue um padrão JSON contendo: `timestamp`, `status`, `error`, `message`, `path` e `details` (para erros de validação).

Exemplo de corpo de erro:
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

### 9. Executando Localmente

A aplicação utiliza Maven para construção e pode ser executada diretamente como uma aplicação Spring Boot.

*   **Pré-requisitos**:
    *   Java 21 instalado
    *   Maven instalado
*   **Passos para Execução**:
    1.  Navegue até a raiz do projeto (onde está o arquivo `pom.xml`).
    2.  Execute a aplicação usando o comando Maven: `mvn clean install` (para construir o projeto e baixar dependências)
    3.  Em seguida, execute: `mvn spring-boot:run`
    4.  A aplicação estará disponível em `http://localhost:8080` (porta padrão do Spring Boot).
    5.  O console do H2 Database estará acessível em `http://localhost:8080/h2-console`.
        *   **JDBC URL**: `jdbc:h2:file:./livraria-database`
        *   **User Name**: `caio`
        *   **Password**: `1234`