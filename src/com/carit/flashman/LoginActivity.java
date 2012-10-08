
package com.carit.flashman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.offlinemap.MOfflineMapStatus;
import com.amap.mapapi.offlinemap.OfflineMapManager;
import com.amap.mapapi.offlinemap.OfflineMapManager.OfflineMapDownloadListener;
import com.carit.flashman.util.Common;

public class LoginActivity extends MapActivity implements OfflineMapDownloadListener {
    private static final String TAG = "LoginActivity";

    private Spinner mHotCityspinner;

    private Spinner mProvincespinner;

    private Spinner mCityspinner;

    private ArrayAdapter<String> adapter;

    private ArrayAdapter<CharSequence> mCityAdpter;

    private String[] mHotCitys;

    private int[] mArrayIds = {
            R.array.AnHuiSheng, R.array.FuJianSheng, R.array.GanSuSheng, R.array.GangAo,
            R.array.GuangDongSheng, R.array.GuangXiSheng, R.array.GuiZhouSheng,
            R.array.HaiNanSheng, R.array.HeBeiSheng, R.array.HeNanSheng, R.array.HeiLongJiang,
            R.array.HuBeiSheng, R.array.HuNanSheng, R.array.JiLinSheng, R.array.JiangSuSheng,
            R.array.JiangXiSheng, R.array.LiaoNingSheng, R.array.NeiMengGu, R.array.NingXia,
            R.array.QingHaiSheng, R.array.ShanDongSheng, R.array.ShanXiSheng, R.array.ShaanXiSheng,
            R.array.SiChuanSheng, R.array.XiZang, R.array.XinJiang, R.array.YunNanSheng,
            R.array.ZheJiangSheng
    };

    private boolean mCityInit;

    private OfflineMapManager mOffline = null;

    private String mProvince;

    private String mCity;

    private String mDownload;
    
