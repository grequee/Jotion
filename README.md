# Jotion

API RESTful para app de Notas (tipo o Notion) usando o Framework Java Spring Boot para o trabalho de Linguagens de Programação - FURG

---

## Tecnologias utilizadas

- **Java 21** + **Spring Boot 4.0**
- **Spring Security** + **JWT** (autenticação)
- **Spring Data JPA** + **SQLite** (banco de dados local)
- **WebSocket** (edição em tempo real)
- **Lombok**

---

## Como executar o projeto

### Pré-requisitos

- Java 21 instalado
- Maven (ou use o `mvnw` incluso no projeto)

### Backend (API)

```bash
# Na pasta /Jotion
./mvnw spring-boot:run
```

> No Windows, use `mvnw.cmd spring-boot:run`

A API ficará disponível em: **`http://localhost:8080`**

O banco de dados SQLite (`Jotion.db`) é criado automaticamente na raiz do projeto.


## Autenticação

A API usa **JWT**. Endpoints de notas exigem o token no header:

```
Authorization: Bearer <access_token>
```

O `access_token` é obtido no endpoint `/usuarios/login`.

---

## Endpoints da API

### Usuários — `/usuarios`

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/usuarios` | Lista todos os usuários |
| `GET` | `/usuarios/{id}` | Busca usuário por ID |
| `GET` | `/usuarios/me` | Retorna o usuário logado (via token) |
| `POST` | `/usuarios` |  Cadastra novo usuário |
| `PUT` | `/usuarios/{id}` |  Atualiza dados de um usuário |
| `DELETE` | `/usuarios/{id}` |  Remove um usuário |
| `POST` | `/usuarios/login` |  Realiza login e retorna tokens |
| `POST` | `/usuarios/refresh` |  Renova o access token via refresh token |
| `POST` | `/usuarios/logout` |  Realiza logout (invalida refresh token) |

#### `POST /usuarios` — Cadastrar usuário
```json
{
  "nome": "joao",
  "senha": "123456"
}
```

#### `POST /usuarios/login` — Login
```json
{
  "nome": "joao",
  "senha": "123456"
}
```
**Resposta:**
```json
{
  "accessToken": "eyJ...",
  "refreshToken": "eyJ..."
}
```

#### `POST /usuarios/refresh` — Renovar token
```
Body (texto puro): eyJ... (o refreshToken)
```

---

### Notas — `/notas`

> Todos os endpoints de notas exigem autenticação via JWT.

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/notas` | Lista todas as notas do usuário logado |
| `GET` | `/notas/{id}` | Busca uma nota específica por ID |
| `POST` | `/notas` | Cria uma nova nota |
| `PUT` | `/notas/{id}` | Atualiza título e/ou conteúdo de uma nota |
| `DELETE` | `/notas/{id}` | Remove uma nota |
| `POST` | `/notas/{id}/colaboradores` | Adiciona um colaborador à nota |
| `DELETE` | `/notas/{id}/colaboradores/{nomeColaborador}` | Remove um colaborador da nota |

#### `POST /notas` e `PUT /notas/{id}` — Criar/Atualizar nota
```json
{
  "titulo": "Minha nota",
  "conteudo": "Conteúdo da nota..."
}
```

**Resposta (NotaDTO):**
```json
{
  "id": 1,
  "titulo": "Minha nota",
  "conteudo": "Conteúdo da nota...",
  "nomeUsuario": "joao",
  "colaboradores": ["maria"],
  "dataCriacao": "2025-06-25T14:00:00",
  "dataAtualizacao": "2025-06-25T14:05:00"
}
```

#### `POST /notas/{id}/colaboradores` — Adicionar colaborador
```json
{
  "nomeColaborador": "maria"
}
```
