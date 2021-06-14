## API трекинга задач

### Регистрация и авторизация пользователей
#### Регистрация:
Необходимо выполнить POST запрос по адресу localhost:8080/api/auth/signup <br/> 
Тело запроса: <br/>
```javascript
{
    "username": "4abrec",
    "email": "4abrec@gmail.com", 
    "password": "1234","firstName": 
    "Artyom","lastName": 
    "Cherkasov","roles": ["tester"]
}
```
В массиве roles необходимо указать роль: manager, developer или tester.<br/> 
Если не указывать это поле, по умолчанию будет присвоена роль USER, ограничивающая функционал.<br/> 
#### Авторизация:
Необходимо выполнить POST запрос по адресу localhost:8080/api/auth/login <br/> 
Тело запроса: <br/> 
```javascript
{
    "username": "4abrec",
    "password": "1234","firstName": 

}
```
В случае успешной авторизации будет возвращен объект, содержащий информацию об авторизованном пользователе <br/> 
и JWT токен.