    private boolean mIsSuccess;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.arg1 == MOfflineMapStatus.LOADING) {
                        Log.e(TAG, "loading = " + msg.arg2);
                        ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar1);
                        progress.setProgress(msg.arg2);
                        if(mIsSuccess&&msg.arg2==0){
                            if (mDownload != null) {
                                Common.init(getBaseContext(),mDownload);
                            } else {
                                Common.init(getBaseContext(),mCity);
                            }
                            startActivity(new Intent(getBaseContext(),MainActivity.class));
                            finish();
                        }
                            
                    } else if (msg.arg1 == MOfflineMapStatus.SUCCESS) {
                        Log.e(TAG, "success");
                        Toast.makeText(getBaseContext(), getString(R.string.download_success),
                                Toast.LENGTH_LONG).show();
                        mIsSuccess = true;
                    } else if (msg.arg1 == MOfflineMapStatus.UNZIP) {
                        Log.e(TAG, "unzip");
                        Toast.makeText(getBaseContext(), getString(R.string.unzip),
                                Toast.LENGTH_LONG).show();
                        mIsSuccess = true;
                    } else {
                        Toast.makeText(getBaseContext(), "" + msg.arg1, Toast.LENGTH_LONG).show();
                    }
                    break;
                case 1:
                    startActivity(new Intent(getBaseContext(),MainActivity.class));
                    finish();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle arg0) {
        SharedPreferences info = getSharedPreferences("Info", 0);
        setContentView(R.layout.login);
        mOffline = new OfflineMapManager(this, this);
        boolean init = info.getBoolean("Init", false);

        if (!init) {
            // Editor editor = track.edit();
            // editor.putBoolean("track", true);
            // editor.putBoolean("reboot_track", true);
            // editor.commit();
            findViewById(R.id.data_download).setVisibility(View.VISIBLE);
            mHotCityspinner = (Spinner) findViewById(R.id.hot_citys);
            mHotCitys = getResources().getStringArray(R.array.Hot_Citys);
            // 将可选内容与ArrayAdapter连接起来
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    mHotCitys);

            // 设置下拉列表的风格
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // 将adapter 添加到spinner中
            mHotCityspinner.setAdapter(adapter);

            // 添加事件Spinner事件监听
            mHotCityspinner.setOnItemSelectedListener(new SpinnerSelectedListener());

            // 设置默认值
            mHotCityspinner.setVisibility(View.VISIBLE);

           

            Button btn = (Button) findViewById(R.id.download);
            btn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
                    String city = null;
                    if (mDownload != null) {
                        city=mDownload;
                    } else {
                        city=mCity;
                    }
                    mOffline.remove(city);
                    mOffline.downloadByCityName(city);
                    //init(city);
                }
            });
            btn = (Button) findViewById(R.id.skip);
            btn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(getBaseContext(),MainActivity.class));
                    finish();
                }
            });
        }else{
            mHandler.sendEmptyMessageDelayed(1, 2000);
        }
        /*
         * ToggleButton ctl = (ToggleButton) findViewById(R.id.download_ctl);
         * ctl.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         * @Override public void onCheckedChanged(CompoundButton buttonView,
         * boolean isChecked) { if(isChecked){ Log.e(TAG, "download pause");
         * mOffline.pause(); }else{ Log.e(TAG, "download restart");
         * mOffline.restart(); } } });
         */

        super.onCreate(arg0);
    }

    // 使用数组形式操作
    class SpinnerSelectedListener implements OnItemSelectedListener {

        @SuppressWarnings("unchecked")
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (mHotCityspinner.equals(arg0)) {
                if (mHotCitys.length == (arg2 + 1)) {
                    mDownload = null;
                    findViewById(R.id.other_city).setVisibility(View.VISIBLE);
                    if (!mCityInit) {
                        mProvincespinner = (Spinner) findViewById(R.id.province);
                        // 将可选内容与ArrayAdapter连接起来
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                                LoginActivity.this, R.array.provinces,
                                android.R.layout.simple_spinner_item);
                        // 设置下拉列表的风格
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // 将adapter 添加到spinner中
                        mProvincespinner.setAdapter(adapter);

                        // 添加事件Spinner事件监听
                        mProvincespinner.setOnItemSelectedListener(new SpinnerSelectedListener());
                        ;
                        mCityspinner = (Spinner) findViewById(R.id.city);
                        mCityAdpter = ArrayAdapter.createFromResource(LoginActivity.this,
                                R.array.AnHuiSheng, android.R.layout.simple_spinner_item);
                        mCityspinner.setAdapter(adapter);
                        // 设置下拉列表的风格
                        mCityAdpter
                                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // 添加事件Spinner事件监听
                        mCityspinner.setOnItemSelectedListener(new SpinnerSelectedListener());
                        mCityInit = true;

                    }

                    return;
                } else {
                    findViewById(R.id.other_city).setVisibility(View.GONE);
                    if (arg1 != null) {
                        mDownload = ((TextView) arg1).getText().toString();
                    }
                }
            } else if (mProvincespinner != null && mProvincespinner.equals(arg0)) {
                mCityspinner.setAdapter(ArrayAdapter.createFromResource(LoginActivity.this,
                        mArrayIds[arg2], android.R.layout.simple_spinner_item));
                ((ArrayAdapter<CharSequence>) (mCityspinner.getAdapter()))
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if (arg1 != null) {
                    mProvince = ((TextView) arg1).getText().toString();
                }
            } else if (mCityspinner != null && mCityspinner.equals(arg0)) {
                // mCity = arg1.toString();
                if (arg1 != null) {
                    mCity = ((TextView) arg1).getText().toString();
                    Toast.makeText(getBaseContext(), mCity, Toast.LENGTH_LONG).show();
                }
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    @Override
    public void onDownload(int arg0, int arg1) {
        // TODO Auto-generated method stub
        mHandler.obtainMessage(0, arg0, arg1).sendToTarget();
    }

   
}
