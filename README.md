# RN-inApp-update

1) В android/app/build.gradle добавить "implementation 'com.google.android.play:core:1.6.5'"
2) В корневом компоненте импортировать NativeModules и в didMount вызвать NativeModules.RNInAppUpdateModule.getUpdate(update_type, callback)
где update_type 0/1 что соответсвует FLEXIBLE/IMMEDIATE способу обновления
