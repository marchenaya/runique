# Runique — Homework / Extension Ideas

Feature ideas to extend the app beyond the course. Uses only existing data / public APIs (the
backend API is static and can't be extended).

## 1. Extend the Analytics feature

The dynamic-delivery analytics module is intentionally simple. Extend it:

- Add more metrics (e.g. total number of runs).
- Add performance-over-time graphs (e.g. average distance per run over a selectable from/to date
  range) to visualize progress.
- Mock-ups provided: see `analytics-dashboard.webp` and `analytics-detail.webp` in this folder.
- Requires no new tracked data — reuse existing run data.

## 2. Readable location on the Run Overview screen

- Show a human-readable location per run (e.g. "Central Park"). Location is already saved remotely
  but currently unused.
- Use the Google Places API (via Google Cloud Console).

## 3. Remote map images instead of local screenshot workaround

- Replace the fragile local map-snapshot workaround (shrink map → transparent → snapshot) with a
  Google Maps remote static-image API that returns an image for a list of locations.
- More reliable; removes the local snapshot hack.

## 4. More locally-tracked data types

API doesn't support extra data types, so track and store these locally (Room):

- Steps taken during a run.
- Step length.
- Calories burned — from Wear OS health services, OR estimate locally from a formula using distance,
  average speed, duration, and user-entered weight/height.

## 5. Goal system

- Let user set running goals (e.g. "run 10 km per session by November").
- Show current average vs goal in analytics (e.g. progress with a dotted target line).

## 6. Wear OS course

- Do the companion Wear OS course — covers multi-module architecture + a new application module
  concept not covered in the mobile course.