Change Log
==========

v3.2.0
------

* Deprecated (Removed in the next major version)
  * All internal classes to handle push notifications on behalf of the parent application under the `gcm` package.
  * Deprecated class `AuthenticatorApplication`.
  * Deprecated methods `endpoint(...)` and `sslCert(...)` in `AuthenticatorConfig.Builder`.

v3.1.0
------

* Deprecated support for Codes (aka Tokens, T-OTPs).
* Fixed issue with `SecurityFragment` not calling the other Security Views when embedding fragment in custom Activities.
* Fixed issue with `AuthRequestManager` not clearing cache after detecting a pending Auth Request is already expired when responding.
* Fixed issue with `AuthenticatorManager` not returning correct values for `successful` and `error` when calling `AuthenticatorManager.linkDevice(...)` while listening to global event via `DeviceLinkedEventCallback`. Local, one-time callback `SimpleOperationCallback` returned proper data when calling the aforementioned method.
* Removed `AlertDialog` shown on error in `AuthRequestFragment` to have the parent `Activity` handle error objects instead.
* Prevented `SecurityFragment` from tinting/theming text and icons in `Toolbar` instances part of a parent `Activity` when embedding `SecurityFragment`.
* Updated Javadocs.

v3.0.4
------

* Added option in `AuthenticatorConfig.Builder` to set a configurable Activation Delay in seconds for passive factors.
* Fixed issue with checks around Service-set geo-fences depending on the dynamic policy sent by the Service.
* Improved Javadocs.

v3.0.3
------

* Fixed issue with passive factor checking when there were multiple instances of `AuthRequestFragment`.
* Improved flow with `AuthRequestFragment` when a device is unlinked due to 10 failed attempts at a knowledge factor verification.
* Improved background Bluetooth scanning for devices added as proximity factors.
* Improved image resources for when passive factors are being checked in `AuthRequestFragment` on devices with high resolution displays.

v3.0.2
------

* Added `UnexpectedCertificateError` as an error type returned when SSL Pinning is working on an unreliable network.
* Fixed issue where the device was not registering for Push Notifications immediately after linking a device until the next time the application was recreated.
* Fixed issue with `AuthRequestFragment` not reacting properly for failing responses to pending Auth Requests.
* Fixed issue with the fingerprint scanner on select Samsung devices due to an OS firmware bug.
* Improved logic and navigation when a device is automatically unlinked upon 10 failed attempts when verifying knowledge factors.
* Improved Javadocs.

v3.0.1
------

* The White Label SDK is now the Authenticator SDK
 - `WhiteLabelConfig` now `AuthenticatorConfig`
 - `WhiteLabelManager` now `AuthenticatorManager`
* Tweaked UI
* Defined better managers: `AuthenticatorManager`, `DeviceManager`, `SessionManager`, `AuthRequestManager`
* Added event propagation via each manager
* Allowed theming support via XML themes
* Performance overhaul
* Improved definition of errors for better handling

Reference docs for more changes between v2.x and v3.

v2.3.2
------
* Added
 * New implementation `WhiteLabelManager.AccountStateListener2` that will take over the now deprecated `WhiteLabelManager.AccountStateListener`. Continue calling `WhiteLabelManager.add/removeAccountStateListener` to register and unregister for those events. The difference with the initial version is that `AccountStateListener2.onAuthenticationFailure(…)` now returns a `BaseError` object to deal with errors generated when responding to an Auth Request.
 * `ExpiredAuthRequestError` will now be returned in `AccountStateListener2.onAuthenticationFailure(…)` to allow the integrator to react to that event and notify the end user of the expired/invalid Auth Request with a custom user interface.
 * General improvements.

* Removed
 * Toast served by the WL SDK notifying the end user of an expired/old pending Auth Request immediately after responding to one. (Reference the second bullet point in “Added” section above)

* Fixed
 * Fixed Javadocs entries for various methods in `WhiteLabelManager`.

v2.3.1
------
* Added
 * Support for push notifications sent to the White Label device that is unlinked remotely by notifying the usual `onUnlink()` callback.
 * General improvements.

* Fixed
 * Issue #15: `AuthRequestFragment` will now have the SDK call `onUnlink()` when the device is unlinked by an API response when checking for a pending Auth Request.

v2.3.0
------

* Added
 - Improvements (issue #7) to the generation of the Key Pair when linking the device. `WhiteLabelConfig.Builder` now accepts a custom size for the key pair, and the key is generated when `WhiteLabelManager` is initialized with such `WhiteLabelConfig` object in the background instead of when attempting to link.
 - Improvements to the Auth Request UI and verbiage when more factors were required by the Application.
 - Improvements to the linking code and device name validators.
 - Updates to Javadocs with changes in this v2.3.0 release.

* Fixed
 - Issue #10: `AuthRequestFragment` now automatically continues with the authentication once the user sets more factors if the Auth Request requires it.

v2.2.1
------

* Added
 - Improvements to the handling of remote unlinking of the device in use.
 - Improvements to how the current device is unlinked when done so in the list of linked devices.
 - Improvements to `AuthRequestFragment`'s included toolbar; now hidden by default and changed via `AuthRequestFragment.setToolbarVisible(boolean)`.

* Fixed
 - Issue #6: Crash due to lack of a null-check in `AuthRequestFragment`.

* Deprecated
 - `WhiteLabelManager.hasActiveSessions()` in favor of listeners when the status is updated `WhiteLabelManager.add/removeStatusListener(WhiteLabelManager.SessionListener)`.

v2.2.0
------

* New WhiteLabelConfig.Builder constructor
* Better error handling
 - Error objects covering most scenarios
 - New callbacks for manager objects returning error objects and success flags
* Added SecurityService allowing implementers to get information in the current status on set Security Factors
* Bug fixes and enhancements

v2.1.0
------

* Added support for custom fonts via WhiteLabelConfig.Builder
* Added support for custom device name with default UI and custom registerUser(...) method
* Added support for unlinking other devices under the WL username
* Added support for custom endpoint
* Added support for custom SSL cert (when SSL Pinning is required for a custom endpoint)
* Added proper contrast for branding (when present)
* Bug fixes and enhancements

v2.0.0
------

* Major UI overhaul with Material Design
* Added default embeddable Fragments for responding to Auth Requests, list of Authorizations, Logs, Devices, and T-OTPs
* New exposed methods for custom UI of Authorizations, Logs, Devices, T-OTPs
* New White Label configurator
* Improved theming options

v1.0.3
------

* Bug fixes or enhancements
