## ADDED Requirements

### Requirement: Colors, shape, and type scale match the style guide
`ui/theme/Color.kt`, `Theme.kt`, and `Shape.kt` SHALL use the brand, surface, and
status-badge colors, and the corner-radius values, documented in
`docs/farpost-style-guide.md` (main Farpost repo). The app SHALL use a single
light color scheme — no dark theme SHALL be implemented, matching the real, live
`farpost-web` app.

#### Scenario: Brand color matches the style guide exactly
- **WHEN** any primary action or accent element renders
- **THEN** its color is `#F97316` (or `#EA6C05` for a pressed/hover-equivalent state), matching the style guide's `brand`/`brand-dark` tokens

### Requirement: Real Inter font is not yet bundled
As of this spec, `Type.kt` SHALL use the platform default font
(`FontFamily.Default`) as a placeholder — the style guide's specified typeface,
Inter, SHALL NOT yet be bundled or loaded. This is a known, tracked gap, not an
intentional design choice; closing it is separate, smaller work.

#### Scenario: Rendered text uses the OS default typeface, not Inter
- **WHEN** any text renders in the current build
- **THEN** it uses the device's default system font rather than Inter
