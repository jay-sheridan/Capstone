# Car365 Assistant

A floating “Ask Car365” button appears for signed-in buyers, sellers and agencies. The responsive panel supports suggested questions, recent conversation context, typing status, error/retry handling, clearing the conversation and keyboard Enter/Shift+Enter/Escape controls.

The assistant uses the existing private `GEMINI_API_KEY` and `GEMINI_MODEL` configuration. `POST /api/v1/chat` requires the existing authenticated session; no roles or authentication restrictions are relaxed. Each message is limited to 1,500 characters, context to eight previous turns, and replies to 4,000 characters. Only one AI request can be in progress per account; sequential follow-ups need no cooldown. Each provider attempt times out after 15 seconds. Transient network/502/503/504 failures retry once, while quota errors do not trigger more requests. The browser releases a stuck request after 35 seconds and preserves the question for retry.

Common questions about selling, scores, certification, filters, mileage and AI prices use short built-in help responses without a provider call. Other replies are instructed to use simple language and usually no more than 60 words. If AI is unavailable, the chat displays clearly labelled basic help instead of an empty response or provider error.

For local stability, `jwt-local.properties` may supply a persistent private `JWT_SECRET_KEY`; it is ignored by Git. Reuse it across server restarts instead of generating a new signing key each time. The running development backend disables automatic DevTools restarts to avoid partially compiled application startup and duplicate servers competing for port 8080.

Only the question and recent chat turns are sent to Gemini. No account records, VINs, documents, inventory or inspection records are automatically attached. The panel explains that messages are sent to Google and advises against entering private information. Conversation history is held in browser memory and is cleared on logout/reload or with New Chat; it is not persisted in the database or local storage.

The system instructions describe the actual Car365 filters, inspection weights, certification requirements and seller publication flow. The chatbot cannot perform actions or look up live accounts, prices or inventory, and is instructed not to invent those facts. It provides guidance and directs users to actual listings and reports.

`GeminiChatTests` covers successful replies with history, account-specific request spacing, invalid/oversized inputs, forbidden system-role history, missing configuration, provider quota and empty responses. Tests use a mocked provider and no real credentials.
