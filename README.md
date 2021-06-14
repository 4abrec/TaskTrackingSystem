## API трекинга задач

### Регистрация и авторизация пользователей
#### Регистрация:
Необходимо выполнить POST запрос по адресу localhost:8080/api/auth/signup <br/> 
Тело запроса: <br/> 
{<br/> 
    *"username": "4abrec",<br/> 
    "email": "4abrec@gmail.com",<br/> 
    "password": "1234",<br/> 
    "firstName": "Artyom",<br/> 
    "lastName": "Cherkasov",<br/> 
    "roles": ["tester"]<br/> 
}<br/> 
В массиве roles необходимо указать роль: manager, developer или tester.<br/> 
Если не указывать это поле, по умолчанию будет присвоена роль USER, ограничивающая функционал.<br/> 
