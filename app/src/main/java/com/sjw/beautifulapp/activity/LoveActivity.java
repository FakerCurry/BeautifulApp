package com.sjw.beautifulapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.adapter.LoveAdapter;
import com.sjw.beautifulapp.base.BaseActivity;
import com.sjw.beautifulapp.base.BaseData;
import com.sjw.beautifulapp.bean.LoveFileBean;
import com.sjw.beautifulapp.greendaoopr.DaoManager;
import com.sjw.beautifulapp.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoveActivity extends BaseActivity implements LoveAdapter.SetOnCheckBack {


    @BindView(R.id.love_top_righttv)
    TextView loveTopRighttv;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private Context context = LoveActivity.this;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.love_rv)
    RecyclerView loveRv;
    @BindView(R.id.love_addLove)
    ImageView loveAddLove;
    private Intent getintent;
    private String type;
    private List<LocalMedia> selectList;

    private List<LoveFileBean> loveFileBeanList;

    private LoveAdapter adapter;
    private Snackbar snackbar;
    private Button snackbar_right_btn;

    private TextView snackbar_love_selectnum;
    private LinearLayout snackbar_love_selectll;


    @Override
    protected void initView() {

    }

    @Override
    protected void judgeNet() {

    }

    @Override
    protected void setLayoutId() {

        setContentView(R.layout.activity_love);
    }

    @Override
    protected void initData() {
        getintent = getIntent();
        loveFileBeanList = new ArrayList<>();
        selectList = new ArrayList<>();
        type = getintent.getStringExtra("type");
        //从数据库中获取数据
        optionDatabase();
        if (type.equals("1")) {

            title.setText("喜欢的图片");

        } else if (type.equals("2")) {
            title.setText("喜欢的视频");

        } else if (type.equals("3")) {

            title.setText("喜欢的音频");
        }
        loveRv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new LoveAdapter(loveFileBeanList, context, type);
        adapter.setOncheckBackClickListener(this);
        loveRv.setAdapter(adapter);
        if (loveFileBeanList.size()>0){

            fab.setVisibility(View.VISIBLE);
            loveRv.setVisibility(View.VISIBLE);
            loveAddLove.setVisibility(View.GONE);

        }else {

            fab.setVisibility(View.GONE);
            loveRv.setVisibility(View.GONE);
            loveAddLove.setVisibility(View.VISIBLE);
        }



    }

    private void optionDatabase() {
        List<LoveFileBean> fileBeans=new ArrayList<>();
        fileBeans=DaoManager.getInstance().searchByType(type);
        loveFileBeanList=fileBeans;

        selectList=getLocalmedialist(loveFileBeanList);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void beforeUnbinder() {

    }

    @Override
    public void finish() {
        //记录在数据库中
        //先删除数据库中所有数据(type为当前的)
        List<LoveFileBean> allFileList=new ArrayList<>();
        allFileList=DaoManager.getInstance().searchAllFile();
        if (allFileList.size()>0){
            for (LoveFileBean bean:allFileList){
                if (bean.getIsType().equals(type)){

                    DaoManager.getInstance().deleteByKeyFile(bean);
                }


            }

        }



        //再将所有的数据存在数据库中
        for (LoveFileBean bean:loveFileBeanList){

            bean.setEdit(false);
            bean.setSelect(false);
            bean.setChecked(false);

            DaoManager.getInstance().insertFile(bean);
        }


        super.finish();

        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out);
    }

    @OnClick({R.id.back, R.id.love_addLove,R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.love_addLove:
                if (type.equals("1")) {

                    addLovePic();

                } else if (type.equals("2")) {
                    addLoveVideo();

                } else if (type.equals("3")) {

                    addLoveAudio();
                }

                break;
            case R.id.fab:
                if (type.equals("1")) {

                    addLovePic();

                } else if (type.equals("2")) {
                    addLoveVideo();

                } else if (type.equals("3")) {

                    addLoveAudio();
                }

                break;
        }
    }

    private void addLoveAudio() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(LoveActivity.this)
                .openGallery(PictureMimeType.ofAudio())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(9)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(2)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .isCamera(false)// 是否显示拍照按钮 true or false
                .selectionMedia(selectList)// 是否传入已选图片
                .forResult(BaseData.BD_SELECT_AUDIO);//结果回调onActivityResult code
    }

    private void addLoveVideo() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(LoveActivity.this)
                .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(9)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(2)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .isCamera(false)// 是否显示拍照按钮 true or false
                .selectionMedia(selectList)// 是否传入已选图片
                .forResult(BaseData.BD_SELECT_VIDEO);//结果回调onActivityResult code
    }

    private void addLovePic() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(LoveActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(9)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(2)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .isCamera(false)// 是否显示拍照按钮 true or false
                .selectionMedia(selectList)// 是否传入已选图片
                .forResult(BaseData.BD_SELECT_PIC);//结果回调onActivityResult code
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BaseData.BD_SELECT_PIC:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    rtOption();


                    break;
                case BaseData.BD_SELECT_VIDEO:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);

                    rtOption();

                    break;
                case BaseData.BD_SELECT_AUDIO:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);

                    rtOption();

                    break;
            }
        }
    }

    private void rtOption() {

        loveTopRighttv.setText("编辑");
        if (snackbar!=null){

            snackbar.dismiss();
        }
        // 例如 LocalMedia 里面返回三种path
        // 1.media.getPath(); 为原图path
        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
        // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
        List<LoveFileBean> beanpics = getLovefilelist(selectList);
        List<LoveFileBean> newPics = getSelectrsList(beanpics);


        loveFileBeanList=newPics;

        //刷新adapter
        adapter.updateAdapter(loveFileBeanList);
        if (loveFileBeanList.size() > 0) {

            loveRv.setVisibility(View.VISIBLE);

            loveAddLove.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    private List<LoveFileBean> getSelectrsList(List<LoveFileBean> beanpics) {
        //判断选择的文件是否在原来的文件中存在,已存在 删掉
        List<LoveFileBean> newPics = new ArrayList<>();
        if (loveFileBeanList.size() > 0) {
            if (beanpics.size() > 0) {
                for (int i = 0; i < beanpics.size(); i++) {
                    boolean isCunzai = false;
                    for (int j = 0; j < loveFileBeanList.size(); j++) {

                        if (beanpics.get(i).getFileTitle().equals(loveFileBeanList.get(j))) {

                            isCunzai = true;
                            break;

                        }


                    }
                    if (!isCunzai) {

                        newPics.add(beanpics.get(i));

                    }

                }


            }

        } else {


            newPics = beanpics;
        }
        return newPics;
    }


    @OnClick(R.id.love_top_righttv)
    public void onViewClicked() {
        if (loveFileBeanList.size() == 0) {
            return;
        }
        if (loveTopRighttv.getText().toString().equals("编辑")) {

            loveTopRighttv.setText("取消");
            for (LoveFileBean bean : loveFileBeanList) {

                bean.setEdit(true);

            }
            adapter.notifyDataSetChanged();
            //TODO implement

            snackbar = Snackbar.make(loveTopRighttv, "", Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.appMain));

            snackbar.show();


            View view = LayoutInflater.from(context).inflate(R.layout.snackbar_love, null);

            ((Snackbar.SnackbarLayout) snackbar.getView()).addView(view, 0);

            final TextView snackbar_love_allselect = (TextView) view.findViewById(R.id.snackbar_love_allselect);
            snackbar_love_selectnum = (TextView) view.findViewById(R.id.snackbar_love_selectnum);
            snackbar_love_selectll = (LinearLayout) view.findViewById(R.id.snackbar_love_selectll);

            snackbar_love_selectll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

              if (loveFileBeanList.size()>0){
                  snackbar_love_selectnum.setText("删除(0)");
                  //去删除
                  List<LoveFileBean> tempArr = new ArrayList<>();

                  for (LoveFileBean bean : loveFileBeanList) {
                      if (bean.isSelect()) {


                      } else {

                          tempArr.add(bean);
                      }

                  }
                  loveFileBeanList = tempArr;

                  //预览的集合也要操作
                  List<LocalMedia> mediaTempList=new ArrayList<>();
                  for (LocalMedia media:selectList){
                      boolean isNeed=false;
                      for (LoveFileBean bean:loveFileBeanList){

                          if (bean.getFilePath().equals(media.getPath())){

                      isNeed=true;

                          }


                      }

                      if(isNeed){

                          mediaTempList.add(media);
                      }


                  }
                  selectList=mediaTempList;



                  adapter.updateAdapter(loveFileBeanList);
                  if (loveFileBeanList.size() <= 0) {
                      loveRv.setVisibility(View.GONE);

                      loveAddLove.setVisibility(View.VISIBLE);
                      fab.setVisibility(View.GONE);
                      loveTopRighttv.setText("编辑");
                      loveTopRighttv.setEnabled(true);
                      if (snackbar != null) {
                          snackbar.dismiss();
                      }
                  } else {

                      loveRv.setVisibility(View.VISIBLE);

                      loveAddLove.setVisibility(View.GONE);
                      fab.setVisibility(View.VISIBLE);
                  }

              }else {

                  CustomToast.showCustomToast("没有数据可删");
              }

                }
            });

            snackbar_love_allselect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (loveFileBeanList.size() > 0) {

                        if (snackbar_love_allselect.getText().toString().equals("全选")) {
                            snackbar_love_allselect.setText("反选");
                            for (LoveFileBean bean : loveFileBeanList) {
                                bean.setSelect(true);
                            }
                            adapter.notifyDataSetChanged();

                        } else {

                            snackbar_love_allselect.setText("全选");
                            for (LoveFileBean bean : loveFileBeanList) {
                                bean.setSelect(false);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        int selectnum = 0;
                        //有多少被选中了
                        for (LoveFileBean bean : loveFileBeanList) {
                            if (bean.getIsType().equals(type)) {

                                if (bean.isSelect()) {

                                    selectnum++;

                                }
                            }

                        }

                        snackbar_love_selectnum.setText("删除(" + selectnum + ")");
                    } else {

                        snackbar_love_selectnum.setText("删除(0)");
                    }

                }
            });


        } else {

            loveTopRighttv.setText("编辑");
            for (LoveFileBean bean : loveFileBeanList) {

                bean.setEdit(false);

            }
            adapter.notifyDataSetChanged();
            if (snackbar != null) {

                snackbar.dismiss();
            }


        }

    }

    @Override
    public void oncheckBack(String path, boolean isselect) {
        int selectnum = 0;
        for (LoveFileBean bean : loveFileBeanList) {
            if (bean.getIsType().equals(type)) {

                if (bean.getFilePath().equals(path)) {
                    bean.setSelect(isselect);


                }
            }


        }
        //有多少被选中了
        for (LoveFileBean bean : loveFileBeanList) {
            if (bean.getIsType().equals(type)) {

                if (bean.isSelect()) {

                    selectnum++;

                }
            }

        }

        snackbar_love_selectnum.setText("删除(" + selectnum + ")");

    }

    @Override
    public void onClickImage(int point) {
        if (type.equals("1")) {

            List<LocalMedia> tempMList=new ArrayList<>();
            tempMList=getLocalmedialist(loveFileBeanList);
            // 预览图片 可自定长按保存路径
            //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
            PictureSelector.create(LoveActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(point, selectList);
        }else if(type.equals("2")) {
            // 预览视频
            PictureSelector.create(LoveActivity.this).externalPictureVideo(loveFileBeanList.get(point).getPath());
        }else {

            // 预览音频
            PictureSelector.create(LoveActivity.this).externalPictureAudio(loveFileBeanList.get(point).getPath());

        }
    }


    //LoveFileBean转LocalMedia  集合
    private List<LocalMedia>   getLocalmedialist(List<LoveFileBean> beanList){

        List<LocalMedia> lmList=new ArrayList<>();
        for (LoveFileBean bean:beanList){
            LocalMedia media=new LocalMedia();
            media.setChecked(bean.isChecked());
            media.setCompressed(bean.isCompressed());
            media.setCompressPath(bean.getCompressPath());
            media.setCut(bean.isCut());
            media.setCutPath(bean.getCutPath());
            media.setDuration(bean.getDuration());
            media.setHeight(bean.getHeight());
            media.setMimeType(bean.getMimeType());
            media.setNum(bean.getNum());
            media.setPath(bean.getPath());
            media.setPictureType(bean.getPictureType());
            media.setPosition(bean.getPosition());
            media.setWidth(bean.getPosition());

            lmList.add(media);

        }


        return lmList;


    }
    //LocalMedia转LoveFileBean  集合
    private List<LoveFileBean>  getLovefilelist(List<LocalMedia> beanList){

        List<LoveFileBean> lfList=new ArrayList<>();
        for (LocalMedia bean:beanList){
            LoveFileBean lfBean=new LoveFileBean();
            lfBean.setChecked(bean.isChecked());
            lfBean.setCompressed(bean.isCompressed());
            lfBean.setCompressPath(bean.getCompressPath());
            lfBean.setCut(bean.isCut());
            lfBean.setCutPath(bean.getCutPath());
            lfBean.setDuration(bean.getDuration());
            lfBean.setHeight(bean.getHeight());
            lfBean.setMimeType(bean.getMimeType());
            lfBean.setNum(bean.getNum());
            lfBean.setPath(bean.getPath());
            lfBean.setPictureType(bean.getPictureType());
            lfBean.setPosition(bean.getPosition());
            lfBean.setWidth(bean.getPosition());

            lfBean.setSelect(false);
            lfBean.setEdit(false);
            lfBean.setIsType(type);
            lfBean.setFileTitle(bean.getPath());
            lfBean.setFilePath(bean.getPath());
            lfList.add(lfBean);

        }


        return lfList;


    }


}
