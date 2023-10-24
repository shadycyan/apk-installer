# apk_installer

A Flutter plugin for easy Android APK installation, compatible with **Android API 19** and higher. 



## Usage
To use this plugin, add `apk_installer` as a [dependency in your pubspec.yaml file](https://flutter.dev/docs/development/platform-integration/platform-channels).

### Example
```dart
import 'package:flutter/material.dart';
import 'package:apk_installer/apk_installer.dart';

Future<void> _installApk() async {
  await ApkInstaller.installApk(filePath: 'path/to/your/package.apk');
}

void main() {
  runApp(
    const MaterialApp(
      home: Material(
        child: Center(
          child: ElevatedButton(
            onPressed: _installApk,
            child: Text('Install APK'),
          ),
        ),
      ),
    ),
  );
}
```

## License
This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.
