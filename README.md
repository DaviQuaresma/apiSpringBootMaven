# Movies API REST

Uma API RESTful completa para gerenciamento de filmes com sistema de autenticação JWT, integração com API externa (OMDB) e arquitetura Spring Boot bem estruturada.

## 📋 Índice

- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Configuração do Ambiente](#-configuração-do-ambiente)
- [Docker e Banco de Dados](#-docker-e-banco-de-dados)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Autenticação](#-autenticação)
- [Endpoints da API](#-endpoints-da-api)
- [Models/Entidades](#-modelsentidades)
- [DTOs e Mappers](#-dtos-e-mappers)
- [Services](#-services)
- [Repositories](#-repositories)
- [Responses](#-responses)
- [Configurações](#-configurações)
- [Tratamento de Exceções](#-tratamento-de-exceções)
- [Como Executar](#-como-executar)
- [Exemplos de Uso](#-exemplos-de-uso)

## 🚀 Tecnologias Utilizadas

### Framework e Dependências Principais
- **Spring Boot 3.5.5** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Spring WebFlux** - Cliente reativo para chamadas HTTP
- **MySQL** - Banco de dados principal
- **Docker Compose** - Containerização do banco

### Bibliotecas de Apoio
- **JWT (Json Web Tokens)** - `io.jsonwebtoken:jjwt-*:0.11.5`
  - Geração e validação de tokens de autenticação
- **Lombok** - Redução de boilerplate code
- **Jackson** - Serialização/deserialização JSON

## 🏗 Arquitetura

A aplicação segue o padrão **MVC (Model-View-Controller)** com camadas bem definidas:

```
├── Controllers    # Endpoints REST
├── Services       # Lógica de negócio
├── Repositories   # Acesso a dados
├── Models         # Entidades JPA
├── DTOs           # Data Transfer Objects
├── Responses      # Objetos de resposta
├── Configs        # Configurações de segurança e beans
└── Exceptions     # Tratamento global de exceções
```

## ⚙️ Configuração do Ambiente

### Propriedades da Aplicação (`application.properties`)

```properties
# Configuração do servidor
spring.application.name=moviesApiRest
server.port=8005

# Configuração do banco MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase?autoReconnect=true
spring.datasource.username=myuser
spring.datasource.password=secret

# Configuração JWT
security.jwt.secret-key=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
security.jwt.expiration-time=3600000  # 1 hora em milissegundos

# Configuração JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# API Externa OMDB
api.key=e9b2a010
api.endpoint=https://www.omdbapi.com/

# Logs
logging.level.root=DEBUG
logging.file.name=application.log
```

## 🐳 Docker e Banco de Dados

O projeto utiliza **Docker Compose** para configurar o banco MySQL:

```yaml
# compose.yaml
services:
  mysql:
    image: 'mysql:latest'
    environment:
      - 'MYSQL_DATABASE=mydatabase'
      - 'MYSQL_PASSWORD=secret'
      - 'MYSQL_ROOT_PASSWORD=verysecret'
      - 'MYSQL_USER=myuser'
    ports:
      - '3306'
```

### Executando o banco:
```bash
docker-compose up -d
```

## 📁 Estrutura do Projeto

### 📊 Models/Entidades

#### `User.java`
Entidade para gerenciamento de usuários com implementação do Spring Security:

```java
@Entity
@Table(name = "users")
public class User implements UserDetails {
    private Integer id;
    private String email;      // Login único
    private String password;   // Criptografado com BCrypt
    private String fullname;
    private Date createdAt;
    private Date updatedAt;
}
```

**Características:**
- Implementa `UserDetails` para integração com Spring Security
- Login via email
- Senhas criptografadas automaticamente
- Timestamps automáticos de criação/atualização

#### `Movie.java`
Entidade principal para armazenamento de filmes:

```java
@Entity
@Table(name = "movies")
public class Movie {
    private Long id;
    private String title;      // Único
    private String year;
    private String runtime;    // Ex: "162 min"
    private String genre;
    private String director;
    private String actors;
    private String plot;       // Até 2000 caracteres
    private String language;
    private String country;
    private String awards;
    private String poster;     // URL da imagem
    private String imdbRating;
    private String type;       // movie, series
    private String boxOffice;
    private Date createdAt;
    private Date updatedAt;
}
```

#### `PaginationRequest.java`
Model para padronizar requisições de paginação:

```java
public class PaginationRequest {
    private Integer page = 1;           // Página atual
    private Integer size = 10;          // Itens por página
    private String sortField = "id";    // Campo de ordenação
    private Sort.Direction direction = Sort.Direction.DESC;
}
```

## 🔄 DTOs e Mappers

### DTOs (Data Transfer Objects)

#### `LoginUserDto.java` / `RegisterUserDto.java`
```java
public class LoginUserDto {
    private String email;
    private String password;
}

public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;
}
```

#### `MovieDto.java`
DTO para receber dados da API externa OMDB com mapeamento JSON:

```java
public class MovieDto {
    @JsonProperty("Title")
    private String title;
    
    @JsonProperty("Year")
    private String year;
    
    // ... outros campos mapeados
}
```

### `MovieMapper.java`
Responsável pela conversão entre DTOs, Models e Responses:

```java
@Component
public class MovieMapper {
    public Movie toEntity(MovieDto dto);        // DTO -> Model
    public MovieResponse toResponse(Movie movie); // Model -> Response
}
```

## 🔧 Services

### `AuthenticationService.java`
Gerencia autenticação e cadastro de usuários:

```java
@Service
public class AuthenticationService {
    // Cadastro de novos usuários
    public User signup(RegisterUserDto input);
    
    // Autenticação de usuários existentes
    public User authenticate(LoginUserDto input);
}
```

### `JwtService.java`
Gerencia tokens JWT:

```java
@Service
public class JwtService {
    public String generateToken(UserDetails userDetails);
    public String extractUsername(String token);
    public boolean isTokenValid(String token, UserDetails userDetails);
    public long getExpirationTime();
}
```

### `MovieService.java`
Lógica de negócio para filmes:

```java
@Service
public class MovieService {
    // CRUD básico
    public Movie save(Movie movie);
    public List<Movie> findAll();
    public Movie findById(Long id);
    public void delete(Long id);
    
    // Funcionalidades avançadas
    public Mono<Movie> saveReactive(Movie movie);  // Salvar reativo
    public PagingResult<MovieResponse> findAllPaginated(PaginationRequest request);
}
```

### `MovieApiExternal.java`
Integração com API externa OMDB usando WebClient:

```java
@Service
public class MovieApiExternal {
    public Mono<MovieResponse> getDataUnique(String movieName);
}
```

### `UserService.java`
Operações relacionadas a usuários:

```java
@Service
public class UserService {
    public List<User> allUsers();
}
```

## 🗄️ Repositories

### `MovieRepository.java`
```java
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findById(Long id);
    List<Movie> findAll();
}
```

### `UserRepository.java`
```java
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
```

## 📤 Responses

### `LoginResponse.java`
```java
public class LoginResponse {
    private String token;
    private Long expiresIn;
    private String email;
}
```

### `MovieResponse.java`
Espelha o `MovieDto` para padronizar respostas da API externa.

### `PagingResult<T>.java`
Wrapper genérico para respostas paginadas:

```java
public class PagingResult<T> {
    private Collection<T> content;
    private Integer totalPages;
    private long totalElements;
    private Integer size;
    private Integer page;
    private boolean empty;
    
    // Classe utilitária interna
    public static class PaginationUtils {
        public static Pageable getPageable(PaginationRequest request);
    }
}
```

## ⚙️ Configurações

### `SecurityConfiguration.java`
Configuração principal de segurança:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        // Configurações de CORS, CSRF, JWT, etc.
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Permite requisições do frontend (localhost:4200)
    }
}
```

**Endpoints públicos:**
- `/auth/**` - Autenticação
- `/movie/search` - Busca de filmes na API externa

### `JwtAuthenticationFilter.java`
Filtro para interceptar e validar tokens JWT:

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain);
}
```

### `ApplicationConfiguration.java`
Beans de configuração da aplicação:

```java
@Configuration
public class ApplicationConfiguration {
    @Bean UserDetailsService userDetailsService();
    @Bean BCryptPasswordEncoder passwordEncoder();
    @Bean AuthenticationManager authenticationManager();
    @Bean AuthenticationProvider authenticationProvider();
}
```

### `WebClientConfig.java`
Configuração do WebClient para chamadas HTTP reativas:

```java
@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("https://www.omdbapi.com/").build();
    }
}
```

## 🚨 Tratamento de Exceções

### `GlobalExceptionHandler.java`
Manipulador global de exceções usando `@RestControllerAdvice`:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception);
}
```

**Exceções tratadas:**
- `BadCredentialsException` - Credenciais incorretas (401)
- `AccountStatusException` - Conta bloqueada (403)
- `AccessDeniedException` - Acesso negado (403)
- `SignatureException` - JWT inválido (403)
- `ExpiredJwtException` - JWT expirado (403)
- `Exception` - Erro interno genérico (500)

## 🔗 Endpoints da API

### 🔐 Autenticação (`/auth`)

#### POST `/auth/signup`
Cadastra um novo usuário.

**Request Body:**
```json
{
    "email": "user@example.com",
    "password": "senha123",
    "fullName": "Nome Completo"
}
```

**Response:**
```json
{
    "id": 1,
    "email": "user@example.com",
    "fullname": "Nome Completo",
    "createdAt": "2025-09-02T10:00:00.000+00:00",
    "updatedAt": "2025-09-02T10:00:00.000+00:00"
}
```

#### POST `/auth/login`
Autentica um usuário existente.

**Request Body:**
```json
{
    "email": "user@example.com",
    "password": "senha123"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "expiresIn": 3600000,
    "email": "user@example.com"
}
```

### 👤 Usuários (`/users`)
> **Requer autenticação JWT**

#### GET `/users/me`
Retorna dados do usuário autenticado.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
{
    "id": 1,
    "email": "user@example.com",
    "fullname": "Nome Completo",
    "createdAt": "2025-09-02T10:00:00.000+00:00",
    "updatedAt": "2025-09-02T10:00:00.000+00:00"
}
```

#### GET `/users`
Lista todos os usuários.

### 🎬 Filmes (`/movie`)

#### GET `/movie/search` (público)
Busca filme na API externa OMDB e salva no banco.

**Query Parameters:**
- `title` (string, obrigatório) - Nome do filme

**Exemplo:**
```
GET /movie/search?title=Inception
```

**Response:**
```json
{
    "id": 1,
    "title": "Inception",
    "year": "2010",
    "runtime": "148 min",
    "genre": "Action, Sci-Fi, Thriller",
    "director": "Christopher Nolan",
    "actors": "Leonardo DiCaprio, Marion Cotillard, Ellen Page",
    "plot": "A thief who steals corporate secrets...",
    "language": "English",
    "country": "USA",
    "awards": "Won 4 Oscars",
    "poster": "https://...",
    "imdbRating": "8.8",
    "type": "movie",
    "boxOffice": "$292,576,195",
    "createdAt": "2025-09-02T10:00:00.000+00:00",
    "updatedAt": "2025-09-02T10:00:00.000+00:00"
}
```

#### GET `/movie` (autenticado)
Lista filmes com paginação.

**Query Parameters:**
- `page` (int, opcional, padrão: 1) - Número da página
- `size` (int, opcional, padrão: 10) - Itens por página
- `sortField` (string, opcional, padrão: "id") - Campo de ordenação
- `direction` (enum, opcional, padrão: DESC) - Direção da ordenação

**Exemplo:**
```
GET /movie?page=1&size=5&sortField=title&direction=ASC
```

**Response:**
```json
{
    "content": [
        {
            "title": "Avatar",
            "year": "2009",
            // ... outros campos
        }
    ],
    "totalPages": 10,
    "totalElements": 50,
    "size": 5,
    "page": 0,
    "empty": false
}
```

#### GET `/movie/allMovies` (autenticado)
Lista todos os filmes sem paginação.

#### GET `/movie/{id}` (autenticado)
Busca filme por ID.

**Path Parameters:**
- `id` (long) - ID do filme

#### DELETE `/movie/{id}` (autenticado)
Remove filme por ID.

**Response:** 
- `204 No Content` - Sucesso
- `404 Not Found` - Filme não encontrado

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+
- Docker e Docker Compose

### Passos

1. **Clone o repositório:**
```bash
git clone <repository-url>
cd moviesApiRest
```

2. **Configure o banco de dados:**
```bash
docker-compose up -d
```

3. **Execute a aplicação:**
```bash
./mvnw spring-boot:run
```

4. **Acesse a API:**
```
http://localhost:8005
```

### Variáveis de Ambiente Opcionais

```bash
# JWT
export JWT_SECRET=seu_jwt_secret
export JWT_EXPIRATION=3600000

# OMDB API
export OMDB_API_KEY=sua_api_key

# Banco de Dados
export DB_URL=jdbc:mysql://localhost:3306/mydatabase
export DB_USERNAME=myuser
export DB_PASSWORD=secret
```

## 📚 Exemplos de Uso

### 1. Cadastro e Login
```bash
# Cadastrar usuário
curl -X POST http://localhost:8005/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"123456","fullName":"Test User"}'

# Fazer login
curl -X POST http://localhost:8005/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"123456"}'
```

### 2. Buscar e Salvar Filme
```bash
# Buscar filme da API externa (público)
curl "http://localhost:8005/movie/search?title=Matrix"
```

### 3. Listar Filmes Paginados
```bash
# Listar filmes (requer token)
curl -X GET "http://localhost:8005/movie?page=1&size=5" \
  -H "Authorization: Bearer SEU_JWT_TOKEN"
```

### 4. Verificar Usuário Autenticado
```bash
curl -X GET http://localhost:8005/users/me \
  -H "Authorization: Bearer SEU_JWT_TOKEN"
```

## 🔍 Funcionalidades Principais

### ✅ Implementadas
- ✅ **Sistema de autenticação JWT completo**
- ✅ **CRUD de filmes com paginação**
- ✅ **Integração com API externa (OMDB)**
- ✅ **Programação reativa com WebFlux**
- ✅ **Tratamento global de exceções**
- ✅ **Configuração CORS para frontend**
- ✅ **Validação e segurança robusta**
- ✅ **Logs estruturados**
- ✅ **Docker para banco de dados**

### 🚀 Melhorias Futuras
- [ ] Testes unitários e de integração
- [ ] Cache com Redis
- [ ] Relacionamento User-Movie (favoritos)
- [ ] Upload de imagens
- [ ] Rate limiting
- [ ] Documentação Swagger/OpenAPI
- [ ] Health checks
- [ ] Métricas com Actuator

---

**Desenvolvido com** ❤️ **usando Spring Boot**

*Para dúvidas ou sugestões, abra uma issue no repositório.*
