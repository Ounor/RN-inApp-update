import android.app.Activity;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;


public class RNInAppUpdateModule extends ReactContextBaseJavaModule {

    public RNInAppUpdateModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNInAppUpdateModule";
    }

    @ReactMethod
    public void getUpdate(int updateType, Callback readyToUpdate) {
        final Activity activity = getCurrentActivity();

        if (activity == null) {
            return;
        }

        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(activity);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        InstallStateUpdatedListener listener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                readyToUpdate.invoke();
                appUpdateManager.completeUpdate();
            }
        };

        // Before starting an update, register a listener for updates.
        appUpdateManager.registerListener(listener);

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(updateType)) {
                // Request the update.
                try {
                appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateType,
                        activity,
                        100
                );
            } catch (android.content.IntentSender.SendIntentException e) {
                System.out.println(e.getMessage());
            }
            }
        });
    }
}
