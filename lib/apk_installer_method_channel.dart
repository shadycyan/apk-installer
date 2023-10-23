import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'apk_installer_platform_interface.dart';

/// An implementation of [ApkInstallerPlatform] that uses method channels.
class MethodChannelApkInstaller extends ApkInstallerPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('apk_installer');

  @override
  Future<void> installApk({required String filePath}) async {
    await methodChannel.invokeMethod('installApk', {'filePath': filePath});
  }
}
