package com.sjw.beautifulapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.bean.LoveFileBean;

import java.util.List;

public class LoveAdapter extends BaseRecyclerAdapter<LoveAdapter.ViewHolder> {
    private List<LoveFileBean> list;
    private Context mContext;
    private int largeCardHeight, smallCardHeight;
    private String type;
    private  SetOnCheckBack back;


    public interface SetOnCheckBack {

        void oncheckBack(String path,boolean isselect);
        void onClickImage(int point);

    }

    public void setOncheckBackClickListener(SetOnCheckBack back){
        this.back=back;
    }


    public LoveAdapter(List<LoveFileBean> list, Context context, String type) {
        this.list = list;
        this.mContext = context;
        this.type=type;
//        largeCardHeight = DensityUtil.dip2px(context, 150);
//        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    public  void  updateAdapter(List<LoveFileBean> list){

        this.list=list;
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position, boolean isItem) {
        final LoveFileBean info = list.get(position);
        holder.item_love_title.setText(info.getFileTitle());


        if (info.isEdit()){

            holder.item_love_edit_rl.setVisibility(View.VISIBLE);
        }else {

            holder.item_love_edit_rl.setVisibility(View.GONE);

        }



//        holder.item_love_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//
//            }
//        });
        holder.item_love_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.item_love_cb.isChecked()){
                    holder.item_love_cb.setChecked(false);
                    back.oncheckBack(info.getFilePath(),false);
                }else {

                    holder.item_love_cb.setChecked(true);
                    back.oncheckBack(info.getFilePath(),true);
                }


            }
        });

        holder.item_love_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
//                    holder.item_love_cb.setChecked(false);
                    back.oncheckBack(info.getFilePath(),true);
                }else {

//                    holder.item_love_cb.setChecked(true);
                    back.oncheckBack(info.getFilePath(),false);
                }

            }
        });

        if (info.isSelect()){

            holder.item_love_cb.setChecked(true);

        }else {
            holder.item_love_cb.setChecked(false);

        }

        if (type.equals("1")){
            //        RequestOptions options = new RequestOptions();
            Glide.with(mContext)
                    .load(info.getFilePath())
//                .apply(options)
                    .into(holder.item_love_img);
            holder.item_play.setVisibility(View.GONE);

        }else if (type.equals("2")){
            //        RequestOptions options = new RequestOptions();
            Glide.with(mContext)
                    .load(info.getFilePath())
//                .apply(options)
                    .into(holder.item_love_img);
            holder.item_play.setVisibility(View.VISIBLE);

        }else {
            //        RequestOptions options = new RequestOptions();
            Glide.with(mContext)
                    .load(R.drawable.audio)
//                .apply(options)
                    .into(holder.item_love_img);
            holder.item_play.setVisibility(View.GONE);

        }

        holder.item_love_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back.onClickImage(position);

            }
        });


    }

    @Override
    public int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    public void setData(List<LoveFileBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_love, parent, false);
        ViewHolder vh = new ViewHolder(v, true);
        return vh;
    }

//    public void insert(EpisodeBean person, int position) {
//        insert(list, person, position);
//    }
//
//    public void remove(int position) {
//        remove(list, position);
//    }
//
//    public void clear() {
//        clear(list);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView item_love_img;
        public TextView item_love_title;
        public ImageView item_play;

        public RelativeLayout item_love_edit_rl;
        public CheckBox item_love_cb;
        public LinearLayout item_love_ll;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                item_love_img = (ImageView) itemView.findViewById(R.id.item_love_img);
                item_love_title = (TextView) itemView.findViewById(R.id.item_love_title);
                item_play=(ImageView)itemView.findViewById(R.id.item_play);
                item_love_edit_rl=(RelativeLayout)itemView.findViewById(R.id.item_love_edit_rl);
                item_love_cb=(CheckBox)itemView.findViewById(R.id.item_love_cb);
                item_love_ll=(LinearLayout)itemView.findViewById(R.id.item_love_ll);
            }

        }
    }

//    public Person getItem(int position) {
//        if (position < list.size())
//            return list.get(position);
//        else
//            return null;
//    }

}