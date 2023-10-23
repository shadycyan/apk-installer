package com.shadycyan.apk_installer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import java.io.File


/** ApkInstallerPlugin */
class ApkInstallerPlugin : FlutterPlugin, MethodCallHandler, ActivityAware,
		PluginRegistry.RequestPermissionsResultListener {
		/// The MethodChannel that will the communication between Flutter and native Android
		///
		/// This local reference serves to register the plugin with the Flutter Engine and unregister it
		/// when the Flutter Engine is detached from the Activity
		private lateinit var channel: MethodChannel
		private lateinit var apkFilePath: String
		private lateinit var context: Context
		private var activity: Activity? = null

		companion object {
				private const val INSTALL_REQUEST_CODE = 9876
		}

		override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
				channel = MethodChannel(flutterPluginBinding.binaryMessenger, "apk_installer")
				channel.setMethodCallHandler(this)
				context = flutterPluginBinding.applicationContext
		}

		override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
				channel.setMethodCallHandler(null)
		}

		override fun onMethodCall(call: MethodCall, result: Result) {
				when (call.method) {
						"installApk" -> {
								val filePath = call.argument<String>("filePath")
										?: return result.error(
												"INVALID_ARGUMENT",
												"File path is null",
												null
										)

								apkFilePath = filePath

								if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O &&
										Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
								) {
										requestMarshmallowPermission()
								} else {
										installApk()
								}

								result.success(0)
						}

						else -> result.notImplemented()
				}
		}

		@SuppressLint("SetWorldReadable")
		private fun installApk() {
				try {
						val cacheDir = context.applicationContext.cacheDir
						val apkFile = File(apkFilePath)
						val cachedApkFile = File(cacheDir, apkFile.name).apply {
								writeBytes(apkFile.readBytes())
								setReadable(true, false)
						}

						val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
								FileProvider.getUriForFile(
										context,
										"${context.packageName}.provider",
										cachedApkFile
								)
						} else {
								Uri.fromFile(cachedApkFile)
						}

						val installIntent = Intent(Intent.ACTION_VIEW).apply {
								setDataAndType(uri, "application/vnd.android.package-archive")
								addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
								addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
								putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, context.packageName)
						}

						context.startActivity(installIntent)
				} catch (e: Exception) {
						val tagField = FlutterActivity::class.java.getDeclaredField("TAG")
						tagField.isAccessible = true
						val tag = tagField.get(null) as String
						Log.e(tag, "Install APK Exception", e)
				}
		}

		@RequiresApi(Build.VERSION_CODES.M)
		private fun requestMarshmallowPermission() {
				activity?.requestPermissions(
						arrayOf(Manifest.permission.INSTALL_PACKAGES),
						INSTALL_REQUEST_CODE
				)
		}

		override fun onRequestPermissionsResult(
				requestCode: Int,
				permissions: Array<out String>,
				grantResults: IntArray
		): Boolean {
				when (requestCode) {
						INSTALL_REQUEST_CODE -> {
								if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
										installApk()
								}
						}
				}
				return true
		}

		override fun onAttachedToActivity(binding: ActivityPluginBinding) {
				activity = binding.activity
				binding.addRequestPermissionsResultListener(this)
		}

		override fun onDetachedFromActivityForConfigChanges() {
				activity = null
		}

		override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
				activity = binding.activity
				binding.addRequestPermissionsResultListener(this)
		}

		override fun onDetachedFromActivity() {
				activity = null
		}
}

