# Bookkeeping Shared Blueprint

## Core Infrastructure To Reuse
- Shared query switch: `iw-web/src/main/java/com/itwray/iw/web/annotation/SharedQueryScope.java`
- Shared query aspect: `iw-web/src/main/java/com/itwray/iw/web/core/aop/SharedQueryScopeAspect.java`
- Shared query thread context: `iw-web/src/main/java/com/itwray/iw/web/utils/UserSharedQueryUtils.java`
- Shared data-permission expansion: `iw-web/src/main/java/com/itwray/iw/web/core/mybatis/UserDataPermissionHandler.java`
- Owner response abstraction:
  - `iw-web/src/main/java/com/itwray/iw/web/model/vo/AbstractUserOwnerVo.java`
  - `iw-web/src/main/java/com/itwray/iw/web/model/vo/UserOwnerDetailVo.java`
  - `iw-web/src/main/java/com/itwray/iw/web/model/vo/UserOwnerPageVo.java`
  - `iw-web/src/main/java/com/itwray/iw/web/support/UserOwnerFillSupport.java`
- Owner-name provider abstraction:
  - `iw-web/src/main/java/com/itwray/iw/web/service/UserNameQueryService.java`
  - `iw-feign-client/iw-auth-client/src/main/java/com/itwray/iw/auth/config/AuthUserClientAutoConfiguration.java`
- Family group auth client:
  - `iw-feign-client/iw-auth-client/src/main/java/com/itwray/iw/auth/client/AuthFamilyGroupClient.java`

## Backend Blueprint

### 1. Shared storage
- SQL adds `group_id` and `share_state` to `bookkeeping_records`.
- SQL also adds `idx_group_id`.
- Config registers `bookkeeping_records` in both:
  - `iw.dao.dataPermission.enable-table-names`
  - `iw.dao.dataPermission.share-scope-enable-table-names`

Reference:
- `iw-packaging-parent/iw-bookkeeping/scripts/iw-bookkeeping-init.sql`
- `iw-packaging-parent/iw-bookkeeping/src/main/resources/application.yml`

### 2. Current family group provider
- `BookkeepingCurrentGroupProvider` implements `UserCurrentGroupProvider`.
- It queries `AuthFamilyGroupClient.queryCurrentGroupId(userId)`.
- Fallback value is `0`, which means personal mode.

Reference:
- `iw-packaging-parent/iw-bookkeeping/src/main/java/com/itwray/iw/bookkeeping/core/mybatis/BookkeepingCurrentGroupProvider.java`

### 3. Shared query request contract
- Shared bookkeeping DTOs expose `Integer queryOnlyMyself`.
- They follow the convention:
  - `null` => shared scope
  - `1` => only-myself
- Shared query endpoints are annotated with `@SharedQueryScope`.

Reference:
- `iw-packaging-parent/iw-bookkeeping/src/main/java/com/itwray/iw/bookkeeping/model/dto/BookkeepingRecordPageDto.java`
- `iw-packaging-parent/iw-bookkeeping/src/main/java/com/itwray/iw/bookkeeping/model/dto/BookkeepingRecordListDto.java`
- `iw-packaging-parent/iw-bookkeeping/src/main/java/com/itwray/iw/bookkeeping/model/dto/BookkeepingStatisticsDto.java`
- `iw-packaging-parent/iw-bookkeeping/src/main/java/com/itwray/iw/bookkeeping/controller/BookkeepingRecordsController.java`

### 4. Default share write behavior
- On add/import, bookkeeping resolves current group first.
- If current group is invalid or missing, the record stays personal: `groupId = 0`, `shareState = NOT_SHARED`.
- If the user has a group, bookkeeping queries the auth-side default shared switch and maps it to `ShareStateEnum`.

Reference:
- `iw-packaging-parent/iw-bookkeeping/src/main/java/com/itwray/iw/bookkeeping/service/impl/BookkeepingRecordsServiceImpl.java`
- Methods worth copying:
  - `add(...)`
  - `importRecords(...)`
  - `queryCurrentGroupId(...)`
  - `queryDefaultShareState(...)`

