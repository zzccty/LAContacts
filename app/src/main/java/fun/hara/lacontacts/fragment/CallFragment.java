package fun.hara.lacontacts.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import fun.hara.lacontacts.MainActivity;
import fun.hara.lacontacts.R;


public class CallFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_call,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 对拨号键盘的显示与隐藏功能进行初始化
        initDialHideAndShow();
    }

    /**
     * 对拨号键盘的显示与隐藏功能进行初始化
     */
    private void initDialHideAndShow(){
         /* 为调出包含键盘按钮添加响应事件 */
        final FloatingActionButton showDialBtn  = (FloatingActionButton)getActivity().findViewById(R.id.showDialBtn);
        final RelativeLayout dial = (RelativeLayout) getActivity().findViewById(R.id.dial);
        showDialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 隐藏按钮
                showDialBtn.setVisibility(View.INVISIBLE);

                // 弹出键盘
                Animation animbottomIn = AnimationUtils.loadAnimation(getActivity(),  R.anim.bottom_in);
                animbottomIn.setDuration(240);
                dial.setVisibility(View.VISIBLE);
                dial.startAnimation(animbottomIn);
            }
        });

        /* 收回键盘事件 */
        ImageButton hideDialBtn = (ImageButton) getActivity().findViewById(R.id.hideDialBtn);
        hideDialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 隐藏键盘
                Animation animbottomOut = AnimationUtils.loadAnimation(getActivity(),  R.anim.bottom_out);
                animbottomOut.setDuration(240);
                showDialBtn.setVisibility(View.VISIBLE);
                dial.setVisibility(View.GONE);
                dial.startAnimation(animbottomOut);
            }
        });
    }
}
