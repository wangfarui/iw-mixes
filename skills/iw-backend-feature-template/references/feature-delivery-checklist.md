# Feature Delivery Checklist

## Requirement Intake Template
- Business goal:
- Target users and scenarios:
- Primary module:
- API changes (new/modify/remove):
- Data model changes:
- Cross-service calls:
- Performance/security constraints:
- Backward compatibility requirements:

## Implementation Checklist
- [ ] Confirm module placement and ownership.
- [ ] Confirm API contract and naming (`add/update/delete/detail/page` where applicable).
- [ ] Add SQL changes under module `scripts/` with idempotent and append-only style.
- [ ] Add/update `Entity`, `Mapper`, `Mapper.xml`, `Dao`, `Service`, `Controller`.
- [ ] Apply validation annotations and error behavior consistent with existing patterns.
- [ ] For status/类型字段（`BusinessConstantEnum`），`Entity/DTO/VO` 直接使用枚举类型，不使用 `Integer` 码值对象。
- [ ] For binary `是否xx`字段，使用 `Integer` + `BoolEnum` 语义 `0/1`，不新增单独业务枚举。
- [ ] For enum/bool optional filters, avoid `normalizeXxx`; use non-null checks directly.
- [ ] For bool `0/1` inserts, when value is null, rely on DB default instead of service-side default assignment.
- [ ] Update `application.yml` (`spring.application.name`, nacos import, springdoc scan path).
- [ ] Update data-permission table list if new user-scoped table is added.
- [ ] Add/adjust Feign client contract if cross-service call is introduced.
- [ ] Add/adjust gateway routes and internal security checks if service boundary changes.
- [ ] Ensure Redis key definitions have TTL and follow enum-based patterns.
- [ ] Ensure MQ publish flow uses existing helper and destination enums.

## File List Template
- `iw-packaging-parent/<service>/scripts/<service>-init.sql`
- `iw-packaging-parent/<service>/src/main/java/.../model/entity/*Entity.java`
- `iw-packaging-parent/<service>/src/main/java/.../mapper/*Mapper.java`
- `iw-packaging-parent/<service>/src/main/resources/mapper/*Mapper.xml`
- `iw-packaging-parent/<service>/src/main/java/.../dao/*Dao.java`
- `iw-packaging-parent/<service>/src/main/java/.../service/*Service.java`
- `iw-packaging-parent/<service>/src/main/java/.../service/impl/*ServiceImpl.java`
- `iw-packaging-parent/<service>/src/main/java/.../controller/*Controller.java`
- `iw-packaging-parent/<service>/src/main/resources/application.yml`
- `iw-feign-client/<service>-client/src/main/java/.../*Client.java` (if needed)
- `iw-packaging-parent/iw-gateway/src/main/resources/application.yml` (if needed)

## Verification Checklist
- [ ] Build and compile pass for impacted modules.
- [ ] Core endpoint smoke tests pass.
- [ ] Data permission behavior verified for new/changed tables.
- [ ] Feign call request/response and error behavior verified.
- [ ] Gateway routing and auth checks verified.
- [ ] SQL execution order and rollback notes documented.

## Final Handoff Template
- Scope:
- Changed modules:
- API summary:
- SQL summary:
- Config summary:
- Risks and compatibility notes:
- Verification commands executed:
