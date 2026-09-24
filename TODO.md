# TODO / Roadmap

Samlade actionpunkter från utvecklingsarbetet, senast uppdaterad 2026-09-17.
Föreslagen prioritetsordning finns längst ner.


 ## TODO – AI Memory

[ X ] Skapa UserModel
    - id
    - name
    - email

[ X ] Skapa UserRepository
    - JpaRepository<UserModel, Long>

[ X ] Skapa UserService
    - saveUser()
    - getUsers()
    - getUserById()
    - deleteUser()

[ X ] Skapa UserController
    - POST /users
    - GET /users
    - GET /users/{id}
    - DELETE /users/{id}

[ ] Skapa ProjectUserModel
    - id
    - projectId
    - userId
    - role

[ ] Skapa ProjectUserRepository

[ ] Skapa ProjectUserService

[ ] Skapa ProjectUserController

[ ] Uppdatera AiMemoryModel
    - projectId
    - createdBy
    - createdAt
    - updatedAt

[ ] Uppdatera AiMemoryService
    - spara memory kopplat till project
    - spara vem som skapade memory
    - hämta memory för ett specifikt project

[ ] Testa relationen:
    User → ProjectUser → Project → Memory

[ ] Därefter:
    Koppla AI-modellen till memory-systemet
    - searchMemory()
    - saveMemory()
    - senare delete/update memory


## AI-säkerhet & datahantering

Från diskussionen om OpenAI-säkerhet inför mötet om offerter/kunddata.

- [ ] Skriv under OpenAIs Data Processing Addendum (DPA) på organisationskontot
      (platform.openai.com → org-inställningar).
- [ ] Överväg att sätta upp ett nytt OpenAI Project med EU-region
      (`eu.api.openai.com`), som ger zero data retention på köpet. Kräver att
      API-nyckeln och `spring.ai.openai.base-url` migreras till det nya
      projektet — går inte att konvertera ett befintligt project i efterhand.
- [ ] Minska vad som skickas tillbaka till modellens kontext från
      `ProjectTools`: låt `updateProject`/`createProject`/`deleteProject`
      returnera en enkel bekräftelse istället för hela `ProjectModel`
      (kundnamn + pris) i varje tool-svar.
- [ ] Bygg "jämför vår prissättning mot marknaden" som en egen, avgränsad
      funktion (t.ex. `MarketResearchTools`) som bara läser den generella
      rate carden (`ResourceService`/`RoleService` — timpriser per roll),
      **aldrig** kund- eller projektdata.
- [ ] För webbsökning: bygg en egen `@Tool` mot ett sök-API (Brave Search,
      Tavily, SerpAPI e.dyl.) istället för att förlita sig på OpenAIs
      inbyggda `web_search`. Spring AI stödjer just nu bara OpenAIs
      preview-modeller (`gpt-4o-mini-search-preview`) för det, inte
      `gpt-4o-mini` som appen redan använder — se
      [spring-ai#4312](https://github.com/spring-projects/spring-ai/issues/4312).
- [ ] (Parkerat, inte bråttom) RAG över projekthistorik — bara motiverat när
      antalet historiska projekt/offerter blir för stort för att skicka in i
      en enskild systemprompt. Spring AI har inbyggt stöd
      (`RetrievalAugmentationAdvisor` + `VectorStore`) när det blir aktuellt.
- [ ] Självhostad modell (Ollama) — medvetet valt bort pga prestanda/kvalitet.
      Ligger kvar som eskaleringsväg om ledningen kräver att ingen data
      någonsin får lämna egen infrastruktur, oavsett OpenAIs retention-policy.

## Kända buggar / halvfärdiga saker

- [ ] `AuditLogService.getAll()` sorterar fortfarande med
      `Collections.reverse(copy)` på fysisk listordning. Byt till
      `copy.sort(Comparator.comparingLong(AuditLogEntryModel::id).reversed())`
      — annars kan ordningen bli fel under samtidiga skrivningar (id-tilldelning
      och listinsättning är var för sig trådsäkra, men inte som kombination).
- [ ] `ProjectController` saknar `GET /api/costcompass/projects/{id}`.
      `ProjectDetail.tsx` hämtar just nu hela projektlistan och filtrerar
      fram rätt id client-side — fungerar, men en riktig `@GetMapping("/{id}")`
      vore renare REST-design.
- [ ] `TaskModel.estimatedExecutionTime`/`estimatedVerificationTime`
      (`java.time.Duration`) kan krascha tool-calling om LLM:et skriver t.ex.
      `"5h"` istället för ISO-8601 (`"PT5H"`). Lägg till
      `@JsonPropertyDescription` med exempel på fälten, eller byt till en
      enkel `Long`-minuter-typ om felet dyker upp igen.

## README.md är delvis inaktuell

- [ ] "Tech stack" anger fortfarande "Spring AI (Ollama)" trots att appen kör
      OpenAI (`spring.ai.openai.*`).
- [ ] "Status"/"Implemented" beskriver AI Chat som "UI only" — stämmer inte
      längre (tool-calling CRUD mot Projects, audit log, routing är på plats).
- [ ] Verifiera om Kafka-events för Resources och Projects faktiskt publiceras
      från sina services nu (README säger typerna finns men är inte inkopplade
      — dubbelkolla `ResourceService`/`ProjectService` mot `EventPublishers`).

## Möjliga nästa steg (ej påbörjade)

- [ ] Stats-dashboard ovanpå audit-loggen (nästa lager efter grundfunktionen).
- [ ] Dead-letter-topic-viewer/endpoint (alternativet vi valde bort mot
      audit-loggen — kan fortfarande vara värt att bygga senare).
- [ ] Skala `@Tool`-designen till fler entiteter (Resources, Roles, Tasks) —
      troligen en `@Tool`-klass per entitet (`ResourceTools`, `RoleTools`,
      `TaskTools`), samma mönster som `ProjectTools`.
- [ ] Persistent lagring (databas istället för in-memory-store).
- [ ] Automatiserade tester (JUnit 5 / Mockito).
- [ ] Fler Kafka-koncept att öva på: partitions + keys i praktiken, skala en
      consumer group med flera instanser.

## Föreslagen ordning

1. **Snabba bugfixar** — audit log-sorteringen och `Duration`-formatet är små
   ändringar som förbättrar korrekthet direkt.
2. **DPA + minska tool-svarens innehåll** — låg insats, täcker det som kom upp
   på mötet, blockerar inget annat arbete.
3. **`GET /projects/{id}`** — liten, gör frontend renare inför fortsatt arbete.
4. **README-uppdatering** — housekeeping, gör det enkelt att komma tillbaka
   till projektet efter ett uppehåll.
5. **Marknadsjämförelse-funktionen** — större feature, men självständig och
   inte beroende av något av ovanstående.
6. **Skala `@Tool`-designen till fler entiteter** — gör när Resources/Roles/
   Tasks ska in i AI-chatten.
7. **RAG, persistent lagring, tester, EU-region-migrering** — större/senare
   arbete, inget akut.
