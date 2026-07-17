## ADDED Requirements

### Requirement: One row per held role
`ProfessionalHubScreen` SHALL render one row for every string present in the
signed-in professional's `roles` list. A professional with zero roles SHALL see an
explicit "No roles on this account" message instead of an empty list.

#### Scenario: Multiple roles render as multiple rows
- **WHEN** a professional's `roles` is `["adjuster", "scout"]`
- **THEN** two rows render, one per role

#### Scenario: Zero roles shows an explicit message
- **WHEN** a professional's `roles` is empty
- **THEN** "No roles on this account" is shown instead of an empty row list

### Requirement: Scout role navigates to a real screen; other roles do not yet
A `"scout"` role's row SHALL be clickable and SHALL navigate to `ScoutCapture`.
Every other role's row SHALL render as "Coming soon" and SHALL NOT be clickable —
this reflects that no other role has a real destination screen built yet, not a
permanent restriction.

#### Scenario: Tapping the scout row opens ScoutCapture
- **WHEN** a professional with the `"scout"` role taps their scout row
- **THEN** the app navigates to `ScoutCapture`

#### Scenario: Tapping a non-scout role's row does nothing
- **WHEN** a professional taps a role row for any role other than `"scout"`
- **THEN** no navigation occurs
