package com.sjw.beautifulapp.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.luoshihai.xxdialog.DialogViewHolder;
import com.luoshihai.xxdialog.XXDialog;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.adapter.MyFragmentPagerAdapter;
import com.sjw.beautifulapp.application.AppApplication;
import com.sjw.beautifulapp.base.BaseData;
import com.sjw.beautifulapp.base.BaseFragmentActivity;
import com.sjw.beautifulapp.base.BaseSharedPreferences;
import com.sjw.beautifulapp.fragment.EpisodeFragment;
import com.sjw.beautifulapp.fragment.FoodFragment;
import com.sjw.beautifulapp.fragment.GirlFragment;
import com.sjw.beautifulapp.fragment.Index2Fragment;
import com.sjw.beautifulapp.fragment.IndexFragment;
import com.sjw.beautifulapp.newtools.baidulocation.LocationService;
import com.sjw.beautifulapp.newtools.permission.PermissionSetting;
import com.sjw.beautifulapp.utils.ActivityListUtil;
import com.sjw.beautifulapp.utils.CustomToast;
import com.sjw.beautifulapp.utils._loadingDialog;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.header_love)
    ImageView headerLove;
    @BindView(R.id.update_location)
    TextView updateLocation;
    @BindView(R.id.current_location)
    TextView currentLocation;
    @BindView(R.id.des_location)
    TextView desLocation;
    private Context context = MainActivity.this;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tab)
    PageNavigationView tab;
    NavigationController mNavigationController;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.header_iv)
    ImageView headerIv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private List<Fragment> fragmentList;

    //andpermission
    private Rationale mRationale;
    private PermissionSetting mSetting;
    private static final int INSTALL_PACKAGES_REQUESTCODE = 111;
    private static final int GET_UNKNOWN_APP_SOURCES = 222;
    // 定义一个变量，来标识是否退出
    private long exitTime = 0;

    private PopupWindow mPopupWindow;


    private LocationService locationService;

    @Override
    protected void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = ((AppApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK

    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void judgeNet() {

    }

    @Override
    protected void setLayoutId() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData() {
//        CrashReport.testJavaCrash();

        //设置友盟各个功能
        setUmengTools();
//        https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1555680419,979539261&fm=26&gp=0.jpg

        //初始化图像和名字
        boolean islogin= (boolean) BaseSharedPreferences.get(BaseData.BD_IS_LOGIN,false);


//
        seticonName(islogin);

        fragmentList = new ArrayList<>();
        fragmentList.add(new Index2Fragment());
        fragmentList.add(new GirlFragment());
        fragmentList.add(new EpisodeFragment());
        fragmentList.add(new FoodFragment());

        mNavigationController = tab.material()
                .addItem(R.drawable.main_index, "首页")
                .addItem(R.drawable.main_girl, "美女")
                .addItem(R.drawable.main_episode, "段子")
                .addItem(R.drawable.main_food, "美食")
                .build();


        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));

        mNavigationController.setupWithViewPager(viewPager);
//        checkIsAndroidO();
        requestPermission();


    }

    private void setUmengTools() {
        //统计
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(true);
        /**
         * 设置日志加密
         * 参数：boolean 默认为false（不加密）
         */
        UMConfigure.setEncryptEnabled(true);

        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
        // 将默认Session间隔时长改为40秒。
        MobclickAgent.setSessionContinueMillis(1000);

        //推送
        PushAgent.getInstance(this).onAppStart();
    }


    /**
     * 判断是否是8.0系统,是的话需要获取此权限，判断开没开，没开的话处理未知应用来源权限问题,否则直接安装
     */
    private void checkIsAndroidO() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                CustomToast.showCustomImgToast("欢迎光临");
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
            }
        } else {
            CustomToast.showCustomImgToast("欢迎光临");
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case INSTALL_PACKAGES_REQUESTCODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CustomToast.showCustomImgToast("欢迎光临");
                } else {
                    XXDialog xxDialog = new XXDialog(this, R.layout.dialog_customze) {
                        @Override
                        public void convert(DialogViewHolder holder) {
                            holder.setOnClick(R.id.dialog_sure, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                                    startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);
                                    dismiss();
                                }
                            });
                            holder.setText(R.id.dialog_msg, "请设置允许安装未知应用");
                            holder.getView(R.id.dialog_cancel).setVisibility(View.GONE);
                            holder.setText(R.id.dialog_sure_tv, "去设置");

                        }
                    }.fromBottomToMiddle()
                            .showDialog().setCanceledOnTouchOutside(false)
                            .setCancelAble(false);


                }
                break;

        }
    }


    @Override
    protected void setListener() {
        headerIv.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    toolbar.setVisibility(View.VISIBLE);

                } else {

                    toolbar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //在这里处理item的点击事件
                if (item.getItemId() == R.id.drawer_pay) {

                    startActivity(PayActivity.class, false);


                }else if (item.getItemId()==R.id.drawer_location){

                    startActivity(LocationActivity.class, false);

                }else if (item.getItemId()==R.id.drawer_video){

                    XXDialog xxDialog = new XXDialog(MainActivity.this, R.layout.dialog_sp) {
                        @Override
                        public void convert(DialogViewHolder holder) {
                            holder.setOnClick(R.id.localvideo_tv, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, LoveActivity.class);
                                    intent.putExtra("type", "2");
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                                    dismiss();
                                }
                            });
                            holder.setOnClick(R.id.netvideo_tv, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, NetvideoActivity.class);

                                    startActivity(intent);
                                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                                    dismiss();
                                }
                            });

                        }
                    }.fromBottomToMiddle()
                            .showDialog().setCanceledOnTouchOutside(true)
                            .setCancelAble(true);;

                }else if(item.getItemId()==R.id.drawer_share){
                    share();
                }else if(item.getItemId()==R.id.drawer_login){
                    thirdLogin();
                }else if(item.getItemId()==R.id.drawer_loginout){
                    loginOut();
                }



                return true;
            }
        });




    }

    @Override
    protected void beforeUnbinder() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_iv:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }


    //请求权限
    private void requestPermission() {


        AndPermission.with(context)
                .permission(Permission.READ_PHONE_STATE, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE, Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION)
//                     .rationale(mRationale)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
//                            checkIsAndroidO();
                        CustomToast.showCustomImgToast("欢迎光临");
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {

                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            mSetting.showSetting(permissions);
                        }
                    }
                }).start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void exit() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {

            CustomToast.showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
//            finish();
            //            System.exit(0);
            ActivityListUtil.getInstance().exit();

        }

    }


    private void popSelectLove() {
        View contentView = getPopupContentView();
// 创建PopupWindow时候指定高宽时showAsDropDown能够自适应(能够根据剩余空间自动选中向上向下弹出)
        // 如果设置为wrap_content,showAsDropDown会认为下面空间一直很充足（我以认为这个Google的bug）
        // 备注如果PopupWindow里面有ListView,ScrollView时，一定要动态设置PopupWindow的大小
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        // popupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        // popupWindow.showAsDropDown(mButton1);  // 默认在mButton1的左下角显示
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xOffset = contentView.getMeasuredWidth();
        mPopupWindow.showAsDropDown(headerLove, xOffset, 10);    // 在mButton1的中间显示

    }

    private View getPopupContentView() {

        int layoutId = R.layout.pop_love;
        View contentView = LayoutInflater.from(this).inflate(layoutId, null);

        View.OnClickListener menuOnclickListener = new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.menu_item1:
                        Intent intent1 = new Intent(context, LoveActivity.class);
                        intent1.putExtra("type", "1");
                        startActivity(intent1);
                        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                        break;
                    case R.id.menu_item2:
                        Intent intent2 = new Intent(context, LoveActivity.class);
                        intent2.putExtra("type", "2");
                        startActivity(intent2);
                        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                        break;
                    case R.id.menu_item3:
                        Intent intent3 = new Intent(context, LoveActivity.class);
                        intent3.putExtra("type", "3");
                        startActivity(intent3);
                        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                        break;

                    default:

                        break;

                }


                if (mPopupWindow != null) {


                    mPopupWindow.dismiss();
                }

            }
        };


        contentView.findViewById(R.id.menu_item1).setOnClickListener(menuOnclickListener);
        contentView.findViewById(R.id.menu_item2).setOnClickListener(menuOnclickListener);
        contentView.findViewById(R.id.menu_item3).setOnClickListener(menuOnclickListener);

        return contentView;


    }


    @OnClick({R.id.update_location, R.id.header_love})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update_location:
                locationService.start();
                break;
            case R.id.header_love:
                //弹出一个popwindow
                popSelectLove();


            break;
        }
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息

                currentLocation.setText(location.getAddrStr());
                desLocation.setText(location.getLocationDescribe());
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }

            } else {

                currentLocation.setText("定位不成功");
            }
        }

    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_UNKNOWN_APP_SOURCES:
                checkIsAndroidO();
                break;

            default:
                break;
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    //友盟第三方登陆
    public void thirdLogin() {
        UMShareAPI.get(this).getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

                Log.e("thirdLogin", share_media + "");
                _loadingDialog.submitLoading(context,"登陆中...");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.e("thirdLogin", share_media + "");
//                Toast.makeText(MainActivity.this, map + "", Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                "name" -> "宇宙星辰老祖"
//                "iconurl" -> "http://thirdqq.qlogo.cn/qqapp/1107839525/D00D4D5C4E9A04135F5F8EE38C6E7DF5/100"
                String iconurl=map.get("iconurl");
                String name=map.get("name");
                BaseSharedPreferences.put(BaseData.BD_ICON,iconurl);
                BaseSharedPreferences.put(BaseData.BD_NAME,name);
                BaseSharedPreferences.put(BaseData.BD_IS_LOGIN,true);
                seticonName(true);


               _loadingDialog.dismissLoading();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.e("thirdLogin", throwable.getMessage() + "");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.e("thirdLogin", share_media + "");
            }
        });


    }

    private void seticonName(boolean islogin) {
        String iconurl;
        String name;
        if (!islogin){
            iconurl="https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1555680419,979539261&fm=26&gp=0.jpg";
            name="老祖";
        }else {

            iconurl= (String) BaseSharedPreferences.get(BaseData.BD_ICON,"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1555680419,979539261&fm=26&gp=0.jpg");
            name=(String) BaseSharedPreferences.get(BaseData.BD_NAME,"老祖");
        }
        Glide.with(context).load(iconurl).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(headerIv);
//修改headerlayout中的内容
        View headerView = navigationView.getHeaderView(0);
        TextView textview = (TextView)headerView.findViewById(R.id.name);
        textview.setText(name);
        ImageView iv=(ImageView)headerView.findViewById(R.id.avatar);
        Glide.with(context).load(iconurl).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv);
    }


    //友盟分享
    public void share() {
        UMImage image = new UMImage(MainActivity.this, R.mipmap.ic_launcher);//网络图片
        UMImage thumb=new UMImage(this,R.mipmap.ic_launcher);
        image.setThumb(image);
        image.setTitle("好图片");
        image.setDescription("好图片");
        thumb.setTitle("好图片");
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享


        image.compressFormat = Bitmap.CompressFormat.PNG;//用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色


        UMWeb web=new UMWeb("https://www.baidu.com");
        web.setTitle("天秀应用");
        web.setThumb(image);
        web.setDescription("天秀应用，秀出强大");
        web.mText="展会高大上" ;


        new ShareAction(MainActivity.this)
                .withText("好图片")
                .withSubject("xixi")

                .withMedia(web)
                .setDisplayList( SHARE_MEDIA.QZONE,SHARE_MEDIA.QQ)
                .setCallback(shareListener).open();
    }
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };


//友盟退出登录
    public void loginOut() {

        UMShareAPI.get(this).deleteOauth(MainActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.e("thirdLogin", share_media + "");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                Log.e("thirdLogin", share_media + "");
                Toast.makeText(MainActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();

                BaseSharedPreferences.put(BaseData.BD_ICON,"");
                BaseSharedPreferences.put(BaseData.BD_NAME,"");
                BaseSharedPreferences.put(BaseData.BD_IS_LOGIN,false);
                seticonName(false);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.e("thirdLogin", throwable.getMessage() + "");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.e("thirdLogin", share_media + "");

            }
        });
    }
}
