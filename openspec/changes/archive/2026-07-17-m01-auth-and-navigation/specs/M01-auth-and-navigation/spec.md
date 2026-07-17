## ADDED Requirements

### Requirement: Sign-in via WelcomeScreen
`WelcomeScreen` SHALL present email and password fields and a "Sign in" action. On
submit, it SHALL call `AuthRepository.login(email, password)` and SHALL show a
loading indicator in place of the action while the call is in flight. On failure,
it SHALL show a generic "Invalid email or password" message — it SHALL NOT
distinguish bad credentials from a user with zero role-memberships, matching
`farpost-web`'s login page.

#### Scenario: Successful sign-in
- **WHEN** a user submits valid credentials
- **THEN** a session is created and persisted, and the app navigates away from `WelcomeScreen`

#### Scenario: Failed sign-in shows a generic error
- **WHEN** a user submits invalid credentials, or valid credentials for an account with no role-memberships
- **THEN** the same generic "Invalid email or password" message is shown, and the user remains on `WelcomeScreen`

### Requirement: Session persistence
`AuthRepositoryImpl` SHALL persist a successful login's session to DataStore, and
SHALL derive `sessionState: StateFlow<SessionState>` from that same store —
`Loading` before the first read completes, then `SignedOut` or `SignedIn(session)`.

#### Scenario: Session survives app restart
- **WHEN** the app is relaunched after a previous successful login
- **THEN** `sessionState` resolves to `SignedIn` without requiring the user to log in again

### Requirement: Capability-based destination routing
On any `sessionState` change, the app SHALL route to a destination determined by
the session's capabilities, in priority order: `ADMIN` > `PROFESSIONAL` > `OWNER`.
A `SignedOut` session, or a session matching none of those, SHALL route to
`Welcome`. These three SHALL be the only capabilities recognized — role-specific
behavior (e.g. scout) SHALL NOT be modeled as a separate capability.

#### Scenario: Admin session routes to AdminHome
- **WHEN** a session is signed in with the `ADMIN` capability
- **THEN** the app navigates to `AdminHome`, regardless of any other capability also present

#### Scenario: Professional session routes to ProfessionalHub
- **WHEN** a session is signed in with the `PROFESSIONAL` capability and not `ADMIN`
- **THEN** the app navigates to `ProfessionalHub`

### Requirement: Sign-out
Every signed-in home screen (`AdminHome`, `ProfessionalHub`, `OwnerHome`) SHALL
expose a sign-out action. Signing out SHALL clear the persisted session and SHALL
route back to `Welcome`.

#### Scenario: Sign-out returns to Welcome
- **WHEN** a signed-in user triggers sign-out from any home screen
- **THEN** the session is cleared and the app navigates to `Welcome`
