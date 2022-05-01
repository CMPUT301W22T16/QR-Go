# QR Go

**An Android app that allows you to hunt for the coolest QR codes that score the most points**

> We want a mobile application that allows us to hunt for the coolest QR codes that score the most points. Players will run around scanning QR codes, barcodes, etc. trying to find barcodes and QR codes that give them the most points. QR codes and barcodes (scannable codes) will be hashed and the hashes they produce will be analyzed and scored. A QR code that has certain properties like repeated nibbles or bytes (hex digits) will have a higher score than a QR code that does not. We have a proposed scoring system, but the implementers are free to use a different scoring system. We want users to compete with each other for the highest scoring QR codes, the most QR codes, the highest sum of QR codes, or highest scoring QR codes in a region.  When a player scans a QR code they will take a photo of what or where the QR code is and also record the geolocation of the QR code. Players can see on a map local QR codes that other players have scanned.

> I open my QRHunter app. I see a QR code in my wallet. I indicate I want to add a new QR code and I use the phone camera to add the QR code. The QR code is scored and I’m told that my QR score is 30. The system prompts me for a photo of the object I scanned. I decline since this was an ID card. I also decline geolocation because it is in my wallet. The system adds the 30 points to my total score and records a hash of the QR code. I then see some sticker on a pole. I scan it and am told it is worth 1000 points! I record the geolocation and take a photo of the pole and save it to my account. 1000 points wow. Then I see that other users have found this pole as well. So I open the map for nearby QR codes and I see something worth 10000 is 100 meters away so I’m going to head on over there!
[View full project description](https://github.com/CMPUT301W22T16/QR-Go/wiki)

## Project mark
**Excellent (A+)** - _A excellent submission that meets the all the requirements without problem._

## Links
### UI Design Mockup
- [Go to Figma design](https://www.figma.com/file/p4YQpmAkAw2VkmGpXoKw7v/QR-GO-UI-Final-Draft?node-id=0%3A1)
- [UI Design Wiki Docs](https://github.com/CMPUT301W22T16/QR-Go/wiki/UI-Design)
### UML Diagram
- [UML Diagram](https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/diagrams/QRGo_UML.png)
- [CRC Cards](https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/diagrams/CRC%20Cards.pdf)
### Product Backlog
- [Github projects board](https://github.com/CMPUT301W22T16/QR-Go/projects/2)
- [Github Issues](https://github.com/CMPUT301W22T16/QR-Go/issues?q=)

### Github pages site
- https://cmput301w22t16.github.io/QR-Go displays everything in the [gh-pages](https://github.com/CMPUT301W22T16/QR-Go/tree/gh-pages) branch (aka the `build` folder)
- [Javadocs](https://cmput301w22t16.github.io/QR-Go/outputs/javadoc/index.html)
- [Debug Unit Tests Report](https://cmput301w22t16.github.io/QR-Go/reports/tests/testDebugUnitTest/index.html)
- [Release Unit Tests Report](https://cmput301w22t16.github.io/QR-Go/reports/tests/testReleaseUnitTest/index.html)
- [Lint Report](https://cmput301w22t16.github.io/QR-Go/reports/lint-results-debug.html)

## Android App Screenshots
[View screenshot files](https://github.com/CMPUT301W22T16/QR-Go/tree/main/doc/screenshots)
### Map of QR codes
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/map.png" width="300">

### Scanning a new QR code
<div>
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/scan_qr_code.png" width="300">
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/confirm_qr_code.png" width="300">
</div>

## Viewing my QR Codes
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/my_qr_codes.png" width="300">

## Searching for QR codes
<div>
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/search_qr_code_by_proximity.png" width="300">
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/search_qr_code_by_score.png" width="300">
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/search_regional_qr_codes.png" width="300">
</div>

## Searching for players
<div>
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/search_player_by_num_codes.png" width="300">
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/search_player_by_total_score.png" width="300">
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/search_player_by_unique_score.png" width="300">
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/search_player_by_username.png" width="300">
</div>

## QR code info
<div>
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/qr_info.png" width="300">
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/scanner_users_list.png" width="300">
</div>

## Player profile
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/player_profile.png" width="300">

## My Account
<div>
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/edit_my_profile.png" width="300">
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/edit_profile_validation.png" width="300">
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/generate_game_qr.png" width="300">
<img src="https://github.com/CMPUT301W22T16/QR-Go/blob/main/doc/screenshots/generate_login_qr.png" width="300">
</div>


Demo video
[![Alternate Text]({image-url})]({https://user-images.githubusercontent.com/56571687/166137119-40f479ec-f6d9-4a3c-9d2d-e338c67a140e.mp4} "Link Title")



