import 'package:apk_installer/apk_installer.dart';
import 'package:flutter/material.dart';

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
