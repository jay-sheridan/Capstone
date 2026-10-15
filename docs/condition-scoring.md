# Car365 condition scoring

The existing inspection report stores `score`, `rating`, `score_version`, `approved`, and `emissions_applicable`. Score version 1 uses only Engine 20/10/0, Transmission 15/8/0, Brakes 20/10/0, Tires 10/5/0, Body 10/5/0 and Frame 25/12/0 for Good/Fair/Poor. Other findings remain part of the inspection but do not change these points.

The assigned inspection agency submits through the existing report endpoint. It accepts component ratings, not a manually supplied score or rating. The backend computes the score both in the service and in persistence callbacks. Missing or unsupported component ratings produce a null score. Approval requires PASS and six supported ratings. Saving without approval revokes previous approval and hides the listing; approving again still requires the seller's price and publication consent.

Every listing/report response includes a `condition` summary with the same saved score, rating, certification state and component breakdown. Final scores are exposed only for approved, complete, dated reports using version 1. Certification additionally requires PASS, VIN and odometer verification, safety passed, emissions passed unless explicitly marked not applicable, and no Poor brakes or frame. A passing overall result alone never grants certification. The UI identifies certification as a platform designation rather than government safety certification.

## Existing records and schema

All added columns are nullable. Legacy scores were calculated with a different formula and are not advertised as version 1 scores. Legacy reports and their documents/photos remain available; the assigned inspector can open the existing form, review the saved checks, complete missing ratings and approve to adopt the new score. No bulk approval or destructive reset is performed.

The configured local Neon profile uses Hibernate `ddl-auto=update` and adds these columns on startup. For environments using managed migrations, apply the equivalent additive SQL before deploying:

```sql
ALTER TABLE inspection_reports ADD COLUMN IF NOT EXISTS rating varchar(255);
ALTER TABLE inspection_reports ADD COLUMN IF NOT EXISTS score_version integer;
ALTER TABLE inspection_reports ADD COLUMN IF NOT EXISTS approved boolean;
ALTER TABLE inspection_reports ADD COLUMN IF NOT EXISTS emissions_applicable boolean;
```

Null emissions applicability conservatively requires an emissions pass. New forms default to applicable. There is no existing admin inspection UI in this repository; the shared summary is available for future admin views without adding or changing roles.

## Verification

Backend: `./mvnw.cmd -o test` (database integration tests require the separately configured `NEON_TEST_DATABASE_*` test branch).

Web dashboard: `npm run build` and `node --test tests/condition-score.cjs`.

Tests cover all 729 rating combinations, rating boundaries, missing/unsupported/legacy data, high scores with failed safety/verification, emissions exemption, poor brakes/frame, persistence recalculation, assigned-agency ownership, incomplete approval rejection, approval revocation, preservation of omitted findings, and card/detail consistency.
