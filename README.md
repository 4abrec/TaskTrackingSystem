## API трекинга задач

### Регистрация и авторизация пользователей
#### Регистрация:
Необходимо выполнить POST запрос по адресу localhost:8080/api/auth/signup
Тело запроса: 
{
    "username": "4abrec",
    "email": "4abrec@gmail.com",
    "password": "1234",
    "firstName": "Artyom",
    "lastName": "Cherkasov",
    "roles": ["tester"]
}
В массиве roles необходимо указать роль: manager, developer или tester.
Если не указывать это поле, по умолчанию будет присвоена роль USER, ограничивающая функционал.
