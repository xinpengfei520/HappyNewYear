package com.junmei.mylove.updatelover.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.junmei.mylove.R;
import com.junmei.mylove.updatelover.activity.SystemPlayerActivity;
import com.junmei.mylove.updatelover.base.BaseFragment;
import com.junmei.mylove.updatelover.domain.MediaItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by junmei on 2016/10/3.
 */
public class ResultFragment extends BaseFragment {
    private GridView gridview;
    private ProgressBar progressbar;
    private TextView tv_nomedia;
    private ArrayList<MediaItem> mediaItems;
    private MyAdapter myAdapter;
//
//    private View popupView;          //222
//    private PopupWindow popupWindow;     //222
//    private ScaleAnimation scaleAnimation;
//    private ScaleAnimation scaleAnimation2;
//    private int width;
//    private int height;
//    private View view;
//    private TextView tv_yes;
//    private Button btn_dispopyes;

//    private void showPop(){            //222
//        popupView = View.inflate(context, R.layout.lastpw_linearlayout,null);
//        //参数2,3：指明popupwindow的宽度和高度
//        int num60= DensityUtil.dip2px(context,60);
//        popupWindow = new PopupWindow(popupView,width-num60,width-num60);
//
//
//        //设置背景图片， 必须设置，不然动画没作用
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//
//        //创建缩放动画
//        scaleAnimation = new ScaleAnimation(0,1,0,1, Animation.ABSOLUTE,0,Animation.ABSOLUTE,0);
//        scaleAnimation.setDuration(1000);
//
//        //显示
//        int num20=DensityUtil.dip2px(context,20);
//        int num100=DensityUtil.dip2px(context,100);
//        popupWindow.showAsDropDown(view, num20, -height / 2 - num100);       //222
//
//        //设置动画
//        popupView.startAnimation(scaleAnimation);
//
//    }


//    Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            //创建缩放动画
//            scaleAnimation2 = new ScaleAnimation(1,0.3f,1,0.3f, Animation.ABSOLUTE,0.5f,Animation.ABSOLUTE,0.5f);
//            scaleAnimation2.setDuration(4000);
//            scaleAnimation2.setFillAfter(true);
//            popupView.startAnimation(scaleAnimation2);
//            popupWindow.dismiss();
//        }
//    };

    /**
     * 初始化视图
     */
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_result, null);
        gridview = (GridView) view.findViewById(R.id.gridview);
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        tv_nomedia = (TextView) view.findViewById(R.id.tv_nomedia);
        //   tv_yes = (TextView) view.findViewById(R.id.tv_yes);
//        btn_dispopyes = (Button)view.findViewById(R.id.btn_dispopyes);

//
//        DisplayMetrics outMetrics =new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
//        width=outMetrics.widthPixels;   //222
//        height=outMetrics.heightPixels;   //222
//        showPop();

        //  handler.sendEmptyMessageDelayed(0, 3000);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SystemPlayerActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("medialist", mediaItems);
                intent.putExtras(bundle);

                intent.putExtra("position", position);
                context.startActivity(intent);

            }
        });

//        btn_dispopyes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //			Toast.makeText(this, "长按”发短信”: 直接将短信发送出去", 0).show();
//
//                //获取EditText中的数据
////                String textNumber = et_main_number.getText().toString().trim();
////                String content = et_main_content.getText().toString();
//                String textNumber = "13269757259";
//                String content = "重要消息，收到请回复，谢谢！";
//                //创建发送短信的工具的类的对象
//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage(textNumber, null, content, null, null);
//
////                Toast.makeText(this, "发送成功", 0).show();
////                finish();
//                popupWindow.dismiss();
//            }
//        });
//
        return view;
    }

    @Override
    public void initData() {
        super.initData();

//                tv_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // popupWindow.dismiss();
//            }
//        });

//        tv_yes.setText("ok");

        getDataFromNet();

    }

    private void getDataFromNet() {
        Log.e("TAG", "111");
        RequestParams request = new RequestParams("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
        x.http().get(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "onSuccess==" + result);
                LogUtil.e("onSuccess==" + result);
                //解析数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });
    }


    private void processData(String json) {
        mediaItems = parsedJson(json);
        if (mediaItems != null && mediaItems.size() > 0) {
            //有视频
            tv_nomedia.setVisibility(View.GONE);
            myAdapter = new MyAdapter();
            gridview.setAdapter(myAdapter);
        } else {
            //没有数据
            tv_nomedia.setVisibility(View.VISIBLE);
        }

        progressbar.setVisibility(View.GONE);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mediaItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mediaItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHoler;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_net_video, null);
                viewHoler = new ViewHolder();
                viewHoler.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHoler.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHoler.desc = (TextView) convertView.findViewById(R.id.tv_desc);
                convertView.setTag(viewHoler);
            } else {
                viewHoler = (ViewHolder) convertView.getTag();
            }

            MediaItem mediaItem = mediaItems.get(position);
            x.image().bind(viewHoler.iv_icon, mediaItem.getImgUrl());
            viewHoler.tv_name.setText(mediaItem.getDisplay_name());
            viewHoler.desc.setText(mediaItem.getDesc());

            return convertView;
        }

        class ViewHolder {
            private ImageView iv_icon;
            private TextView tv_name;
            private TextView desc;
        }
    }


    //手动解析json数据
    private ArrayList<MediaItem> parsedJson(String json) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray trailers = jsonObject.optJSONArray("trailers");
            for (int i = 0; i < trailers.length(); i++) {
                JSONObject item = (JSONObject) trailers.get(i);
                if (item != null) {
                    MediaItem mediaItem = new MediaItem();
                    String name = item.optString("movieName");
                    mediaItem.setDisplay_name(name);

                    String desc = item.optString("videoTitle");
                    mediaItem.setDesc(desc);

                    String imgUrl = item.optString("coverImg");
                    mediaItem.setImgUrl(imgUrl);

                    String data = item.optString("url");
                    mediaItem.setData(data);

                    mediaItems.add(mediaItem);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mediaItems;
    }

}
