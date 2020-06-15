                                         ██████╗ ███╗   ██╗██╗██████╗ ██████╗ ███████╗
                                        ██╔════╝ ████╗  ██║██║██╔══██╗██╔══██╗██╔════╝
                                        ██║  ███╗██╔██╗ ██║██║██████╔╝██████╔╝███████╗
                                        ██║   ██║██║╚██╗██║██║██╔══██╗██╔═══╝ ╚════██║
                                        ╚██████╔╝██║ ╚████║██║██║  ██║██║     ███████║
                                         ╚═════╝ ╚═╝  ╚═══╝╚═╝╚═╝  ╚═╝╚═╝     ╚══════╝
                                                  
                                  __                             __          __      __        __                  
                ____  ____  _____/ /_____ _________  _________ _/ /     ____/ /___ _/ /_____ _/ /_  ____ _________ 
               / __ \/ __ \/ ___/ __/ __ `/ ___/ _ \/ ___/ __ `/ /_____/ __  / __ `/ __/ __ `/ __ \/ __ `/ ___/ _ \
              / /_/ / /_/ (__  ) /_/ /_/ / /  /  __(__  ) /_/ / /_____/ /_/ / /_/ / /_/ /_/ / /_/ / /_/ (__  )  __/
             / .___/\____/____/\__/\__, /_/   \___/____/\__, /_/      \__,_/\__,_/\__/\__,_/_.___/\__,_/____/\___/ 
            /_/                   /____/                  /_/                                                      
                                                                                                  
## Summary

This simple module offers an out-of-the-box secure connection to a PostgreSQL database, relying on configuration passed 
through the `application.yml` (or by default `postgresql-application.yml`) file. It also defines a custom naming 
strategy to be used as an example if someone ever needed to enforce custom conventions.

## Contents

- [Implementation](https://github.com/REDLab-Team/gnirps/tree/master/src/postgresql-database#implementation)
- [Properties](https://github.com/REDLab-Team/gnirps/tree/master/src/postgresql-database#properties)
    
## Implementation

The tools chosen to manage this connection in fitting way for Spring are JPA and its implementation Hibernate. Creating 
DAO (or repository, as we'd rather call it) becomes as easy as:
```
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CatRepository: JpaRepository<Cat, UUID> {
    override fun findAll(pageable: Pageable): Page<Cat>
}
```

This example is extracted from the cat-api example project, which uses and deploys a PostgreSQL database relying on 
this module.

## Properties

The following properties are imported by default:

```
gnirps:
  database:
    postgresql:
      url: jdbc:postgresql://${DATABASE_URL}/${DATABASE_NAME}
      username: ${DATABASE_USER}
      password: ${DATABASE_PASSWORD}
      driver-class-name: 'org.postgresql.Driver'
      default-schema: gnirps
      database-dialect: 'org.hibernate.dialect.PostgreSQLDialect'
      naming-strategy: 'org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl'
      hbm2ddl-auto: update
      use-sql-comments: true

spring:
  datasource:
    hikari:
      maximum-pool-size: 9
  jpa:
    generate-ddl: true
```

As you may have noticed, some rely on environment variables, which are most often provided by a stack or docker-compose 
file in a Docker environment.

The most important properties here are:
- `gnirps.database.postgresql.hbm2ddl-auto`: defines the behaviour relative to the DDL schema. It can be one of the 
following values: `validate` | `update` | `create` | `create-drop`. The safest one to use in a production environment 
is `validate` together with a proper database schema. `update` allows the generation of a database schema based on the 
code objects and is often used during the development times.
- `spring.datasource.hikari.maximum-pool-size`: size of the database connections' cache managed by hikari (a light-weight 
and performant JDBC DataSource implementation).

If other properties become important in the future, they can be added in both the configuration file and the class 
loading them: `com.gnirps.database.postgresql.config.JpaConfiguration`.