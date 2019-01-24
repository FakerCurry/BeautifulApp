package com.sjw.beautifulapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.adapter.Index24ItemAdapter;
import com.sjw.beautifulapp.bean.Index2Item3ItemBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 2018/5/6.
 */

public class Index2EveryFragment extends Fragment {

    @BindView(R.id.index2_every_rv)
    RecyclerView index2EveryRv;
    Unbinder unbinder;
    private Bundle arg;
    private Index24ItemAdapter   adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arg = getArguments();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, null);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Index2Item3ItemBean> index2Item3ItemBeanList=new ArrayList<>();
        index2Item3ItemBeanList= (List<Index2Item3ItemBean>) arg.getSerializable("itemList");
        adapter=new Index24ItemAdapter(index2Item3ItemBeanList,getActivity());

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        index2EveryRv.setLayoutManager(linearLayoutManager);
        index2EveryRv.setAdapter(adapter);

    }

    public static Index2EveryFragment newInstance(Bundle args) {
        Index2EveryFragment fragment = new Index2EveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
