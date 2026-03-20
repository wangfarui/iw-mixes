## Skills

### Available skills
- iw-backend-feature-template: Implement standard backend CRUD features in `iw-mixes` with project-native templates. Use when users ask to add a new feature end-to-end, including SQL, Entity/DTO/VO, Mapper/Dao, Service, Controller, enum conventions, and compile verification. (file: `/Users/wangfarui/workspaces/wfr/iw-mixes_ai/iw-mixes/skills/iw-backend-feature-template/SKILL.md`)
- iw-family-group-shared-data: Add family-group shared data support to `iw-mixes` business modules across backend and app. Use when a feature needs 家庭共享/仅自己 query scope, default shared write behavior, owner fields (`userId`/`userName`/`canEdit`), or owner-only edit/delete protection. (file: `/Users/wangfarui/workspaces/wfr/iw-mixes_ai/iw-mixes/skills/iw-family-group-shared-data/SKILL.md`)

### How to use skills
- Use `iw-backend-feature-template` for standard backend CRUD delivery.
- Use `iw-family-group-shared-data` for family-group shared visibility and owner-info adaptation.
- If a task needs both CRUD scaffolding and shared-data support, use `iw-backend-feature-template` first, then apply `iw-family-group-shared-data`.
- Read only the reference files needed for the current module instead of bulk-loading all references.
