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
    "password": "1234"
}
```
В случае успешной авторизации будет возвращен объект, содержащий информацию об авторизованном пользователе и JWT токен.
### Добавление task и feature
#### Добавление feature 
Сначала необходимо авторизоваться пользователем с ролью MANAGER.<br/> 
Далее нужно выполнить POST запрос с с использование полученного после авторизации JWT токена по адресу localhost:8080/api/task/add <br/> 
Тело запроса:<br/> 
```javascript
{
    "title": "testFeature",
    "description": "test",
    "users": ["4abrec", "5abrec", "6abrec"]
}
Массив users соджержит в себе имена пользователей, участвующих в разработке данной feature.
