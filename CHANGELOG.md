Change Log
==========

v4.7.0
------

* Added
  * Auth Method Insights in encrypted response to implementer. **Note:** Requires a version of the implemented Service SDK that supports this feature in order to parse and provide that data.
  * Support for a configurable Auth Failure threshold via `AuthenticatorConfig.Builder#thresholdAuthFailure(...)`.
  * Support for configurable Auto-Unlink thresholds via `AuthenticatorConfig.Builder#thresholdAutoUnlink(...)`.
  * New code under `ApiError` when attempting to retrieve (or respond to) an Auth Request canceled by the implementer.
  * New messaging shown to the end user in the failure view if they attempted to respond to a request that has been canceled by the implementer.
  * Minor improvements.

* Updated
  * Tweaked Auth Request UI.
  * `AuthenticatorConfig` returned by the SDK will not return the SDK key if already initialized.
  * Optimizations to PIN Code view layout inflation ([#9](https://github.com/iovation/launchkey-android-authenticator-sdk/issues/9)).

* Fixed
  * Bug having SDK rely on an invalid cached API time after reboots.
  * Bug preventing the SDK from re-generating a key when unlinked in order to link the authenticator once again ([#37](https://github.com/iovation/launchkey-android-authenticator-sdk/issues/37)).
  * Bug displaying a confirmation dialog twice when adding a geo-fence for the first time.
  * Bug resulting in a crash on apps targeting 27 and up due to a transparent `Activity` when using the default linking views ([#32](https://github.com/iovation/launchkey-android-authenticator-sdk/issues/32)).
  * Few minor bugs.

4.6.0
-----

* Added
  * A new UI and UX for Auth Requests. 
  * Support for Auth Denial Context/Options shown to the end user if Directory supports it. Note: This functionality can be customizable as an implementer if using an updated Service SDK.
  * Support for new Auth Request response classifications to better differentiate failures, denials, and authorizations, with specific sub-types. Note: This functionality requires an implementer to use an updated Service SDK to parse the new type of response data sent back. The old type of response will stay as is in plain boolean.
  *  New theme properties for new UI elements in the new Auth Request UI (`authResponseButton`, `authResponseButtonNegative`, `expirationTimer`, `authContentViewBackground`, `authResponseAuthorizedColor`, `authResponseDeniedColor`, `authResponseFailedColor`, `denialReasons`)
  * Support for retrieving a specific Auth Request by ID provided by push notification payloads.
  * Support for implementers to have parent app explicitly allow or disallow the use of specific Security Factors/Auth Methods via `AuthenticatorConfig.Builder#allowAuthMethod(...)`.
* Fixed
  * Minor issues happening internally in the SDK.
  * Bug resulting in a crash when the app is idle and is attempting to send metrics back.
  * Bug resulting in a crash when `AuthRequestFragment` is still shown after using Fingerprint Scan (on supported devices) and the end user interacts with a timeout dialog after using the fingerprint sensor.
  * Lack of parity among iOS and Android Auth SDKs when it comes to preference and verification order of Security Factors/Auth Methods.
* Deprecated
  * The `AuthorizationSlider` view and its XML/Java theme properties. The slider will no longer be shown with the new UI/UX.
  * Few methods in `AuthRequest` and `ServiceProfile` classes.

v4.5.1
-----

* Added
  * Support for data centers.
  * Support for 3rd-party notifications via `AuthenticatorManager#onPushNotificationPackage(String)`
  * New methods facilitating providing payload when handled by the LaunchKey platform via `AuthenticatorManager#onPushNotification(Map<String, String>)` and `AuthenticatorManager#onPushNotification(Bundle bundle)`
* Fixed
  * Bug resulting in missing pending Auth Requests due to subsequent checks against a different data centers due to DNS.
* Deprecated
  * `AuthenticatorManager#onPushNotification(Bundle, String)`

v4.4.1
------
* Fixed
  * UI glitches around activation delays.
  * Issues with checks around Local Auth Requests.
  * Spacing around several views in the SDK.
  * Issue preventing end users to add a new auth method if the dialog is brought up too soon.
  * UI issue when resuming an expired request.
  * Issue with Snackbars overlapping views below `SecurityFragment` if embedded.
  * Issue with the navigation leading back to an auth method view even if automatically unlinked.
  * Bug resulting in a crash if specific conditions were presented around internal metrics and unlink events.
  * Minor internal bugs.
  
* Added
  * Support for dynamic theming that can be provided at runtime. Note: XML is still supported and those values are converted over to the new `AuthenticatorTheme.Builder` object that can be passed to the `AuthenticatorConfig.Builder` like the static reference of XMLs and the properties will be applied to the relevant views at runtime.
  * More theme properties that can be customized.
  
* Updated
  * Messaging.
  * Layout of a few Security screens for consistency.

v4.3.1
------
* Fixed
  * Issue with a few values that are part of the internal metrics sent back.

v4.3.0
------
* Fixed
  * Issue with the UI not updating the amount of factors verified during an Auth Request.
* Added
  * Support for internal metrics. Implementing apps can call the new `AuthenticatorManager#sendMetrics(...)` method to deliver them on demand.

v4.2.1
------
* Fixed
  * Issue with the UI not updating during verification of Geofencing as a factor affecting v4.2.0.
  * Smaller issues with UI elements affecting v4.2.0.
* Added
  * Improvements around the update of push notification tokens.

v4.2.0
------
* Updated
  * Auth SDK to allow for use of concurrent knowledge factors (both PIN Code and Circle Code can be used concurrently)

* Added
  * `LocalAuthManager` to allow for Local Auth Requests
	- Implementors can build a `Policy` object either by type (knowledge, inherence, and/or possession), or by count (required number of active factors) via `PolicyFactory`.
	- Implementors can pass the built `Policy` object to `LocalAuthManager#authenticateUser(Policy)` in order to generate a Local Auth Request.
  * Support for implementors to let End Users set factors even when unlinked.
  * Protection against making changes in the Security view when there is a pending request.

* Fixed
  * Issue with the camera view (in default linking view) not restarting after failing to scan a valid QR code or End User backing out of providing a name for a device after scanning a QR code.
  * Issue displaying "Enabled factors" header in the Security view when there are no Security factors set.
  * Various UI issues around alignment, style, and theming.
  * Inconsistent crash when canceling a Fingerprint Scan dialog.
  * Issue notifying implementing application the SDK is done working on the key generated preluding the linking step.
  * Issue prolonging the location check of the device during a request.
  * Issue with default linking view not showing the manual entry of linking codes when permissions to the camera were denied.

v4.1.0
------
* Updated
  * Overall messaging
  * Javadocs
  * Few dialogs to support keyboard's "done" action when an input is involved.
  * Fingerprint scanner handler with improved support.

* Added
  * Support for OpenStreetMap (OSM) for Android when Google Maps dependencies are not found. Note: If using Google Maps, remember to provide a valid Google Maps key via `AndroidManifest.xml`. You may also be subject to Google's own terms of services when using their Google Maps service(s).
  * Support for device-only Location Services when Google Play Services is not present. 

* Fixed
  * Issue with fingerprint sensor not working if `AuthRequestFragment` is shown at all times.
  * Lack of support for TLS 1.2 on older versions of Android defaulting to 1.0 and 1.1.
  * Bugs around permissions and map views.
  * UI glitches.

v4.0.0
------
* Updated
  * Overall messaging
  * Networking functionality
  * Javadocs

* Added
  * Added `DeviceKeyPairGeneratedEventCallback` as a way to know when the Authenticator SDK has finished generating the key used to link a new device.
  * Added `updatePushNotificationToken(...)` to `AuthenticatorManager` in order to notify the Authenticator SDK when the device token for Push Notifications changes.
  * Added `onPushNotification(...)` to `AuthenticatorManager` in order to handle incoming push notifications received by the implementing application.
  * Added new style properties `authenticatorColorNegativeWidgetText` and `authenticatorColorNegativeWidgetBackground` to override the default color of widgets (and text if it has any) related to negative actions (Unlinking in default Devices view, removing a factor, etc.).

* Removed
  * Internal classes to receive push notifications from GCM. Note: Must be handled by the implementing app and sent back to the Auth SDK via `updatePushNotificationToken(...)` and `onPushNotification(...)` mentioned above.
  * `AuthenticatorApplication` class. It is not needed anymore and if inherited, subclass the standard Android class `Application` instead.
  * T-OTPs/Codes.
  * `endpoint(...)` method in `AuthenticatorConfig.Builder` to override the API endpoint. It is NOT an option anymore.
  * `sslCert(...)` method in `AuthenticatorConfig.Builder` to set the SSL cert for overridden endpoints when `sslPinning(true)` was called along with a custom endpoint via `endpoint(...)` which has also been removed.

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
