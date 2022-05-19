# Know me App REST API

#### Authentification
| Method   | URL                        | Description               |
| -------- | -------------------------- | ------------------------- |
| `GET`    | `/api/auth/signin`         | Sign in user.             |
| `GET`    | `/api/auth/signup`         | Sign up user.             |

#### User
| Method    | URL                           | Description                                   |
| --------- | ----------------------------- | --------------------------------------------- |
| `GET`     | `/api/v1/users`               | Retrieve all users.                           |
| `GET`     | `/api/v1/users/{id}`          | Retrieve user with id.                        |
| `PUT`     | `/api/v1/users/{id}`          | Update user with id.                          |
| `GET`     | `/api/v1/users/{id}/decks`    | Retrieve all decks from user with id.         |
| `PUT`     | `/api/v1/users/{id}/decks`    | Add new deck with secretId from user with id. |
| `DELETE`  | `/api/v1/users/{id}/decks`    | Delete deck with secretId from user with id.  |

#### Decks
| Method   | URL                        | Description                   |
| -------- | -------------------------  | ----------------------------- |
| `GET`    | `/api/v1/decks/{id}`       | Retrieve deck with id.        |
| `GET`    | `/api/v1/decks/{secretId}` | Retrieve deck with secretId.  |
| `POST`   | `/api/v1/decks`            | Add new deck.                 |
| `PUT`    | `/api/v1/decks/{id}`       | Update deck with id.          |
| `DELETE` | `/api/v1/decks/{id}`       | Delete deck with id.          |

#### Questions
| Method   | URL                      | Description                |
| -------- | ------------------------ | -------------------------- |
| `GET`    | `/api/v1/questions`      | Retrieve all questions.    |
| `GET`    | `/api/v1/questions/{id}` | Retrieve question with id. |
| `POST`   | `/api/v1/questions`      | Create a new post.         |
| `PUT`    | `/api/v1/questions/{id}` | Update question with id.   |
| `DELETE` | `/api/v1/questions/{id}` | Delete question with id.   |
