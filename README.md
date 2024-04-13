# GUFPB

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

This project is an API built using **Java, Java Spring, thymeleaf for Templates ,H2 as the database, and Spring Security and JWT for authentication control.**

## Usage

1. Start the application with Maven
2. The API will be accessible at http://localhost:8081

3. Para acessar as configuraçoes do banco de dados a segurança do Spring deve ser desativada em SecurityConfigurations.java
4. Inicie a aplicação e acesse http://localhost:8081/h2-console/
5. SELECT * FROM CURSO;

6. O WebScrapping e feito pelo Script python localizado em resourses, ele salva no arquivo cursos_info.csv deve rodar uma vez ao dia para manter a tabela atualizada.