# Seller AI price suggestions

After an agency approves a completed inspection, the seller's publication form automatically requests a preliminary CAD asking price and range. The seller can use the suggestion or type a different price; neither a suggestion nor the request publishes a listing or changes its asking price. Changing the asking price resets publication consent.

## Configuration

Copy `gemini-local.properties.example` to `gemini-local.properties`, set `GEMINI_API_KEY`, and restart the backend. The private file is ignored by Git. Environment variables with the same names also work. The default model is `gemini-3.8-flash`; Google rejected `gemini-2.5-flash` for the configured new-user account during validation. Model availability and free quotas are controlled by the Google project. The app does not enable billing or use paid search tools.

The integration uses the [Gemini generateContent REST API](https://ai.google.dev/api/generate-content), structured JSON responses, and a server-side API-key header. Keys never enter browser configuration or error responses. Replace any key shared in chat with a fresh key in the private local file.

## Inputs and limitations

Only the authenticated listing owner can call `POST /api/v1/seller/listings/{id}/price-suggestion`, and the listing must be READY_TO_PUBLISH with a completed, approved passing report. Inputs include decoded make/model/year, fuel/transmission, inspection score and components, recorded odometer kilometres, verification state, and estimated repair cost. VINs, names, emails, phone numbers, documents and photos are excluded. Missing mileage remains unknown and must not be invented.

This is a model estimate without live comparable listings. The panel labels that limitation and leaves the final price to the seller. If configuration, quota, model, network or response validation fails, the seller can still enter a price manually. The backend rejects negative, non-finite, missing, incorrectly ordered, wrong-currency and excessive price values.

Valid suggestions are cached in memory for six hours, up to 256 entries. Cache keys include the vehicle, report and pricing inputs, so changed inspection facts trigger a new suggestion. Restarting the backend clears the cache. Failed requests are not cached. No extra database columns or third-party dependencies are needed.

## Tests

Run `./mvnw.cmd -o test` from the backend and `npm run build` from the web dashboard. Pricing tests use a mocked HTTP transport and no real keys. A separate live validation used synthetic vehicle facts and successfully returned a structured CAD range.
