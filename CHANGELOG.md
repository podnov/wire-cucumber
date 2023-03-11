## [Unreleased]
- Upgrade wiremock from `2.31.0` to `2.35.0`

## [0.16.3]
- Add request body matching

## [0.16.2]
- Add log message when performaing no-op mock verification

## [0.16.1]
- Fix mock verification while disabled

## [0.16.0]
- Modify mock verification to not cause exceptions when wire-cucumber isDisabled

## [0.15.0]
- Refactor to separate out building and verifying

## [0.14.0]
- Add convencience `resetWireMockServer` that respects `isDisabled` option
- Add functional test for `isDisabled` option
- Standardize on invocation instead of interaction verbiage

## [0.13.2]
- Update disabled feature to consider mocks finalized

## [0.13.1]
- Add `isDisabled` option

## [0.13.0]
- Move WireCucumberOptions acceptance to constructor
- Update functional test to uee a static WireCucumber and WireMockServer while resetting wire mock between scenarios

## [0.12.1]
- Upgrade wiremock to 2.31.0

## [0.12.0]
- Starting the wiremock server and initializing the cucumber steps don't need to be coupled, let's separate them

## [0.11.1]
- Test fix

## [0.11.0]
- [Breaking] Updated simple header request verification matching from `the request should have had header {string} {string}` to `the request should have had header {string} containing {string}`
- [Breaking] Request invocation index/state body verification has been changed from `request body of invocation` to the `request at invocation` pattern
- [Breaking] Request invocation index/state url verification has been changed from `request url of invocation` to the `request at invocation` pattern

## [0.10.0]
- Update various dependencies

## [0.9.2]
- Add support for body file name

## [0.9.1]
- Don't do close scenario verifications if the scenario is not PASSED

## [0.9.0]
- Upgrade to cucumber 6.x
