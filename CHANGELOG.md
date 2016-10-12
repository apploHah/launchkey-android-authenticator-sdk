Change Log
==========

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
