import 'apk_installer_platform_interface.dart';

class ApkInstaller {
  static Future<void> installApk({required String filePath}) {
    return ApkInstallerPlatform.instance.installApk(filePath: filePath);
  }
}
