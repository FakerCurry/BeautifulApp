package com.sjw.beautifulapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.utils.DimensionTranslate;

import java.util.ArrayList;
import java.util.List;

public class TopTabView extends LinearLayout {

    private List<String> tabList;

    private TextView[] tvTabsArr;

    TopTabListener listener;

    private int selectPosition;



    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        if (tvTabsArr.length!=0){

            for (int i=0;i<tvTabsArr.length;i++){

                if (i==selectPosition){

                    tvTabsArr[i].setBackgroundColor(getResources().getColor(R.color.tab_bg));


                }else {
                    tvTabsArr[i].setBackgroundColor(getResources().getColor(R.color.white));
                }




            }
        }



    }

    public TopTabView(Context context) {
        this(context,null);
    }

    public TopTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TopTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initData();
//
//
//        initView(context);
    }

    private void initData() {

      tabList=new ArrayList<>();

    }


    public void setTabList(List<String> tabList,Context context,int selectP) {
        this.tabList = tabList;
        tvTabsArr=new TextView[tabList.size()];

    if(getChildCount()>0){

        removeAllViews();
    }

      initView(context,selectP);

    }

    private void initView(Context context, int selectP) {


        setOrientation(HORIZONTAL);
        setBackground(getResources().getDrawable(R.drawable.shape_item_index2_rv3));

        if (tabList.size()!=0){

            for(int i=0;i<tabList.size();i++){



                TextView tv=new TextView(context);

                tv.setText(tabList.get(i));

                tv.setPadding(10,5,10,5);


                LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);

                ll.gravity=Gravity.CENTER;

                tv.setTextSize(15);

                tv.setLayoutParams(ll);

                tv.setTag(i);

                tvTabsArr[i]=tv;

                //初始化选择
                if (i==selectP){

                    tv.setBackgroundColor(getResources().getColor(R.color.tab_bg));


                }else {
                    tv.setBackgroundColor(getResources().getColor(R.color.white));
                }

                addView(tv);


                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        for (int j=0;j<tvTabsArr.length;j++){

                            if ((j+"").equals(view.getTag()+"")){

                                tvTabsArr[j].setBackgroundColor(getResources().getColor(R.color.tab_bg));


                            }else {
                                tvTabsArr[j].setBackgroundColor(getResources().getColor(R.color.white));
                            }


                        }
                        listener.onSlect(Integer.parseInt(view.getTag()+""));


                    }
                });




                if (i!=3){

                    View view=new View(context);

                    view.setBackgroundColor(getResources().getColor(R.color.tab_line_color));

                    LinearLayout.LayoutParams llView=new LinearLayout.LayoutParams(DimensionTranslate.dip2px(context,1),LayoutParams.MATCH_PARENT);


                    view.setLayoutParams(llView);

                    addView(view);
                }







            }


        }









    }


    public void setOnselectTabListener(TopTabListener listener){

        this.listener=listener;




    }




    public interface  TopTabListener{


        void onSlect(int position);


    }
}
