package spreo.web;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mlins.res.setup.ConfigsUpdater;
import com.spreo.enums.ResUpdateStatus;
import com.spreo.interfaces.ConfigsUpdaterListener;
import com.spreo.sdk.data.SpreoResourceConfigsUtils;
import com.spreo.sdk.location.SpreoLocationProvider;

public class SpreoUpdateView extends LinearLayout implements ConfigsUpdaterListener {

    private final Context ctx;
    private RelativeLayout pl;
    private SpreoUpdateListener updateListener = null;

    public SpreoUpdateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        ctx = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.spreo_update_view, this, true);

        pl = (RelativeLayout) findViewById(R.id.loading_indicator);

    }

    public void update(String key) {
        if (key != null) {
            ConfigsUpdater.getInstance().setReqApikey(key);
            SpreoResourceConfigsUtils.subscribeToResourceUpdateService(this);
            SpreoResourceConfigsUtils.update(ctx);
        }
    }

    @Override
    public void onPreConfigsDownload() {
        if (pl != null) {
            pl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPostConfigsDownload(ResUpdateStatus resUpdateStatus) {
        if (resUpdateStatus.equals(ResUpdateStatus.OK)) {
            SpreoResourceConfigsUtils.unSubscribeFromResourceUpdateService(this);

            if (pl != null) {
                pl.setVisibility(View.GONE);
            }
            notifyUpdateFinished();
        } else {
            if (pl != null) {
                pl.setVisibility(View.GONE);
            }
        }

    }

    private void notifyUpdateFinished() {
        try {
            if (updateListener != null) {
                updateListener.OnUpdateFinished();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void onPreConfigsInit() {

    }

    @Override
    public void onPostConfigsInit(ResUpdateStatus resUpdateStatus) {

    }

    public void setUpdateListener(SpreoUpdateListener updateListener) {
        this.updateListener = updateListener;
    }
}
