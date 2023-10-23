import 'package:flutter_test/flutter_test.dart';
import 'package:apk_installer/apk_installer.dart';
import 'package:apk_installer/apk_installer_platform_interface.dart';
import 'package:apk_installer/apk_installer_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockApkInstallerPlatform
    with MockPlatformInterfaceMixin
    implements ApkInstallerPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final ApkInstallerPlatform initialPlatform = ApkInstallerPlatform.instance;

  test('$MethodChannelApkInstaller is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelApkInstaller>());
  });

  test('getPlatformVersion', () async {
    ApkInstaller apkInstallerPlugin = ApkInstaller();
    MockApkInstallerPlatform fakePlatform = MockApkInstallerPlatform();
    ApkInstallerPlatform.instance = fakePlatform;

    expect(await apkInstallerPlugin.getPlatformVersion(), '42');
  });
}
