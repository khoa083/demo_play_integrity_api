Done Sample client (refactor)<br>
JDK 23<br>
Update CI/CD (Fastlane, github actions)

## PlayIntegrity:
- in order to decrypt the integrity verdict locally on the device or the server you have to manage the corresponding decryption and verification keys by yourself. This is only possible if you have a paid Google Developer Account and registered the app bundle in the [Google Play Console](https://play.google.com/console/about/). Navigate to **Release** -> **Setup** -> **AppIntegrity** -> **Response encryption**, click on **Change** and choose **Manage and download my response encryption keys**. Follow the instructions to create a private-public key pair in order to download the encrypted keys.
- add the keys to `local.properties` as `base64_of_encoded_decryption_key=...` and `base64_of_encoded_verification_key=...`

Final local.properties file should look like this:
```
base64_of_encoded_decryption_key = YOUR DECRYPTION KEY
base64_of_encoded_verification_key = YOUR VERIFICATION KEY
```

## KeyStore 
- add the keys to `local.properties`

```
RELEASE_STORE_FILE = ... 
RELEASE_STORE_PASSWORD = ...
RELEASE_KEY_ALIAS = ...
RELEASE_KEY_PASSWORD = ...
```

# License
MIT License

```
Copyright (c) 2023 khoa083 (kblack)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
