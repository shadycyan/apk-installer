
import 'apk_installer_platform_interface.dart';

class ApkInstaller {
  Future<String?> getPlatformVersion() {
    return ApkInstallerPlatform.instance.getPlatformVersion();
  }
}
