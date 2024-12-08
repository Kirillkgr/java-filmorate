TABLE films {
    id int [primary key] // Идентификатор фильма
    name varchar(255) [not null] // Название фильма
    description varchar(200) [not null] // Описание фильма
    release_date date [not null] // Дата выхода фильма
    duration interval [not null] // Продолжительность фильма
    rating_id int [ref: > ratings.id] // Рейтинг фильма
}

TABLE film_genres {
    id int [primary key] // Уникальный идентификатор записи
    film_id int [ref: > films.id] // Ссылка на фильм
    genre_id int [ref: > genres.id] // Ссылка на жанр
}

TABLE genres {
    id int [primary key] // Идентификатор жанра
    name varchar(100) [not null] // Название жанра
}

TABLE ratings {
    id int [primary key] // Идентификатор рейтинга
    name varchar(50) [not null] // Название рейтинга (G, PG и т.д.)
    description text // Описание рейтинга
}

TABLE film_likes {
    id int [primary key] // Уникальный идентификатор записи
    film_id int [ref: > films.id] // Ссылка на фильм
    user_id int [ref: > users.id] // Ссылка на пользователя
}


TABLE users {
    id int [primary key] // Идентификатор пользователя
    name varchar(255) [not null] // Имя пользователя
    email varchar(255) [not null, unique] // Электронная почта
    login varchar(50) [not null, unique] // Логин
    birthday date [not null] // Дата рождения
}

TABLE friendship {
    id int [primary key] // Уникальный идентификатор записи
    user_id int [ref: > users.id] // Первый пользователь
    friend_id int [ref: > users.id] // Второй пользователь
    status varchar(50) [not null] // Статус дружбы (confirmed/unconfirmed)
}

