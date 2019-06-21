package fun.hara.lacontacts.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import fun.hara.lacontacts.CallRecordEditActivity;
import fun.hara.lacontacts.ContactsAdapter;
import fun.hara.lacontacts.MainActivity;
import fun.hara.lacontacts.R;
import fun.hara.lacontacts.callrecord.CallRecord;
import fun.hara.lacontacts.dao.ContactsDAO;
import fun.hara.lacontacts.domain.ContactInfo;
import fun.hara.lacontacts.getcallrecord.GetCallRecord;
import fun.hara.lacontacts.util.CallRecordAdapter;


public class CallFragment extends Fragment {
    @Nullable
    private String str = "";//用于存取号码
    final static String TAG="CallFramgment";//用于测试
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, container, false);
        initCallRecord(view);//初始化通讯记录
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDialHideAndShow();  // 对拨号键盘的显示与隐藏功能进行初始化
        setCallClick();//开始拨号
    }

    /**
     * 添加通讯显示和触发事件
     *
     * @param view
     */
    public void initCallRecord(View view) {
        // 获取所有联系人列表
        //TODO 改成获取通讯记录
        List<CallRecord> list = new GetCallRecord().getCallInfos(getActivity());//getActivity()
        // 将联系人数据填充到listview中
        ListView callRecordInfo = (ListView) view.findViewById(R.id.callRecordList);//获取消息记录list用于设置触发事件
        final CallRecordAdapter adapter = new CallRecordAdapter(list, getActivity());//getActivity()
        callRecordInfo.setAdapter(adapter);//为消息记录list添加adapter
        // 为每行设置点击响应事件

        callRecordInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                CallRecord mRecord = (CallRecord) adapter.getItem(position);//获取每一位联系人
                Intent intent = new Intent(getActivity(), CallRecordEditActivity.class);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());//转换日期格式
                String dateString = format.format(mRecord.getDate());//要先转时间再传
                intent.putExtra("phone", mRecord.getPhone());//将数据传入CallRecordEditActivity
                intent.putExtra("name", mRecord.getName());
                intent.putExtra("date", dateString);
                intent.putExtra("type", mRecord.getType() + "");
                startActivityForResult(intent, 0);
            }
        });
        /**
         * 触摸记录收起键盘事件
         */

        callRecordInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                     //   触摸时操作
                        Log.v(TAG, "getCallInfos: "+1);
                        RelativeLayout b = (RelativeLayout) getActivity().findViewById(R.id.dial);
                        b.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 触摸移动时的操作
                        RelativeLayout c = (RelativeLayout) getActivity().findViewById(R.id.dial);
                        c.setVisibility(View.INVISIBLE);
                        break;

                    case MotionEvent.ACTION_UP:
                      //   触摸抬起时的操作
                        FloatingActionButton a = (FloatingActionButton) getActivity().findViewById(R.id.showDialBtn);
                        a.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });
    }
        /**
         *  初始化联系人列表，onCreateView()调用无效
         */
        public void initCallRecord () {
            initCallRecord(getView());
        }


        /**
         * 对拨号键盘的显示与隐藏功能进行初始化
         */
        private void initDialHideAndShow () {
            /* 为调出包含键盘按钮添加响应事件 */
            final FloatingActionButton showDialBtn = (FloatingActionButton) getActivity().findViewById(R.id.showDialBtn);
            final RelativeLayout dial = (RelativeLayout) getActivity().findViewById(R.id.dial);
            showDialBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 隐藏按钮
                    showDialBtn.setVisibility(View.INVISIBLE);

                    // 弹出键盘
                    Animation animbottomIn = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_in);
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
                    Animation animbottomOut = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_out);
                    animbottomOut.setDuration(240);
                    showDialBtn.setVisibility(View.VISIBLE);
                    dial.setVisibility(View.GONE);
                    dial.startAnimation(animbottomOut);
                }
            });
        }
        private void setCallClick () {
            /*按键显示号码*/
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
            ImageButton mDelBtn = (ImageButton) getActivity().findViewById(R.id.delDialBtn);
            ImageButton mCallBtn = (ImageButton) getActivity().findViewById(R.id.callDialBtn);
            final TextView telephone = (TextView) getActivity().findViewById(R.id.editText3);
            //按钮0-9*#的点击事件
            mbtn0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str += "0";
                    telephone.setText(str);
                }
            });
            mbtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str += "1";
                    telephone.setText(str);
                }
            });
            mbtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str += "2";
                    telephone.setText(str);
                }
            });
            mbtn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str += "3";
                    telephone.setText(str);
                }
            });
            mbtn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str += "4";
                    telephone.setText(str);
                }
            });
            mbtn5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str += "5";
                    telephone.setText(str);
                }
            });
            mbtn6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str += "6";
                    telephone.setText(str);
                }
            });
            mbtn7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str += "7";
                    telephone.setText(str);
                }
            });
            mbtn8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str += "8";
                    telephone.setText(str);
                }
            });
            mbtn9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str += "9";
                    telephone.setText(str);
                }
            });
            mbtnSharp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str += "#";
                    telephone.setText(str);
                }
            });

            mbtnStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str += "*";
                    telephone.setText(str);
                }
            });
            mDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (str.equals("")) {
                        str = "";
                        telephone.setText("请输入手机号码");
                        return;
                    }
                    str = str.substring(0, str.length() - 1);
                    telephone.setText(str);
                }
            });
            //长按清零
            mDelBtn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    telephone.setText("");
                    return false;
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
                    initCallRecord();
                    str = "";
                    telephone.setText("请输入手机号码");

                }
            });
        }
    }
