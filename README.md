# Testes unitários com JUnit 5, utilizando Mockito e TDD.

## Ferramentas e tecnologias utilizadas
* **IDE de desenvolvimento**: Eclipse.
* **Arquitetura do projeto**: Maven.
* **Linguagem de programação**: Java 11.
* **Framework's de teste**: JUnit 5.9.1 e Mockito 4.6.1.

## Extras
* **Implementado o paralelismo no pom.xml na agilizar a execução da suíte de testes**.

* **Necessário ter o maven configurado no PATH do S.O para execução dos testes via linha de comando**. <p>

* Vídeo tutorial sobre como configurar o Maven: https://www.youtube.com/watch?v=-ucX5w8Zm8s <p>

## Instruções de execução

### Pelo Eclipse IDE
* Clonar o projeto. 
* Descompactar o Eclipse e executá-lo.    
* No menu File >> Import>> Maven >> Existing Maven Projects, localizar o caminho em que o projeto foi clonado.
* Com o projeto devidamente importado no Eclipse, localizar e expandir o source folder:  src/test/java <p>
* Executar a suíte de testes com JUnit: SuiteTest.Java.

### Por linha de comando
* Abrir um terminal e realizar o clone do projeto.
* Entrar na pasta do projeto.
* Digitar: <p>
`mvn test` <p>

* **A primeira execução é um pouco lenta, durante e após a segunda execução é que da para perceber o paralelismo funcionando**.



## Autor
**[Thiago Nogueira dos Santos](https://www.linkedin.com/in/thinogueiras/)**. <br />
**Quality Assurance Analyst**. <br />