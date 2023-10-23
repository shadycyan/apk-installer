import 'dart:io';

import 'package:apk_installer/apk_installer.dart';
import 'package:flutter/material.dart';

final _apkFile = File('path/to/your/package.apk');

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

Future<void> _installApk() async {
  await ApkInstaller.installApk(filePath: _apkFile.path);
}
