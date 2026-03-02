---
name: iw-backend-feature-template
description: Implement standard backend CRUD features in iw-mixes (especially iw-auth) with project-native templates. Use when users ask to add a new feature end-to-end, including table design, Entity/DTO/VO, Mapper/Dao, Service, Controller, enum fields with BusinessConstantEnum/BoolEnum conventions, and compile verification.
---

# IW Backend Feature Template

## Overview

Implement new feature modules with the existing coding style in `iw-mixes`.
Prioritize minimal changes, strict naming consistency, and complete delivery from SQL to API.

## Workflow (CRUD Feature)

1. Confirm module and feature scope
- Identify target service: `iw-packaging-parent/<service>`.
- Decide whether this is standard CRUD (`add/update/delete/detail/page`) or a custom business flow.
- If only one service is involved, avoid introducing Feign/MQ complexity.

2. Use canonical file layout
- SQL: `iw-packaging-parent/<service>/scripts/*-init.sql` (append-only).
- Java: `model/entity`, `model/dto`, `model/vo`, `mapper`, `dao`, `service`, `service/impl`, `controller`.
- Mapper XML: `src/main/resources/mapper/*Mapper.xml`.

3. Design table and entity
- Use `id int unsigned auto_increment` as default primary key.
- User-scoped tables must include `deleted/create_time/update_time/user_id` and entity should extend `UserEntity<Integer>`.
- Add practical indexes (`user_id`, filter fields like `category/status`).
- If a field is business enum status, store numeric code (`tinyint/int`) instead of free-text.
- If a field is binary boolean-like (`是否xx`), use `tinyint(1)` and values `0/1` (default usually `0`).

4. Build model objects
- `*AddDto`: request fields + constraints.
- `*UpdateDto`: extends AddDto + `id`.
- `*PageDto`: extends `PageDto` + query fields.
- `*DetailVo`/`*PageVo`: response fields, include `createTime/updateTime` when needed.
- If API expects array values (e.g., tags), define collection type in DTO/VO and convert in service when DB stores string/json.
- If field enum implements `BusinessConstantEnum`, use enum type directly in `Entity/DTO/VO` (do not keep `Integer` code type in these objects).
- If field value is `BoolEnum` semantics (`0/1`), keep `Integer` type in `Entity/DTO/VO`.

5. Build persistence and service stack
- `Entity` + `Mapper` + `Dao` use existing BaseDao/MyBatis-Plus patterns.
- `Service` extends `WebService<AddDto, UpdateDto, DetailVo, Integer>`.
- `ServiceImpl` extends `WebServiceImpl` and adds custom `page(...)` plus field transform logic.
- For `BusinessConstantEnum` fields, do not write `normalizeXxx` conversion logic; use enum object directly and only check `!= null` when building optional query conditions.
- For `BoolEnum`-semantic `0/1` fields, keep `Integer` and do not write `normalizeXxx` validation; when value is `null`, skip setting/query condition, and let DB default value apply on insert.

6. Build controller endpoints
- Controller should extend `WebController` for standard CRUD endpoints.
- Add `POST /page` endpoint in controller for pagination query.
- Keep request mapping and Swagger annotations aligned with module style.

7. Validate before handoff
- Compile impacted module(s): `mvn -pl <module> -am -DskipTests compile`.
- Confirm enum/status defaults and validation behavior.
- Summarize API paths, SQL changes, and potential compatibility risks.

## Output Contract

- Start with a short plan.
- Deliver concrete file changes in execution order.
- Explicitly list: SQL update, object model update, controller endpoints, verification command.
- If touching enums/status semantics, include migration/compatibility notes.
- Run `references/feature-delivery-checklist.md` before final response.

## Resources

- Read `references/feature-delivery-checklist.md` for execution and handoff checks.
- Read `references/iw-auth-crud-blueprint.md` when implementing standard CRUD in `iw-auth`.
