# OrderFlow - Sistema de Gerenciamento de Pedidos

OrderFlow é uma aplicação web desenvolvida com Spring Boot para gerenciar clientes, produtos, pedidos, itens do pedido, pagamentos, entregas e histórico de status.

## Funcionalidades

- Cadastro de clientes.
- Cadastro de produtos.
- Criação de pedidos.
- Inclusão de itens no pedido.
- Registro de pagamentos.
- Registro de entregas.
- Atualização do status do pedido.
- Histórico de movimentações.
- Dashboard com resumo geral.
- Paginação nas listas de clientes, produtos, pedidos, pagamentos, entregas e histórico.

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database para execução local
- PostgreSQL disponível por perfil de configuração
- HTML, CSS e JavaScript
- Docker para deploy

## Como rodar localmente

Abra a pasta que contém o arquivo `pom.xml` e execute:

```powershell
.\mvnw.cmd spring-boot:run
```

Depois acesse no navegador:

```text
http://localhost:8080
```

## Endpoints principais

```http
GET http://localhost:8080/clientes
GET http://localhost:8080/produtos
GET http://localhost:8080/pedidos
GET http://localhost:8080/contem
GET http://localhost:8080/pagamentos
GET http://localhost:8080/entregas
GET http://localhost:8080/historico-status
```

## Banco local

Por padrão, o projeto utiliza H2 em memória para facilitar a execução sem instalação de banco externo.

Console H2:

```text
http://localhost:8080/h2-console
```

Configuração padrão:

```properties
JDBC URL: jdbc:h2:mem:orderflow
User: sa
Password: vazio
```

## Usar PostgreSQL

Crie um banco chamado `OrderFlow` e rode com o perfil PostgreSQL:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
```

As credenciais podem ser ajustadas no arquivo:

```text
src/main/resources/application-postgres.properties
```

## Deploy na web

O projeto contém `Dockerfile`, `render.yaml` e `system.properties`, permitindo publicar a aplicação em serviços compatíveis com Docker.

Para deploy no Render:

1. Envie o projeto para um repositório no GitHub.
2. No Render, crie um novo Web Service.
3. Conecte o repositório do GitHub.
4. Selecione Docker como ambiente de execução.
5. Inicie o deploy.
6. Após finalizar, acesse a URL pública gerada pelo Render.
