package fun.hara.lacontacts.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import fun.hara.lacontacts.MainActivity;
import fun.hara.lacontacts.R;


public class CallFragment extends Fragment {
    @Nullable
    private String str="";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDialHideAndShow();  // 对拨号键盘的显示与隐藏功能进行初始化
        setCallClick();//开始拨号
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
    private void setCallClick(){
        /*按键显示数字*/
        /*TODO:删除按钮还需添加一个长按快删功能
        后面看能不能优化成switch，fragment中的按钮事件可能不同*/
        Button mbtn1 = (Button) getActivity().findViewById(R.id.btn1);
        Button mbtn2 = (Button) getActivity().findViewById(R.id.btn2);
        Button mbtn3 = (Button) getActivity().findViewById(R.id.btn3);
        Button mbtn4 = (Button) getActivity().findViewById(R.id.btn4);
        Button mbtn5 = (Button) getActivity().findViewById(R.id.btn5);
        Button mbtn6 = (Button) getActivity().findViewById(R.id.btn6);
        Button mbtn7 = (Button) getActivity().findViewById(R.id.btn7);
        Button mbtn8 = (Button) getActivity().findViewById(R.id.btn8);
        Button mbtn9 = (Button) getActivity().findViewById(R.id.btn9);
        Button mbtn0 = (Button) getActivity().findViewById(R.id.btn0);
        Button mbtnSharp = (Button) getActivity().findViewById(R.id.btnSharp);
        Button mbtnStar = (Button) getActivity().findViewById(R.id.star);
        ImageButton mDelBtn = (ImageButton)getActivity().findViewById(R.id.delDialBtn);
        ImageButton mCallBtn = (ImageButton)getActivity().findViewById(R.id.callDialBtn);
        final TextView telephone = (TextView) getActivity().findViewById(R.id.editText3);
        //按钮0-9*#的点击事件
        mbtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str+="0";
                telephone.setText(str);
            }
        });
        mbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str+="1";
                telephone.setText(str);
            }
        });
        mbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str+="2";
                telephone.setText(str);
            }
        });
        mbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str+="3";
                telephone.setText(str);
            }
        });
        mbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str+="4";
                telephone.setText(str);
            }
        });
        mbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str+="5";
                telephone.setText(str);
            }
        });
        mbtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str+="6";
                telephone.setText(str);
            }
        });
        mbtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str+="7";
                telephone.setText(str);
            }
        });
        mbtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str+="8";
                telephone.setText(str);
            }
        });
        mbtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str+="9";
                telephone.setText(str);
            }
        });
        mbtnSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str+="#";
                telephone.setText(str);
            }
        });
        mbtnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str+="*";
                telephone.setText(str);
            }
        });
        mDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str.equals(""))  return;//防止没号码
                str=str.substring(0,str.length() - 1);
                telephone.setText(str);
            }
        });
        mCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //设置拨打电话的动作
                intent.setAction(Intent.ACTION_CALL);
                //设置拨打电话的号码
                 intent.setData(Uri.parse("tel:" + str));
               //开启打电话的意图
                startActivity(intent);
                str="";
                telephone.setText(str);
            }
        });
    }
}
