## [Unreleased]

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
