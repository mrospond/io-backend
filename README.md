# io-backend

## Setup

1. W Intellij IDEA utwórz nowy projekt: `File -> New -> Project from Version Control`. 
2. Wybierz lokalizację projektu.
3. Skopiuj URL tego repozytorium i umieść w oknie, które się pojawiło. 
4. `Clone`

Żeby uruchomić aplikację należy przejść do klasy `IoBackendApplication.java` i wywołać metodę main(). Po kilku sekundach aplikacja powinna być dostępna pod adresem `localhost:8080`.

## Baza danych

H2 - baza testowa <br>
PostgreSQL - baza docelowa

Aby uruchomić bazę testową, należy uruchomić aplikację z profilem `dev` (`Konfiguracje -> Edit Configurations.. -> + -> <stwórz konfigurację z tymi samymi parametrami co obecna> -> Active profiles: dev -> OK`). Nic więcej nie trzeba, bo baza H2 jest in-memory. 

Aby uruchomić bazę PostgreSQL, trzeba mieć zainstalowanego Dockera. W konsoli należy wpisać:

`docker run --name io-database -e POSTGRES_PASSWORD=bmeUYnrCjasZp2ej -e POSTGRES_USER=admin -e POSTGRES_DB=io_database -p 5432:5432 -d postgres`

Powyższa komenda uruchomi bazę o nazwie io_database na porcie 5432. 
Połączenie z bazą jest już skonfigurowane, teraz wystarczy już tylko uruchomić aplikację (bez profilu `dev`, inaczej będziemy korzystać z H2).

Bazę danych tworzą 3 tabele: `item_search_history` zawierająca wszystkie wyszukane przedmioty, `users` zawierająca hashe haseł (w BCrypt) i dane użytkowników oraz `user_verification_tokens` zawierająca wszystkie do tej pory wygenerowane tokeny werydikacyjne. <br>
W testowej bazie znajduje się 3 użytkowników: user, user2 i user3. Wszyscy mają to samo hasło: 12345.

## CSRF

Po wysłaniu pierwszego requesta do endpointów wymagających logowania (`/register`, `/login`, `/search/history`, `/search/history/export`) dostaniemy w odpowiedzi status 403:Forbidden wraz z ciasteczkiem `XRSF-TOKEN`. Na etapie integracji z frontendem trzeba będzie napisać logikę (we frontendzie), która pobierze wartość tego ciasteczka i dołączy do każdego kolejnego requesta w sesji <i><b>nagłówek</i></b> o nazwie `X-XSRF-TOKEN` i pobranej wartości. To taka forma zabezpieczenia przed CSRF. 

Podczas lokalnego testowania można jednak wyłączyć tę funkcję za pomocą odkomentowania/zakomentowania odpowiednich linijek:

