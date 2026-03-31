---
name: iw-family-group-shared-data
description: Add family-group shared data support to iw-mixes business modules across backend and app. Use when a new or existing user-scoped feature needs 家庭共享/仅自己 query scope, default shared write behavior, owner fields (`userId`/`userName`/`canEdit`), or owner-only edit/delete protection.
---

# IW Family Group Shared Data

## Overview

Use this skill when an `iw-mixes` business module must support family-group shared visibility on top of user-owned data.
Reuse the existing shared-query, owner-fill, and app query-scope infrastructure. Do not invent a new sharing model per module.

## When To Use

- New or existing user-scoped business tables need family-group shared query support.
- The feature needs a query scope switch between `家庭共享` and `仅自己`.
- Response objects must expose owner info fields: `userId`, `userName`, `canEdit`.
- Detail/list/page/statistics endpoints should support shared visibility, while writes remain owner-only.

Do not use this skill when the current phase explicitly excludes the module from sharing.
If the work also includes base feature delivery, combine this skill with `$iw-mixes-ai-development`.

## Workflow

1. Confirm sharing boundary
- Identify which tables and endpoints enter the shared scope in this phase.
- Explicitly list related modules that stay private for now.
- Confirm the main table is user-owned. This pattern assumes `user_id` ownership.

2. Implement backend shared data model
- Shared business tables should persist `group_id` and `share_state`.
- On create/import, derive current group and default share state from `AuthFamilyGroupClient`.
- Fallback rule: no family group or auth query failure means `group_id = 0` and `share_state = NOT_SHARED`.
- Register the table in both data-permission enable list and `share-scope-enable-table-names`.
- If the service has no group provider yet, add a `UserCurrentGroupProvider` implementation.

3. Implement shared query contract
- Shared-query DTOs should implement `SharedQueryRequest`.
- Standard request field name is `Integer queryOnlyMyself`.
- Contract rule: `null` means shared scope, `1` means only-myself.
- Annotate shared-query controller methods or controller classes with `@SharedQueryScope`.
- Prefer the existing `iw-web` data-permission + shared-scope flow instead of hand-written group filtering.

4. Implement owner response contract
- Detail/page VOs should extend `UserOwnerDetailVo` or `UserOwnerPageVo`.
- Aggregate/top/rank style VOs should extend `AbstractUserOwnerVo`.
- Fill owner fields with static `UserOwnerFillSupport.fill(...)`.
- Do not inject a dedicated bean only for owner fill. `UserOwnerFillSupport` lazily resolves and caches `UserNameQueryService`.
- If `UserNameQueryService` is absent, treat it as graceful degradation: owner name stays empty, but `userId` and `canEdit` still work.

5. Protect writes explicitly
- Update/delete/edit actions must verify record ownership, not only query permission.
- Load the target record with user data permission temporarily disabled, then compare `userId` with current user.
- Reject non-owner writes with a clear business exception.

6. Adapt the app
- Prefer a family-group level global query-scope store over page-level toggles.
- Use the family-group settings page as the single visible entry for changing shared-data view scope.
- Shared business pages should consume the global scope and avoid inserting a full query-scope control on every page.
- Default rule: users with a family group default to shared scope; otherwise default to only-myself.
- Child role rule: child users are forced to `仅自己` on the backend and app, even if the request asks for shared scope.
- Every shared-query request must pass `queryOnlyMyself: scopeStore.queryOnlyMyself`.
- In shared view, show owner text only when the record is from another user.
- Hide or disable edit/delete operations when `canEdit === false`.

7. Validate before handoff
- Compile impacted backend modules.
- Smoke-test: no-group user, group user shared view, group user only-myself view, non-owner detail, non-owner update/delete.
- Verify app behavior on list/detail/statistics pages, including empty states and scope switching.

## Output Contract

- Start from the sharing boundary first, then apply backend, app, and validation changes in that order.
- Call out excluded modules explicitly when the rollout is phased.
- Summarize at least: data model changes, shared query endpoints, owner-response changes, write-permission rule, app scope behavior, verification commands.

## References

- Read `references/shared-delivery-checklist.md` for the end-to-end implementation checklist.
- Read `references/bookkeeping-shared-blueprint.md` when you need a concrete example based on the existing bookkeeping implementation.
