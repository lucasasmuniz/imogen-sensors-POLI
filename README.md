# API de Monitoramento em Tempo Real do Rio Una - Projeto Imogen

![Java](https://img.shields.io/badge/Java-21-blue.svg?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen.svg?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg?style=for-the-badge&logo=docker)
![Status do Projeto](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow.svg?style=for-the-badge)

## Descrição do Projeto

Esse projeto é uma API construida usando Java, Java Spring e PostgreSQL como banco de dados.

Esta API foi desenvolvida como uma solução tecnológica em parceria com a [**Imogen Tecnologia**](https://www.linkedin.com/company/imogen-health/), uma startup de Recife especializada em visão computacional e IoT. O projeto visa atacar um problema crítico e recorrente na cidade de **Palmares**, na Mata Sul de Pernambuco: as enchentes do **Rio Una**.

A falta de informações precisas e em tempo real sobre o nível do rio agrava um cenário já delicado para centenas de famílias. Esta API surge como o coração de uma plataforma centralizada, fornecendo **dados confiáveis e em tempo real** para a Defesa Civil e a população, possibilitando ações preventivas mais rápidas e eficazes. 

## Tecnologias Utilizadas

| Ferramenta | Versão/Tipo | Propósito |
| --- | --- | --- |
| **Java** | `21` | Linguagem principal do backend |
| **Spring Boot** | `3.5.0` | Framework para construção da API RESTful |
| **Spring Data JPA** | `3.5.0` | Camada de persistência de dados |
| **Spring Web** | `3.5.0` | Módulo para criar aplicações web e APIs |
| **Spring Validation** | `3.5.0` | Validação de dados de entrada (DTOs) |
| **Spring DevTools** | `3.5.0` | Ferramentas para agilizar o desenvolvimento |
| **PostgreSQL** | `15` | Banco de dados relacional principal |
| **H2 Database**| - | Banco de dados em memória para o ambiente de teste |
| **Docker** | `20.10+` | Containerização da aplicação e do banco |
| **Maven**| - | Gerenciador de dependências e build |
| **Lombok**| `1.18.x` | Redução de código boilerplate (getters, setters, etc.) |

## Pré-requisitos

Para executar este projeto localmente, você precisará ter instalado:

* **JDK 21** ou uma versão superior (Ou Docker).
* **Docker** e **Docker Compose**.
* Um cliente de API como **Postman** ou **Insomnia** para realizar os testes.

## Como Rodar a Aplicação

O projeto é totalmente containerizado, para facilitar a sua execução. Siga os passos abaixo:

Você pode rodar a aplicação de duas maneias, basta copiar o docker-compose e trocar a linha:
```yml
services:
  app-service:
    build: . # Constrói a imagem localmente
    # ... resto das configs
```
Para:
```yml
services:
  app-service:
    image: lucasmuniz/imogen-app:2.0 # Puxa a imagem pronta do Docker Hub
    # ... resto das configs
```

E depois adicionar um `.env`(pode copiar o `.env.example` que deixei) e definir as seguintes variáveis:
```properties
# Configurações do Banco de Dados PostgreSQL
POSTGRES_DB=nome_do_banco_aqui
POSTGRES_USER=usuario_aqui
POSTGRES_PASSWORD=sua_senha_aqui

# URL JDBC da aplicação Spring (com o nome do container do PostgreSQL)
SPRING_DATASOURCE_URL=jdbc:postgresql://pg-docker:5432/nome_do_postgres_db_aqui
SPRING_DATASOURCE_USERNAME=nome_do_postgres_user_aqui
SPRING_DATASOURCE_PASSWORD=senha_do_postgres_aqui

#DDL_AUTO é usado para definir o comportamento do Hibernate em relação ao esquema do banco de dados.
#No exemplo, 'update' significa que o Hibernate vai tentar atualizar o esquema do banco de dados sem apagar os dados existentes.
SPRING_JPA_HIBERNATE_DDL_AUTO=update
PGADMIN_PORT=5050
```
Ou seguir esses passos:

1.  **Clone o repositório:**
    ```bash
    git clone git@github.com:lucasasmuniz/imogen-sensors-POLI.git
    cd imogen-sensors-POLI
    ```

2.  **Configure as variáveis de ambiente:**
    Crie um arquivo chamado `.env` na raiz do projeto (você pode copiar o `.env.example` que deixei) e defina as seguintes variáveis:
    ```properties
    # Configurações do Banco de Dados PostgreSQL
    POSTGRES_DB=nome_do_banco_aqui
    POSTGRES_USER=usuario_aqui
    POSTGRES_PASSWORD=sua_senha_aqui
    
    # URL JDBC da aplicação Spring (com o nome do container do PostgreSQL)
    SPRING_DATASOURCE_URL=jdbc:postgresql://pg-docker:5432/nome_do_postgres_db_aqui
    SPRING_DATASOURCE_USERNAME=nome_do_postgres_user_aqui
    SPRING_DATASOURCE_PASSWORD=senha_do_postgres_aqui
    
    #DDL_AUTO é usado para definir o comportamento do Hibernate em relação ao esquema do banco de dados.
    #No exemplo, 'update' significa que o Hibernate vai tentar atualizar o esquema do banco de dados sem apagar os dados existentes.
    SPRING_JPA_HIBERNATE_DDL_AUTO=update
    PGADMIN_PORT=5050
    ```

3.  **Suba os containers com Docker Compose:**
    Este comando vai criar a imagem da aplicação e iniciar os containers do backend e do banco de dados.
    ```bash
    docker-compose up -d --build
    ```

4.  **Pronto!**
    A API já está em execução, você pode usar o **Postman** ou **Insomnia** para acessar os [endpoints](#endpoints) mais fácilmente.

## 

## Endpoints
Aqui estão listados os endpoints que você pode acessar na API.
​
| Endpoint               | Descrição                                          
|----------------------|-----------------------------------------------------
| <kbd>GET /stations</kbd>     | Retorna uma lista de todas as estações cadastradas e suas informações geográficas. [Mais detalhes](#get-all-stations)
| <kbd>GET /stations/{id}</kbd>     | Busca uma estação pelo seu ID. [Mais detalhes](#get-stations-by-id)
| <kbd>POST /stations</kbd>     | Cria uma nova estação. [Mais detalhes](#post-stations)
| <kbd>GET /lots</kbd>     | Retorna uma busca paginada pelos lotes já cadastrados. [Mais detalhes](#get-all-lots)
| <kbd>GET /lots/{id}</kbd>     | Busca um lote pelo seu ID, trazendo mais informações de estações e sensores cadastrados. [Mais detalhes](#get_lots_by_id)
| <kbd>POST /lots</kbd>     | Cria um novo lote de medição de sensores. [Mais detalhes](#)


<h3 id="get-all-stations">GET /stations</h3>

**Resposta**
```json
[
    {
        "id": "6d891f09-015f-4cd8-9647-ac288acc2ebe",
        "name": "STN001",
        "latitude": -9.1355,
        "longitude": -20.0234,
        "elevationM": 2.40,
        "sensors": []
    },
    {
        "id": "875984eb-509d-4081-b5d0-5bbfe8a526a8",
        "name": "STN0015",
        "latitude": -3.1355,
        "longitude": -60.0234,
        "elevationM": 18.40,
        "sensors": []
    }
]
```
<h3 id="get-stations-by-id">GET /stations/{id}</h3>

**Resposta**
```json
{
    "id": "6d891f09-015f-4cd8-9647-ac288acc2ebe",
    "name": "STN001",
    "latitude": -9.1355,
    "longitude": -20.0234,
    "elevationM": 2.40,
    "sensors": []
}
```

<h3 id="post-stations">POST /stations</h3>

**Requisição**
```json
{
    "name": "STM001",
    "latitude": -3.1355,
    "longitude": -60.0234,
    "elevationM": 18.4
}
```

**Resposta**
```json
{
    "id": "fa7136e2-1408-44c0-9440-66565a0b7ba1",
    "name": "STM001",
    "latitude": -3.1355,
    "longitude": -60.0234,
    "elevationM": 18.4,
    "sensors": []
}
```

<h3 id="get-all-lots">GET /lots?page=0&size=12&startDate=2023-03-23&endDate=2025-04-22&sort=timestamp,desc</h3>

OBS: 
1. **startDate** e **endDate** são filtros para procura de lotes, não são necessários.
2. **page** é o número da página que você estará olhando.
3. **size** é a quantidade de lotes por páginas
4. **sort** é a forma que está sendo ordenado os lotes, da forma que está acima, eles estão sendo ordenados do mais recendo para o mais antigo.

**Resposta**
```json
{
    "content": [
        {
            "id": "2bc6461d-d29b-4e4f-aa47-9b38021b44ae",
            "timestamp": "2024-05-23T14:30:00Z",
            "stations": []
        },
        {
            "id": "72f693e0-2bf0-412e-a250-08bd5513c225",
            "timestamp": "2023-05-23T14:30:00Z",
            "stations": []
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 12,
        "sort": {
            "sorted": true,
            "empty": false,
            "unsorted": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 2,
    "size": 12,
    "number": 0,
    "sort": {
        "sorted": true,
        "empty": false,
        "unsorted": false
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
}
```

<h3 id="get_lots_by_id">GET /lots/{id}</h3>

**Resposta**
```json
{
    "id": "2bc6461d-d29b-4e4f-aa47-9b38021b44ae",
    "timestamp": "2024-05-23T14:30:00Z",
    "stations": [
        {
            "id": "875984eb-509d-4081-b5d0-5bbfe8a526a8",
            "name": "STN0015",
            "latitude": -3.1355,
            "longitude": -60.0234,
            "elevationM": 18.40,
            "sensors": [
                {
                    "id": "e46a5036-d15c-47bb-9132-f0fa2a99d4ee",
                    "type": "gps_longitude",
                    "unit": "°",
                    "value": -60.02
                },
                {
                    "id": "0cb874b2-4016-491c-a8ff-b370c07ba0a8",
                    "type": "temperature_air",
                    "unit": "C",
                    "value": 29.10
                },
                {
                    "id": "beec1c5d-c23a-42ba-9ebf-1d9acf06cee1",
                    "type": "wind_speed",
                    "unit": "m/s",
                    "value": 3.20
                },
                {
                    "id": "d0353894-4f3b-44bc-a619-af0bfac2ceeb",
                    "type": "wind_direction",
                    "unit": "deg",
                    "value": 225.00
                },
                {
                    "id": "045cd285-9fca-4d2c-b2bb-9127155fd0cf",
                    "type": "wind_speed",
                    "unit": "m/s",
                    "value": 3.20
                },
                {
                    "id": "a2eb3313-1965-43d9-8834-59f37ebda9dc",
                    "type": "river_level",
                    "unit": "m",
                    "value": 3.42
                },
                {
                    "id": "e4097361-c486-4a87-9753-225e10b1370d",
                    "type": "soil_moisture",
                    "unit": "%",
                    "value": 63.00
                }
            ]
        }
    ]
}

```
<h3 id="post-lots">POST /lots</h3>

**Requisição**
```json
{
    "timestamp": "2025-06-02T14:30:00Z",
    "stations": [
        {
            "name": "STN001",
            "latitude": -9.1355,
            "longitude": -20.0234,
            "elevationM": 2.4,
            "sensors": [                
                {
                    "type": "pressure",
                    "unit": "hPa",
                    "value": 1013.2
                }
                ]
        },
        {
            "name": "STN0015",
            "latitude": -3.1355,
            "longitude": -60.0234,
            "elevationM": 18.4,
            "sensors": [
                {
                    "type": "river_level",
                    "unit": "m",
                    "value": 3.42
                },
                {
                    "type": "rainfall",
                    "unit": "mm",
                    "value": 2.8
                }
            ]
        }
    ]
}
```

**Resposta**
```json
{
    "id": "3067c356-f92a-4631-a2d2-d025fa108308",
    "timestamp": "2025-06-02T14:30:00Z",
    "stations": [
        {
            "id": "875984eb-509d-4081-b5d0-5bbfe8a526a8",
            "name": "STN0015",
            "latitude": -3.1355,
            "longitude": -60.0234,
            "elevationM": 18.40,
            "sensors": [
                {
                    "id": "e1f3f4ca-e388-4cec-967d-8dbe9195f49c",
                    "type": "river_level",
                    "unit": "m",
                    "value": 3.42
                },
                {
                    "id": "0acc36a7-8676-420e-8aa3-63353b0eb31a",
                    "type": "rainfall",
                    "unit": "mm",
                    "value": 2.8
                }
            ]
        },
        {
            "id": "6d891f09-015f-4cd8-9647-ac288acc2ebe",
            "name": "STN001",
            "latitude": -9.1355,
            "longitude": -20.0234,
            "elevationM": 2.40,
            "sensors": [
                {
                    "id": "451e6b6d-0c73-4a32-b6e0-cfe75d50eecd",
                    "type": "pressure",
                    "unit": "hPa",
                    "value": 1013.2
                }
            ]
        }
    ]
}
```