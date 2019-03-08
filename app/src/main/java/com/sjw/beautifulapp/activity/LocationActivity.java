package com.sjw.beautifulapp.activity;


import android.content.Context;
import android.content.pm.ActivityInfo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;


import com.baidu.trace.Trace;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.Point;
import com.baidu.trace.model.PushMessage;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.base.BaseActivity;
import com.sjw.beautifulapp.newtools.baidulocation.indoorview.BaseStripAdapter;
import com.sjw.beautifulapp.newtools.baidulocation.indoorview.StripListView;
;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;


import com.baidu.trace.LBSTraceClient;

public class LocationActivity extends BaseActivity {


//    @BindView(R.id.back)
//    ImageView back;
//    @BindView(R.id.title)
//    TextView title;
    private Button back;


    @BindView(R.id.bmapView)
    MapView bmapView;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    MapView mMapView;
    BaiduMap mBaiduMap;

    StripListView stripListView;
    BaseStripAdapter mFloorListAdapter;
    MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;
    static Context mContext;
    // UI相关

    Button requestLocButton;
    boolean isFirstLoc = true; // 是否首次定位

    private Polyline mPolyline;
    private LatLng target;
    private ArrayList<LatLng>  latLngs;
    private String[] pointsArr={
            "30.52099,120.903131",
            "31.52099,121.903131"
    };
    //鹰眼服务ID
    long serviceId = 202340;
    //entity标识
    String entityName = "sjw";
    //是否返回精简的结果（0 : 将只返回经纬度，1 : 将返回经纬度及其他属性信息）
    int simpleReturn = 0;
    //开始时间（Unix时间戳）
    int startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
    //结束时间（Unix时间戳）
    int endTime = (int) (System.currentTimeMillis() / 1000);
    //分页大小
    int pageSize = 1000;
    //分页索引
    int pageIndex = 1;


    // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
    boolean isNeedObjectStorage = false;
    // 初始化轨迹服务
    Trace mTrace ;
    // 初始化轨迹服务客户端
    LBSTraceClient mTraceClient;
    // 定位周期(单位:秒)
    int gatherInterval = 5;
    // 打包回传周期(单位:秒)
    int packInterval = 10;

    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                 queryHistoryTrack(0);
                    break;

                default:
                    break;
            }
        };
    };




    @Override
    protected void initView() {

    }

    @Override
    protected void judgeNet() {

    }

    @Override
    protected void setLayoutId() {
        setContentView(R.layout.activity_location);
    }

    @Override
    protected void initData() {
        latLngs=new ArrayList<>();
        mTraceClient= new LBSTraceClient(this);
        mTrace=new  Trace(serviceId, entityName, isNeedObjectStorage);
        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RelativeLayout layout = new RelativeLayout(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainview = inflater.inflate(R.layout.activity_location, null);
        layout.addView(mainview);

        back=(Button) mainview.findViewById(R.id.button2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        back.setText("返回");

        requestLocButton = (Button) mainview.findViewById(R.id.button1);
        requestLocButton.setVisibility(View.VISIBLE);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        requestLocButton.setText("普通");
        View.OnClickListener btnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (mCurrentMode) {
                    case NORMAL:
                        requestLocButton.setText("跟随");
                        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true,
                                mCurrentMarker));
                        break;
                    case COMPASS:
                        requestLocButton.setText("普通");
                        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true,
                                mCurrentMarker));
                        break;
                    case FOLLOWING:
                        requestLocButton.setText("罗盘");
                        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true,
                                mCurrentMarker));
                        break;
                    default:
                        break;
                }
            }
        };
        requestLocButton.setOnClickListener(btnClickListener);

        // 地图初始化
        mMapView = (MapView) mainview.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 开启室内图
        mBaiduMap.setIndoorEnable(true);

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

//// 地图初始化
//        MapView mMapView = (MapView) findViewById(R.id.bmapView);
//        BaiduMap mBaiduMap = mMapView.getMap();
//// 开启定位图层
//        mBaiduMap.setMyLocationEnabled(true);

////获取运动后的定位点
//        coordinateConvert();

//设置缩放中点LatLng target，和缩放比例
//        MapStatus.Builder builder = new MapStatus.Builder();
//        builder.target(target).zoom(18f);






