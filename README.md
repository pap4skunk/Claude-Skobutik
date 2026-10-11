# 👟 Skobutikken

En webshop til sko bygget med **Spring Boot 4.1**, Thymeleaf, Spring Data JPA og en H2-database i hukommelsen.

## Kør projektet

Kræver Java 21 eller nyere. Maven skal ikke installeres, fordi projektet har Maven-wrapperen med.

```bash
# Windows
mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

Åbn derefter <http://localhost:8080>.

| Side | URL |
|---|---|
| Butik | http://localhost:8080 |
| Kurv | http://localhost:8080/kurv |
| Admin | http://localhost:8080/admin |
| JSON-API | http://localhost:8080/api/sko |
| H2-konsol | http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:skobutik`, bruger `sa`) |

Kør testene med `mvnw.cmd test`.

## Funktioner

- **Butik**: produktoversigt med søgning og filtrering på kategori (Herre, Dame, Børn, Sport)
- **Produktside**: vælg størrelse. Udsolgte størrelser er streget over.
- **Indkøbskurv**: gemmes i brugerens session og tjekker lagerbeholdningen
- **Kassen**: validering af navn, email og adresse. Ordren oprettes, og lageret trækkes ned i én transaktion.
- **Admin**: opret, rediger og slet sko, styr lager pr. størrelse og skift ordrestatus. Annullerer man en ordre, kommer varerne tilbage på lager.
- **REST API**: `GET /api/sko?kategori=SPORT&soeg=nord` og `GET /api/sko/{id}`

Databasen fyldes med 9 eksempelsko ved opstart (`DataLoader`). Da det er en database i hukommelsen, nulstilles alt ved genstart.

## Struktur

```
src/main/java/dk/skobutik/
├── model/          Sko, SkoVariant (størrelse + lager), Ordre, OrdreLinje, Kategori, OrdreStatus
├── repository/     Spring Data JPA repositories
├── service/        SkoService, KurvService, OrdreService, Kurv (session-scoped)
├── controller/     ButikController, KurvController, AdminController, SkoApiController
└── config/         DataLoader med eksempeldata
src/main/resources/
├── templates/      Thymeleaf-sider (+ admin/ og fragments/)
└── static/css/     style.css
```

## Videre idéer

- Skift til MySQL/PostgreSQL ved at ændre `spring.datasource.*` og tilføje driveren i `pom.xml`
- Beskyt `/admin` med Spring Security
- Upload af rigtige produktbilleder
