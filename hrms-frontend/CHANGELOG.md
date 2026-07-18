# Frontend Structural Cleanup — 2026-07-18

## Why
A structure/quality review found Angular components living inside `core/services/` (should hold only injectable services), a dead duplicate `src/core/` tree, duplicate assets, a broken spec file, and an `authInterceptor` that was written but never registered — leaving 5 services to hand-roll the same auth header logic it should have replaced.

## Changes

**1. Relocated misplaced components → `features/`**
Moved 8 component files out of `core/services/` into the `features/` folders that already held their templates: `login`, `register` (also renamed `LoginComponent`→`Login`, `RegisterComponent`→`Register` to match pre-existing spec files), `my-leaves`, `manager-requests`, `apply-leave`, `apply-wfh`, `attendance`, `my-requests`. Fixed `templateUrl`/`styleUrl` paths and all import paths (components, specs, `app.routes.ts`) accordingly. Fixed a literal-backslash typo in one import.

**2. Removed dead/duplicate code**
- Deleted `src/core/` (unused empty `Leave` service stub + spec)
- Deleted duplicate `src/assets/image/` folder
- Fixed `team.spec.ts`, which referenced a nonexistent `Team` symbol instead of `TeamService`

**3. Wired up the auth interceptor**
- Registered `authInterceptor` in `app.config.ts` (`provideHttpClient(withInterceptors([authInterceptor]))`)
- Removed the now-redundant manual `getHeaders()` / `Authorization` header code duplicated across `api.service.ts`, `leave.service.ts`, `request.service.ts`, `attendance.service.ts`, `team.ts`
- Cleaned up a dead code block in `main.ts`

**4. Rewrote `app.routes.ts`** with corrected imports and no stray comments

**5. Regression fix (found during live testing)**
The interceptor was attaching a stale `localStorage` token to the `/auth/login` and `/auth/register` calls, causing the backend to reject login with `403 Forbidden`. Fixed by excluding those two endpoints from the interceptor.

## Verification
- `npx ng build` — zero TypeScript/Angular compile errors
- Manually logged in via `ng serve` against the running Spring Boot backend — confirmed working end-to-end

## Out of scope (deferred to a future pass)
- Hardcoded `http://localhost:8080` URLs — no `environments/` files yet
- `any`-typed API responses — no request/response models
- Direct `localStorage` access not guarded for SSR (causes prerender warnings in `ng build`)

## Not part of this change
Pre-existing modifications already in the working tree before this session: 5 backend Java files, `dashboard.html`/`dashboard.scss`, `sidebar.component.html`, `tsconfig.app.json`/`tsconfig.spec.json`, and the untracked `team.ts`/`team.spec.ts`/`features/team/` files.
