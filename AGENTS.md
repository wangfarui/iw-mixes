## Skills

### Available skills
- iw-mixes-ai-development: Project-local development skill for `iw-mixes_ai`. Use when implementing or updating features in this workspace, including standard backend CRUD in `iw-mixes` and any required web/app coordination. (file: `/Users/wangfarui/workspaces/wfr/iw-mixes_ai/skills/iw-mixes-ai-development/SKILL.md`)
- iw-family-group-shared-data: Add family-group shared data support to `iw-mixes` business modules across backend and app. Use when a feature needs 家庭共享/仅自己 query scope, default shared write behavior, owner fields (`userId`/`userName`/`canEdit`), or owner-only edit/delete protection. (file: `/Users/wangfarui/workspaces/wfr/iw-mixes_ai/iw-mixes/skills/iw-family-group-shared-data/SKILL.md`)

### How to use skills
- Use `iw-mixes-ai-development` as the default skill for backend work in this repo.
- Use `iw-family-group-shared-data` for family-group shared visibility and owner-info adaptation.
- If a task needs both base feature delivery and shared-data support, use `iw-mixes-ai-development` first, then apply `iw-family-group-shared-data`.
- Read only the reference files needed for the current module instead of bulk-loading all references.
