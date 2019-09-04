package com.ms.karorkefz.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ms.karorkefz.BuildConfig;
import com.ms.karorkefz.adapter.PreferenceAdapter;
import com.ms.karorkefz.util.Config;
import com.ms.karorkefz.util.Constant;
import com.ms.karorkefz.util.DpUtil;
import com.ms.karorkefz.util.Log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.PixelFormat.TRANSPARENT;
import static com.ms.karorkefz.util.Constant.Setting_list;

public class SettingsView extends DialogFrameLayout implements AdapterView.OnItemClickListener {
    private List<PreferenceAdapter.Data> mSettingsDataList = new ArrayList<>();
    private PreferenceAdapter mListAdapter;
    private ListView mListView;

    public SettingsView(Context context) {
        super( context );

        LogUtil.e( "karorkefz", "进入设置页" );
        init( context );
    }

    private void init(Context context) {
        LinearLayout rootVerticalLayout = new LinearLayout( context );
        rootVerticalLayout.setOrientation( LinearLayout.VERTICAL );

        View lineView = new View( context );
        lineView.setBackgroundColor( TRANSPARENT );

        TextView settingsTitle = new TextView( context );
        settingsTitle.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 18 );
        settingsTitle.setText( "全民K歌辅助  " + BuildConfig.VERSION_NAME );
        settingsTitle.setTextColor( Color.WHITE );
        settingsTitle.setTypeface( null, Typeface.BOLD );
        settingsTitle.setBackgroundColor( 0xFF1AAEE5 );
        int defHPadding = DpUtil.dip2px( context, 15 );
        int defVPadding = DpUtil.dip2px( context, 12 );
        settingsTitle.setPadding( defHPadding, defVPadding, defHPadding, defVPadding );

        mListView = new ListView( context );
        mListView.setDividerHeight( 0 );
        mListView.setOnItemClickListener( this );
        mListView.setPadding( defHPadding, 0, 2, 0 );
        mListView.setDivider( new ColorDrawable( Color.TRANSPARENT ) );
        Config config = new Config( context );
        for (int i = 0; i < Setting_list.size(); i++) {
            Constant.Setting setting = (Constant.Setting) Setting_list.get( i );
            mSettingsDataList.add( new PreferenceAdapter.Data( setting.title, setting.subTitle, setting.key, true, config.isOn( setting.key ) ) );
        }
        mListAdapter = new PreferenceAdapter( mSettingsDataList );
        rootVerticalLayout.addView( settingsTitle );
        rootVerticalLayout.addView( lineView, new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dip2px( context, 2 ) ) );
        rootVerticalLayout.addView( mListView );
        this.addView( rootVerticalLayout );

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mListView.setAdapter( mListAdapter );
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final Context context = getContext();
        final Config config = new Config( context );
        PreferenceAdapter.Data data = mListAdapter.getItem( position );
        data.selectionState = !data.selectionState;
        config.setOn( data.key, data.selectionState );
        mListAdapter.notifyDataSetChanged();
        
    }
}
