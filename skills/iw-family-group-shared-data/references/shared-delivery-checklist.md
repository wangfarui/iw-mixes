# Family Shared Delivery Checklist

## Requirement Intake
- Business module:
- Shared scope tables:
- Excluded related tables/modules in this phase:
- Shared query endpoints:
- Write endpoints:
- App pages to adapt:
- Cross-service dependencies:
- Backward compatibility constraints:

## Backend Checklist

### Shared storage model
- [ ] Confirm the main table is user-owned.
- [ ] Add `group_id` and `share_state` to the shared table if missing.
- [ ] Add index for `group_id` if shared queries may scan by group.
- [ ] Keep SQL append-only and idempotent in `scripts/`.
- [ ] Update entity fields to carry `groupId` and `shareState`.

### Data permission and shared query infra
- [ ] Add the table to `iw.dao.dataPermission.enable-table-names`.
- [ ] Add the table to `iw.dao.dataPermission.share-scope-enable-table-names`.
- [ ] Confirm the service module has a `UserCurrentGroupProvider` implementation.
- [ ] Reuse `AuthFamilyGroupClient` for current group and default shared queries.
- [ ] Fallback to personal mode when auth client fails or user has no group.

### Query API contract
- [ ] Shared-query DTOs implement `SharedQueryRequest`.
- [ ] Use the standardized field name `queryOnlyMyself`.
- [ ] Keep `queryOnlyMyself` as `Integer`, where `null` means shared scope and `1` means only-myself.
- [ ] Annotate shared-query controller methods or classes with `@SharedQueryScope`.
- [ ] Cover all shared views consistently: `page`, `list`, `detail`, `statistics`, yearly/overview/top/rank queries.

### Owner response contract
- [ ] Detail VO extends `UserOwnerDetailVo`.
- [ ] Page record VO extends `UserOwnerPageVo`.
- [ ] Aggregate VO extends `AbstractUserOwnerVo` if owner info is needed.
- [ ] Fill owner data with static `UserOwnerFillSupport.fill(...)`.
- [ ] Do not add per-service bean injection only for owner info fill.

### Write protection
- [ ] Update/delete paths query target data with permission temporarily disabled.
- [ ] Compare target `userId` with current user before modifying data.
- [ ] Reject non-owner writes with an explicit business exception.
- [ ] Keep delete and update behavior aligned.

### Default share behavior
- [ ] On add/create, write `groupId` from current family group.
- [ ] On add/create, write `shareState` from default shared switch.
- [ ] On import/batch-create, reuse the same defaulting rules.
- [ ] Preserve existing owner/group/share fields during update unless the business explicitly allows changing them.

## App Checklist
- [ ] Add or reuse a global family-group query-scope store instead of a per-page toggle.
- [ ] Use the family-group settings page as the visible entry for changing shared-data view scope.
- [ ] Effective default: has family group => shared; no family group => only-myself.
- [ ] Child role must be forced to only-myself in both app state and backend enforcement.
- [ ] Every shared-query request passes `queryOnlyMyself`.
- [ ] Shared list cards show owner text only for non-own records.
- [ ] Shared detail page shows owner info when available.
- [ ] Edit/delete entrance is hidden or disabled when `canEdit === false`.
- [ ] Non-owner edit navigation is blocked even if the user reaches the page directly.
- [ ] Shared empty-state and filter refresh behavior are verified.

## Verification Checklist
- [ ] `mvn -pl <module> -am -DskipTests compile` passes.
- [ ] No-group user sees only own data.
- [ ] Group user shared view sees shared family data.
- [ ] Group user only-myself view sees only own data.
- [ ] Non-owner detail can view but cannot edit/delete.
- [ ] Non-owner update/delete API returns the expected error.
- [ ] Auth client failure falls back to personal mode instead of breaking the request.
- [ ] App scope switching refreshes data and statistics correctly.

## Final Handoff Template
- Scope:
- Shared tables:
- Excluded modules:
- Backend API summary:
- Owner-response summary:
- App behavior summary:
- Verification commands executed:
- Risks and compatibility notes:
