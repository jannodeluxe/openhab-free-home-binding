# OpenHAB 3 ABB/Busch-free@home Smart Home binding
 OpenHAB ABB/Busch-free@home binding based on the offical free@home local api
 
![alt text](https://github.com/jannodeluxe/jannnnoooo/blob/main/free_at_home_logo_1.jpg)
![alt text](https://github.com/jannodeluxe/jannnnoooo/blob/main/abb_freeathome_2_0.png)
# Description
This openhab binding allows you to connect your free@home Smart Home system from ABB / Busch-Jaeger to OpenHAB and to control and observe most of the components. 
It requires a System Access Point with version 2.3.1 or higher.

# Features
* Control your ABB/Busch-free@home system with its Lights, Outlets, Blinds, etc. from OpenHAB via local API.

# Supported devices
- ABB / Busch-Jaeger System Access Point 2.0
- Auxilary devices:
-- dsds

# Tested Versions
|Version|Supported|Notes|
|---|---|---|
|2.6.1|:clock9:|under testing|


# Requirements
* ....
* ....

# Setup / Installation

## Prerequisites
To make use of this Binding first the local free@home API has to be activated. The API is disabled by default!
1. Open the free@home next app
2. Browse to "Settings -> free@home settings -> local API and activate the checkbox

![alt text](https://github.com/jannodeluxe/jannnnoooo/blob/main/freeathome-settingsapi.PNG)


## Installing the binding
As this binding is not in the official release of openhab, you will not find it in the "bindings" section.
To use this binding please do the following steps:
1. Download the latest jar file "here"
2. Upload the jar file to the user directory of your device running openhab
 1. for openhabian e.g: /usr/share/openhab/addons
 2. for others: /etc/openhab/addons/
3. reboot the device with     `sudo reboot`

## Setup
1. Enter your openHAB webfrontend with     `<device IP>:8080`
2. Log into openHAB with your crendetials at the lower left side
3. Browse to "Settings -> Things" and press the "+" symbol
4. Choose "FreeAtHome System Binding" and click "Free@home Bridge"
5. Add the required data: SysAP IP adress, username and password

**ATTENTION:** The username here has to be from "Settings -> free@home settings -> local API, NOT the username from webfrontend or used in the app for login)

6. Press save in the righter upper corner
7. If everthing is right the Bridge should went "Online"
8. "Scan" for the free@home devices and set them up

# Communities
[Busch-Jaeger Community](https://community.busch-jaeger.de/)

[free@home user group Facebook DE](https://www.facebook.com/groups/738242583015188)

[free@home user group Facebook EN](https://www.facebook.com/groups/452502972031360)

# Known Issues
lore ipsum


# Changelog
The changelog can be viewed [here](CHANGELOG.md).


# Upgrade Notes
Upgrade Notes can be found in the [CHANGELOG](CHANGELOG.md).


# Help
If you have any questions or help please open an issue on the GitHub project page.


# Contributing
Pull requests are always welcome.


# Donation
If you find my work useful you can support the ongoing development of this project by buying me a [cup of coffee] tbd


# License
The project is subject to the MIT license unless otherwise noted. A copy can be found in the root directory of the project [LICENSE](LICENSE).


# Disclaimer
This API is a private contribution and not related to ABB or Busch-Jaeger. It may not work with future updates of the free@home firmware and can also cause unintended behavior. Use at your own risk!

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
