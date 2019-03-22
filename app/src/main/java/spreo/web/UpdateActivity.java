package spreo.web;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.spreo.sdk.location.SpreoLocationProvider;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity implements SpreoUpdateListener {

    private SpreoUpdateView updateView;
    String key = "b611b8e117a4407c9451904ec57a335c15290942269581425797419";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        updateView = (SpreoUpdateView) findViewById(R.id.updateView);
        updateView.setUpdateListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkRequiredPermission();
        } else {
            startUpdate();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkRequiredPermission() {
        String requiredpermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (!hasPermission(requiredpermission)) {
            requestPermissions(new String[]{requiredpermission}, 1);
        } else {
            checkOptionalPermissions();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private Boolean hasPermission(String requiredpermission) {
        boolean hasPermission = (checkSelfPermission(requiredpermission) == PackageManager.PERMISSION_GRANTED);
        return hasPermission;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkOptionalPermissions() {

        List<String> optionalPermissions = new ArrayList<String>();

        optionalPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        optionalPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        optionalPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        optionalPermissions.add(Manifest.permission.CAMERA);

        List<String> forRequest = new ArrayList<String>();
        for (String o : optionalPermissions) {
            boolean hasPermission = (checkSelfPermission(o) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                forRequest.add(o);
            }
        }

        if (forRequest.isEmpty()) {
            startUpdate();
        } else {
            String arr[] = new String[forRequest.size()];
            String[] permissions = (String[]) forRequest.toArray(arr);
            requestPermissions(permissions, 2);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkOptionalPermissions();
            }
        } else {
            startUpdate();
        }
    }

    private void startUpdate() {
        if (key != null) {
            updateView.update(key);
        }
    }

    @Override
    public void OnUpdateFinished() {
        SpreoLocationProvider.getInstance().startLocationService(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
