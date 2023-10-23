import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'apk_installer_method_channel.dart';

abstract class ApkInstallerPlatform extends PlatformInterface {
  /// Constructs a ApkInstallerPlatform.
  ApkInstallerPlatform() : super(token: _token);

  static final Object _token = Object();

  static ApkInstallerPlatform _instance = MethodChannelApkInstaller();

  /// The default instance of [ApkInstallerPlatform] to use.
  ///
  /// Defaults to [MethodChannelApkInstaller].
  static ApkInstallerPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [ApkInstallerPlatform] when
  /// they register themselves.
  static set instance(ApkInstallerPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
