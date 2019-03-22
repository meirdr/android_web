package spreo.web;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.JsonArray;
import com.spreo.geofence.GeoFenceObject;
import com.spreo.geofence.ZoneDetection;
import com.spreo.interfaces.MyLocationListener;
import com.spreo.nav.enums.LocationMode;
import com.spreo.nav.interfaces.ILocation;
import com.spreo.sdk.geofencing.GeoFencingUtils;
import com.spreo.sdk.location.SpreoLocationProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyLocationListener, ZoneDetection {

    WebView myWebView;
    private static final String OCCUPANCY_ZONE_NAME = "proximity";
    private String url = "https://demo.spreo.co/citrix/"; //file:///android_asset/test.htm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpreoLocationProvider.getInstance().subscribeForLocation(this);
        List<String> list = new ArrayList<>();
        list.add(OCCUPANCY_ZONE_NAME);
        GeoFencingUtils.subscribeToService(this, list);

        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient());

        myWebView.loadUrl(url);

        myWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);
                return false;
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLocationDelivered(ILocation loc) {
        String json = getAsJson(loc);
        if (json != null) {
            callJavaScript("onLocationUpdated", json);
        }
    }

    @Override
    public void onCampusRegionEntrance(String s) {

    }

    @Override
    public void onFacilityRegionEntrance(String s, String s1) {

    }

    @Override
    public void onFacilityRegionExit(String s, String s1) {

    }

    @Override
    public void onFloorChange(String s, String s1, int i) {

    }

    @Override
    public void onLocationModeChange(LocationMode locationMode) {

    }

    @Override
    public void onZoneEnter(GeoFenceObject geoFenceObject) {
        String json = getAsJson(geoFenceObject);
        if (json != null) {
            callJavaScript("onZoneEnter", json);
        }
    }

    @Override
    public void onZoneExit(GeoFenceObject geoFenceObject) {
        String json = getAsJson(geoFenceObject);
        if (json != null) {
            callJavaScript("onZoneExit", json);
        }
    }

    @Override
    public List<String> getListeningTo() {
        return null;
    }

    @Override
    public void setListeningTo(List<String> list) {

    }

    private void callJavaScript(String methodName, Object...params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("javascript:try{");
        stringBuilder.append(methodName);
        stringBuilder.append("(");
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof String) {
                stringBuilder.append("'");
                stringBuilder.append(param.toString().replace("'", "\\'"));
                stringBuilder.append("'");
            }
            if (i < params.length - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(")}catch(error){Android.onError(error.message);}");
        myWebView.loadUrl(stringBuilder.toString());
    }

    public String getAsJson(ILocation loc) {
        JSONObject jsonObj = new JSONObject();

        try {
            try {
                jsonObj.put("type", loc.getLocationType());
            } catch (Throwable var10) {
                var10.printStackTrace();
            }

            try {
                jsonObj.put("campusid", loc.getCampusId());
            } catch (Throwable var9) {
                var9.printStackTrace();
            }

            try {
                jsonObj.put("facilityid", loc.getFacilityId());
            } catch (Throwable var8) {
                var8.printStackTrace();
            }

            try {
                jsonObj.put("x", loc.getX());
            } catch (Throwable var7) {
                var7.printStackTrace();
            }

            try {
                jsonObj.put("y", loc.getY());
            } catch (Throwable var6) {
                var6.printStackTrace();
            }

            try {
                jsonObj.put("z", loc.getZ());
            } catch (Throwable var5) {
                var5.printStackTrace();
            }

            try {
                jsonObj.put("lat", loc.getLat());
            } catch (Throwable var4) {
                var4.printStackTrace();
            }

            try {
                jsonObj.put("lon", loc.getLon());
            } catch (Throwable var3) {
                var3.printStackTrace();
            }

            JSONArray array = new JSONArray();
            JSONObject sample = new JSONObject();
            sample.put("sample key", "sample value");
            array.put(sample);

            jsonObj.put("extra params", array);
        } catch (Throwable var11) {
            var11.printStackTrace();
        }

        return jsonObj.toString();
    }

    public String getAsJson(GeoFenceObject geoFenceObject) {
        JSONObject jsonObj = new JSONObject();

        try {
            try {
                jsonObj.put("id", geoFenceObject.getId());
            } catch (Throwable var10) {
                var10.printStackTrace();
            }

            try {
                jsonObj.put("name", geoFenceObject.getName());
            } catch (Throwable var9) {
                var9.printStackTrace();
            }


            JSONArray array = new JSONArray();
            JSONObject sample = new JSONObject();
            sample.put("sample key", "sample value");
            array.put(sample);

            jsonObj.put("extra params", array);
        } catch (Throwable var11) {
            var11.printStackTrace();
        }

        return jsonObj.toString();
    }

//    private class MyWebViewClient extends WebViewClient {
//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            view.loadUrl(request.getUrl().toString());
//            return false;
//        }
//    }
}
