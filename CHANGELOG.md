Change Log
==========

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