//        // 设置定位和打包周期
//        mTraceClient.setInterval(gatherInterval, packInterval);
//// 开启服务
//        mTraceClient.startTrace(mTrace, mTraceListener);
//// 开启采集
//        mTraceClient.startGather(mTraceListener);
////地图设置缩放状态
//        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//
///**
// * 配置线段图层参数类： PolylineOptions
// * ooPolyline.width(13)：线宽
// * ooPolyline.color(0xAAFF0000)：线条颜色红色
// * ooPolyline.points(latLngs)：List<LatLng> latLngs位置点，将相邻点与点连成线就成了轨迹了
// */
//        OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(latLngs);
//
//
////在地图上画出线条图层，mPolyline：线条图层
//        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
//        mPolyline.setZIndex(2);


        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);
        //允许使用gps定位
        option.setOpenGps(true);
        mLocClient.setLocOption(option);
        mLocClient.start();

        stripListView = new StripListView(this);
        layout.addView(stripListView);
        setContentView(layout);
        mFloorListAdapter = new BaseStripAdapter(LocationActivity.this);

        mTraceClient.startTrace(mTrace, mTraceListener);//开启服务
//        mTraceClient.startGather(mTraceListener);//开启采集

        //第一种方法(1秒之后开始，间隔5秒)
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);

            }
        }, 1000,5000);




        mBaiduMap.setOnBaseIndoorMapListener(new BaiduMap.OnBaseIndoorMapListener() {
            @Override
            public void onBaseIndoorMapMode(boolean b, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
                if (b == false || mapBaseIndoorMapInfo == null) {
                    stripListView.setVisibility(View.INVISIBLE);

                    return;
                }

                mFloorListAdapter.setmFloorList(mapBaseIndoorMapInfo.getFloors());
                stripListView.setVisibility(View.VISIBLE);
                stripListView.setStripAdapter(mFloorListAdapter);
                mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo;
            }
        });






    }



    /**
     * 我这里是在google地图取下来的wgs84坐标集合Const.googleWGS84，模拟的运动后获取的坐标  集合，
     所以需要转化成百度坐标；实际应该是将定位sdk返回的位置点加入到位置集合中，
     定位sdk需要设置返回坐标为百度坐标：mOption.setCoorType("bd09ll")，这样就直接用，不用转换 了。
     */
    private void  coordinateConvert(){
//百度坐标转化工具类CoordinateConverter
        CoordinateConverter converter  = new CoordinateConverter();

        /**
         * 设置需要转化的坐标类型
         CoordType.COMMON：google地图、腾讯地图、高德地图所用坐标
         CoordType.GPS：设备采集的原始GPS坐标
         */
        converter.from(CoordinateConverter.CoordType.COMMON);

        double lanSum = 0;
        double lonSum = 0;
        for (int i = 0; i < pointsArr.length; i++) {
//"39.881970,116.456218"
            String[] ll = pointsArr[i].split(",");
            LatLng sourceLatLng = new LatLng(Double.valueOf(ll[0]), Double.valueOf(ll[1]));


//            converter.coord(sourceLatLng);  //需要转化的坐标点
//            LatLng desLatLng = converter.convert();  //转化成百度坐标点
            LatLng desLatLng=sourceLatLng;
            if (i==0){
                target=desLatLng;
            }
            latLngs.add(desLatLng);//加入定位点集合
            lanSum += desLatLng.latitude;
            lonSum += desLatLng.longitude;
        }

//我这里设置地图的缩放中心点为所有点的几何中心点
        target = new LatLng(lanSum/latLngs.size(), lonSum/latLngs.size());
    }



    @Override
    protected void setListener() {

    }

    @Override
    protected void beforeUnbinder() {

    }



    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner extends BDAbstractLocationListener {

        private String lastFloor = null;

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            String bid = location.getBuildingID();
            if (bid != null && mMapBaseIndoorMapInfo != null) {
                Log.i("indoor", "bid = " + bid + " mid = " + mMapBaseIndoorMapInfo.getID());
                if (bid.equals(mMapBaseIndoorMapInfo.getID())) {// 校验是否满足室内定位模式开启条件
                    // Log.i("indoor","bid = mMapBaseIndoorMapInfo.getID()");
                    String floor = location.getFloor().toUpperCase();// 楼层
                    Log.i("indoor", "floor = " + floor + " position = " + mFloorListAdapter.getPosition(floor));
                    Log.i("indoor", "radius = " + location.getRadius() + " type = " + location.getNetworkLocationType());

                    boolean needUpdateFloor = true;
                    if (lastFloor == null) {
                        lastFloor = floor;
                    } else {
                        if (lastFloor.equals(floor)) {
                            needUpdateFloor = false;
                        } else {
                            lastFloor = floor;
                        }
                    }
                    if (needUpdateFloor) {// 切换楼层
                        mBaiduMap.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo.getID());
                        mFloorListAdapter.setSelectedPostion(mFloorListAdapter.getPosition(floor));
                        mFloorListAdapter.notifyDataSetInvalidated();
                    }

                    if (!location.isIndoorLocMode()) {
                        mLocClient.startIndoorMode();// 开启室内定位模式，只有支持室内定位功能的定位SDK版本才能调用该接口
                        Log.i("indoor", "start indoormod");
                    }
                }
            }

            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        if (handler!=null){

            handler.removeMessages(1);
            handler=null;
        }
        super.onDestroy();
    }


    // 初始化轨迹服务监听器
    OnTraceListener mTraceListener =new OnTraceListener() {
        @Override
        public void onBindServiceCallback(int i, String s) {

            Log.e("mTraceListener",s);

        }

        @Override
        public void onStartTraceCallback(int i, String s) {
            Log.e("mTraceListener",s);

            mTraceClient.startGather(mTraceListener);//开启采集
        }

        @Override
        public void onStopTraceCallback(int i, String s) {
            Log.e("mTraceListener",s);
        }

        @Override
        public void onStartGatherCallback(int i, String s) {
            Log.e("mTraceListener",s);
            queryHistoryTrack(1);
        }

        @Override
        public void onStopGatherCallback(int i, String s) {
            Log.e("mTraceListener",s);
        }

        @Override
        public void onPushCallback(byte b, PushMessage pushMessage) {
            Log.e("mTraceListener",pushMessage.toString());
        }

        @Override
        public void onInitBOSCallback(int i, String s) {
            Log.e("mTraceListener",s);
        }
    };


    private void queryHistoryTrack(int i) {

        // 结束时间
        long endTime;//初始值（可省）
        long start;//初始值（可省）
        endTime =(System.currentTimeMillis() / 1000);//毫秒
        start = System.currentTimeMillis() / 1000 -  60 * 60;//毫秒

        System.out.println("开始：" + start + ",结束：" + endTime);
//         查询历史轨迹

        HistoryTrackRequest htr = new HistoryTrackRequest(1, serviceId,
                entityName);

        htr.setServiceId(serviceId);//设置serviceId
        htr.setProcessed(false);// 是否纠偏
        htr.setEntityName(entityName); // entity标识
        htr.setPageIndex(1);// 分页索引

        htr.setPageSize(10);// 分页大小
        htr.setStartTime(start);// 开始时间
        htr.setEndTime(endTime);// 结束时间

        mTraceClient.queryHistoryTrack(htr, new OnTrackListener() {

            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                // TODO Auto-generated method stub
                super.onHistoryTrackCallback(response);
                System.out.println("数量：" + response.getSize());
                if (response.getSize() > 0) {//如果当前日期范围内有数据点，则准备绘制
                    List<TrackPoint> tp = response.getTrackPoints();// 所有点的坐标信息数据集

                    Point startPoint = response.getStartPoint();// 起点的坐标信息
                    Point endPoint = response.getEndPoint();// 终点的坐标信息

                    drawHistoryTrack(tp, startPoint, endPoint);// 绘制折线
                }

            }

        });
    }


    private void drawHistoryTrack(List<TrackPoint> list_tp, Point startPoint,
                                  Point endPoint) {

        showMarker(startPoint, 0);//调用showMarker（）函数标记起点
        showMarker(endPoint, 1);//调用showMarker（）函数标记终点
        List<LatLng> points = new ArrayList<LatLng>();
        //遍历轨迹坐标，装载到List<LatLng>
        for (int i = 0; i < list_tp.size(); i++) {
            points.add(new LatLng(list_tp.get(i).getLocation().getLatitude(),
                    list_tp.get(i).getLocation().getLongitude()));

        }

        // 构造对象 设置折线的宽度和颜色以及points
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        // 添加到地图
        mBaiduMap.addOverlay(ooPolyline);


    }


    private void showMarker(Point point, int i) {

        // 定义Maker坐标点
        LatLng latlng = new LatLng(point.getLocation().getLatitude(), point
                .getLocation().getLongitude());
        // 构建Marker图标
        BitmapDescriptor bitmap;
        if (i == 0) {
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.qidian);
        } else if (i == 1) {

            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.zhongdian);
        } else {
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.zhongdian);
        }

        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(latlng).icon(
                bitmap);
        // 在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }





}