### 5. Owner response abstraction
- Detail VO extends `UserOwnerDetailVo`.
- Page VO extends `UserOwnerPageVo`.
- Rank/top VO extends `AbstractUserOwnerVo`.
- Service layer fills owner fields through static `UserOwnerFillSupport.fill(...)`.
- `UserOwnerFillSupport` caches the resolved `UserNameQueryService`, so service classes do not need to inject any extra bean just for owner fill.

Reference:
- `iw-packaging-parent/iw-bookkeeping/src/main/java/com/itwray/iw/bookkeeping/model/vo/BookkeepingRecordDetailVo.java`
- `iw-packaging-parent/iw-bookkeeping/src/main/java/com/itwray/iw/bookkeeping/model/vo/BookkeepingRecordPageVo.java`
- `iw-packaging-parent/iw-bookkeeping/src/main/java/com/itwray/iw/bookkeeping/model/vo/BookkeepingStatisticsRankVo.java`
- `iw-packaging-parent/iw-bookkeeping/src/main/java/com/itwray/iw/bookkeeping/model/vo/yearly/consume/BookkeepingRecordsConsumeTopVo.java`
- `iw-packaging-parent/iw-bookkeeping/src/main/java/com/itwray/iw/bookkeeping/model/vo/yearly/income/BookkeepingRecordsIncomeTopVo.java`

### 6. Non-owner write protection
- Bookkeeping does not rely only on shared query permission.
- It loads the target record with data permission disabled, then compares `userId`.
- Non-owner updates and deletes are rejected explicitly.

Reference:
- `iw-packaging-parent/iw-bookkeeping/src/main/java/com/itwray/iw/bookkeeping/service/impl/BookkeepingRecordsServiceImpl.java`
- Methods worth copying:
  - `update(...)`
  - `delete(...)`
  - `queryEditableRecord(...)`
  - `queryRecordByIdIgnorePermission(...)`

## App Blueprint

### 1. Query scope store
- The newer preferred pattern is a family-group level global scope store.
- Keep `stores/bookkeeping-query-scope.js` only as a compatibility wrapper if the bookkeeping module still imports it.
- Effective default:
  - has family group => shared
  - no family group => only-myself
  - child role => forced only-myself
- Request value rule:
  - only-myself => `1`
  - shared => `null`

Reference:
- `iw-mixes-app/stores/bookkeeping-query-scope.js`
- `iw-mixes-app/stores/family-shared-scope.js`

### 2. Scope switch component
- The earlier bookkeeping-only scope switch component should no longer be inserted on every business page.
- Preferred entry is the family-group detail/settings page, which exposes a single global `查看范围` setting.

Reference:
- `iw-mixes-app/components/bookkeeping-query-scope.vue`
- `iw-mixes-app/pagesBase/family/detail.vue`

### 3. Query pages
- Shared bookkeeping pages inject `queryOnlyMyself: scopeStore.queryOnlyMyself` into every request.
- Shared list cards append owner text only when the record belongs to another user.
- Statistics pages pass the same scope parameter and refresh when the scope changes.

Reference:
- `iw-mixes-app/pagesBookkeeping/bookkeeping/bookkeeping-records.vue`
- `iw-mixes-app/pagesBookkeeping/bookkeeping/bookkeeping-consume-statistics.vue`
- `iw-mixes-app/pagesBookkeeping/bookkeeping/bookkeeping-yearly-overview-statistics.vue`
- `iw-mixes-app/pagesBookkeeping/bookkeeping/bookkeeping-yearly-consume-statistics.vue`
- `iw-mixes-app/pagesBookkeeping/bookkeeping/bookkeeping-yearly-income-statistics.vue`
- `iw-mixes-app/pages/bill/index.vue`
- `iw-mixes-app/pages/home/index.vue`

### 4. Detail and action pages
- Detail page shows `userName` when present.
- If `canEdit === false`, the page becomes read-only and displays a shared-record tip.
- Action/edit page blocks direct navigation when the detail response says `canEdit === false`.

Reference:
- `iw-mixes-app/pagesBookkeeping/bookkeeping/bookkeeping-detail.vue`
- `iw-mixes-app/pagesBookkeeping/bookkeeping/bookkeeping-action.vue`

## Porting Rules For A New Module
- Replace bookkeeping-specific names only after the shared table, query endpoints, and app pages are enumerated.
- Keep the request/response field names unchanged unless the shared infrastructure itself changes.
- If the module has subdomains with different rollout timing, record the excluded tables up front and leave them private until the next phase.
