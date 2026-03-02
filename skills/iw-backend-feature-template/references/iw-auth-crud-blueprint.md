# iw-auth CRUD Blueprint

## Standard File Set

- `iw-packaging-parent/iw-auth/scripts/iw-base-init.sql`
- `iw-packaging-parent/iw-auth/src/main/java/com/itwray/iw/auth/model/enums/*StatusEnum.java` (when needed)
- `iw-packaging-parent/iw-auth/src/main/java/com/itwray/iw/auth/model/entity/*Entity.java`
- `iw-packaging-parent/iw-auth/src/main/java/com/itwray/iw/auth/model/dto/*AddDto.java`
- `iw-packaging-parent/iw-auth/src/main/java/com/itwray/iw/auth/model/dto/*UpdateDto.java`
- `iw-packaging-parent/iw-auth/src/main/java/com/itwray/iw/auth/model/dto/*PageDto.java`
- `iw-packaging-parent/iw-auth/src/main/java/com/itwray/iw/auth/model/vo/*DetailVo.java`
- `iw-packaging-parent/iw-auth/src/main/java/com/itwray/iw/auth/model/vo/*PageVo.java`
- `iw-packaging-parent/iw-auth/src/main/java/com/itwray/iw/auth/mapper/*Mapper.java`
- `iw-packaging-parent/iw-auth/src/main/resources/mapper/*Mapper.xml`
- `iw-packaging-parent/iw-auth/src/main/java/com/itwray/iw/auth/dao/*Dao.java`
- `iw-packaging-parent/iw-auth/src/main/java/com/itwray/iw/auth/service/*Service.java`
- `iw-packaging-parent/iw-auth/src/main/java/com/itwray/iw/auth/service/impl/*ServiceImpl.java`
- `iw-packaging-parent/iw-auth/src/main/java/com/itwray/iw/auth/controller/*Controller.java`

## Table Rules

- Keep append-only SQL in `iw-base-init.sql`.
- Use `id int unsigned auto_increment` primary key.
- User data tables should contain:
  - `deleted tinyint(1) default 0`
  - `create_time datetime default CURRENT_TIMESTAMP`
  - `update_time datetime default CURRENT_TIMESTAMP`
  - `user_id int unsigned default 0`
- Add `idx_user_id` index; add business indexes for page filters.

## Entity/DTO/VO Rules

- Entity extends `UserEntity<Integer>` for user-scoped tables.
- Fields based on `BusinessConstantEnum` should use enum type directly in `Entity/DTO/VO`.
- Binary `是否xx` fields should use `Integer` code + `BoolEnum` (`0/1`), and should not introduce a dedicated module enum unless truly needed.
- DTO validation:
  - Add DTO: required fields + max length.
  - Update DTO: extends Add DTO and adds `id`.
  - Page DTO: extends `PageDto`.
- VO:
  - Detail/Page VO should align with front-end shape.
  - Include `createTime/updateTime` if needed for display.

## Service/Controller Rules

- Service interface extends `WebService<Add, Update, Detail, Integer>` and defines `page(PageDto)`.
- ServiceImpl extends `WebServiceImpl` and handles:
  - custom page query conditions
  - optional field conversion (e.g. list tags <-> json string)
  - optional conditions via null checks (`field != null`)
- Controller extends `WebController` and provides `POST /page`.

## Status Enum Pattern

- Define enum in `model/enums`, implement `BusinessConstantEnum`.
- Example coding:
  - `ONLINE(1, "在线")`
  - `OFFLINE(2, "离线")`
- Use enum type directly in `Entity/DTO/VO` fields.
- For add/update/page input, do not write `normalizeXxx`; directly use enum value and only apply optional query conditions when non-null.

## Bool Flag Pattern

- For binary `是否xx` fields, use `BoolEnum` directly:
  - `0` -> `BoolEnum.FALSE`
  - `1` -> `BoolEnum.TRUE`
- Keep field type as `Integer` in `Entity/DTO/VO`.
- Do not write `normalizeXxx` validation.
- If input is null, insert flow should skip assignment and use DB default value.
- Query flow should add condition only when value is non-null.

## Delivery Checklist

- [ ] SQL table created/updated with indexes.
- [ ] Full CRUD object chain (`entity/mapper/dao/service/controller`) added.
- [ ] DTO/VO constraints and field types match API.
- [ ] Enum fields use `BusinessConstantEnum` style.
- [ ] `mvn -pl iw-packaging-parent/iw-auth -am -DskipTests compile` passes.
