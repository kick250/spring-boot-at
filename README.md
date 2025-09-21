# Projeto Spring Boot – Trabalho da Faculdade

Este repositório contém um projeto desenvolvido como **trabalho da faculdade**, utilizando o framework **Spring Boot** com **Maven**.

## Requisitos
- **Java 21** ou superior

## Configuração
Antes de executar o projeto, é necessário configurar o arquivo de propriedades:

1. Localize o arquivo de exemplo:
   ```
   src/main/resources/application.example.yml
   ```
2. Faça uma cópia dele com o nome:
   ```
   src/main/resources/application.yml
   ```
3. Ajuste as configurações conforme necessário.

## Execução
Após configurar o arquivo **application.yml**, execute o projeto com o Maven.

### Via Maven
No terminal, dentro da pasta do projeto, rode:
```bash
./mvn spring-boot:run
```

Isso iniciará a aplicação localmente, normalmente em:
```
http://localhost:8080
```
