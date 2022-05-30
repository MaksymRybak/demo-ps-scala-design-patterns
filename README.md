To run the postgres image:
> docker run --rm --name pg-docker -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 -v ${PWD}/docker/volumes/postgres:/var/lib/postgresql/data postgres

To create a new User, you may send a POST request to `http://localhost:8080/user with below request JSON (Content-type: application/json).
```json
{
  "id": "507f7f05-65d8-4f72-8e0a-2b4958cbba0a",
  "name": "Mario",
  "email": "mario.rossi@gmail.com",
  "password": "12345",
  "address": {
    "addressLine": "via Roma, 1",
    "location": {
      "pin": "12321",
      "city": "Rome",
      "country": "Italy"
    }
  },
  "role": "Speaker"
}
```