![image](https://user-images.githubusercontent.com/91494680/212370434-cc0e8141-ed33-431c-99fd-a7df015890a8.png)

Na razie zostawiam wyłączone. 

## Endpointy

- `/register` <br>
  <i>Metoda HTTP: POST <br>
  Login: niewymagany <br></i>
  
  Przykładowe ciało requesta:
  ```json
  {
    "username": "user123",
    "email": "example@example.com",
    "password": "aA123456#",
    "passwordConfirmation": "aA123456#"
  }
  ```

  Służy do rejestracji użytkownika. W odpowiedzi dostaniemy informację o sukcesie lub błąd, jeżeli jeden z następujących warunków nie zostanie spełniony:
  - pola `username`, `password`, `passwordConfirmation` oraz `email` nie są puste
  - pole `username` zawiera od 4 do 20 znaków
  - pole `username` zawiera wyłącznie litery oraz cyfry
  - pole `email` posiada format odpowiedni dla adresów email (user@domain.something)
  - pola `password` oraz `passwordConfirmation` są takie same
  - pola `password` oraz `passwordConfirmation` zawierają co najmniej 8 znaków, przynajmniej jedną dużą literę, jedną małą literę, jedną cyfrę i jeden znak specjalny
  - nazwa użytkownika, którego próbujemy zarejestrować, jeszcze nie istnieje w bazie
  - email użytkownika, którego próbujemy zarejestrować, jeszcze nie istnieje w bazie
  
  Po rejestracji należy aktywować konto w ciągu 24 godzin za pomocą linku przesłanego na adres email. 
  
- `/login` <br>
  <i>Metoda HTTP: POST <br>
  Login: niewymagany <br></i>
  
  Przykładowe ciało requesta: 
  ```json
  {
    "username": "user123",
    "password": "aA123456#"
  }
  ```
  
  Służy do logowania się użytkownika. W odpowiedzi dostaniemy informację o sukcesie wraz z ciasteczkiem sesji `JSESSIONID` lub błąd, jeżeli jeden z następujących warunków nie zostanie spełniony:
  - pola `username` oraz `password` nie są puste
  - pole `username` zawiera wyłącznie litery oraz cyfry
  - pole `username` zawiera od 4 do 20 znaków
  - użytkownik istnieje w bazie
  - hasło i login są prawidłowe
  
- `/search` <br>
  <i>Metoda HTTP: GET <br>
  Login: niewymagany <br></i>

  Służy do wyszukiwania przedmiotów na podstawie wprowadzonego tekstu. W odpowiedzi zwraca listę znalezionych przedmiotów. 
  Przykładowy URL requesta: `localhost:8080/search?query=syrop`
  
  Przykładowa odpowiedź:
  
  ```json
  [
    {
        "name": "Syrop na kaszel",
        "url": "exampleurl.com",
        "price": 15.00,
        "shippingPrice": 2.00,
        "shippingDays": 1,
        "currency": "PLN",
        "shop": "Allegro"
    },
    {
        "name": "Tabletki do ssania",
        "url": "exampleurl.com",
        "price": 10.00,
        "shippingPrice": 1.50,
        "shippingDays": 2,
        "currency": "PLN",
        "shop": "Jakiśsklep"
    }
  ]
  ```
  
- `/search` <br>
  <i>Metoda HTTP: POST <br>
  Login: niewymagany <br></i>
  
  Służy do wyszukiwania przedmiotów na podstawie pliku przekazanego jako wartość nagłówka `file` żądania. W odpowiedzi zwraca listę znalezionych przedmiotów. 

  
- `/search/history` <br>
  <i>Metoda HTTP: GET <br>
  Login: wymagany <br></i>
  
  Służy do pozyskania historii wyszukiwań zalogowanego użytkownika. 
  Przykładowa odpowiedź:
  
  ```json
  [
    {
        "name": "Tabletki do ssania",
        "url": "exampleurl.com",
        "price": 10.00,
        "timestamp": "2023-01-13T16:10:11.332754"
    },
    {
        "name": "Syrop na kaszel",
        "url": "exampleurl.com",
        "price": 15.00,
        "timestamp": "2023-01-13T16:10:11.331443"
    }
  ]
  ```
  
- `/search/history/export` <br>
  <i>Metoda HTTP: GET <br>
  Login: wymagany <br></i>
  
  Służy do eksportu historii wyszukiwań zalogowanego użytkownika do wybranego formatu przekazanego jako zmienna url: `extension`. <br>
  Obsługiwane formaty: csv, pdf
  
  Przykład: `localhost:8080/search/history/export?extension=csv`<br>
  Odpowiedź: plik csv zawierający historię wyszukiwania<br>
  
  Można wykonać requesta w przeglądarce po uprzednim zalogowaniu, wtedy automatycznie pobierze się plik.
  
- `/h2` <br>
  <i>Login: niewymagany do aplikacji, wymagany do bazy danych <br>
  Login do bazy: sa <br>
  Hasło: brak <br></i>
  
  Konsola wbudowanej bazy danych H2 dostępna z poziomu przeglądarki. Do celów testowych
