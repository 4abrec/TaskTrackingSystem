## API трекинга задач
### Тестирование API
Необходимо перейти по ссылке http://localhost:8080/swagger-ui.html#/ <br/> 
#### Формат Authorization токена: 
Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0YWJyZWMiLCJpYXQiOjE2MjM3NjE2NjksImV4cCI6MTYyMzg0ODA2OX0.hRO-l-WGKyqbESiFW5sH5GAoq3Jh6ClRCW2FB4BE9YIMAJQlEK8gIpYCilao3fzSV6EW_Yxr9dgnrDnQYwmV3Q
### Регистрация и авторизация пользователей
#### Регистрация:
Необходимо выполнить POST запрос по адресу localhost:8080/api/auth/signup <br/> 
Тело запроса: <br/>
```javascript
{
    "username": "4abrec",
    "email": "4abrec@gmail.com", 
    "password": "1234",
    "firstName": "Artyom",
    "lastName": "Cherkasov",
    "roles": ["manager"]
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
Далее нужно выполнить POST запрос с использование полученного после авторизации JWT токена по адресу localhost:8080/api/feature/add <br/> 
Тело запроса:<br/> 
```javascript
{
    "title": "testFeature",
    "description": "test",
    "users": ["4abrec", "5abrec", "6abrec"]
}
```
Массив users соджержит в себе имена пользователей, участвующих в разработке данной feature.
#### Добавление task
Также необходимо авторизоваться пользователем с ролью MANAGER.<br/>
Далее нужно выполнить POST запрос с использование полученного после авторизации JWT токена по адресу localhost:8080/api/task/add <br/> 
Тело запроса:<br/> 
```javascript
{
    "title": "testFeature",
    "description": "test",
    "username": "5abrec",
    "featureId": 1
}
```
Username - developer, на которого будет перекинута task. Если этот пользователь не является developer, будет сгенерировано исключение.
### Выполнение task
Сначала необходимо авторизоваться пользователем с ролью DEVELOPER.<br/> 
Далее нужно выполнить GET запрос по адресу localhost:8080/api/task/resolve?id=1&&username=6abrec ,<br/>
где id - id task, которая выполнена, а username - имя тестера, на которого переназначается task. В случае если пользователь <br/>
оказывается не тестером, будет сгенерированно исключение.
### Возврат task на доработку
Сначала необходимо авторизоваться пользователем с ролью TESTER.<br/> 
Далее нужно выполнить POST запрос по адресу localhost:8080/api/task/return <br/> 
Тело запроса:<br/> 
```javascript
{
    "title": "testBug",
    "description": "test",
    "taskId": 1
}
```
Тело запроса содержит описание bug и id задачи, которая будет автоматически переназначена на предыдущего developer со сменой статуса.
### Закрытие task
Сначала необходимо авторизоваться пользователем с ролью TESTER.<br/>
Далее нужно выполнить GET запрос по адресу localhost:8080/api/task/close/1 ,<br/>
где 1 - id задачи. Bug будет переведен в состояние null, а статус в COMPLETED.
### Закрытие feature
Сначала необходимо авторизоваться пользователем с ролью MANAGER.<br/>
Далее нужно выполнить GET запрос по адресу localhost:8080/api/feature/close/1 ,<br/>
где 1 - id feature. Будет проверено, выполнены ли все задачи, и если да - статус feature будет переведен в COMPLETED.
