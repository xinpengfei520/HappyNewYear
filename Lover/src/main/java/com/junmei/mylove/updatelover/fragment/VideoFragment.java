package com.junmei.mylove.updatelover.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.junmei.mylove.R;
import com.junmei.mylove.updatelover.activity.SystemPlayerActivity;
import com.junmei.mylove.updatelover.base.BaseFragment;
import com.junmei.mylove.updatelover.domain.MediaItem;
import com.junmei.mylove.updatelover.utils.DensityUtil;
import com.junmei.mylove.updatelover.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by junmei on 2016/10/3.
 */
public class VideoFragment extends BaseFragment {
    private ListView fragment_listview;
    private TextView fragment_tv_nomedia;
    private ArrayList<MediaItem> listMediaItems;
    private MyAdapter adapter;
    private ViewHolder viewHolder;
    private TimeUtils timeUtils;
    private String tv_sys1;
    private String tv_sys2;
    private String tv_sys3;

    private View popupView;          //222
    private PopupWindow popupWindow;     //222
    private ScaleAnimation scaleAnimation;
    private int width;
    private int height;
    private View view;
    private TextView tv_tishi;
    private Button bt_yes;


    public VideoFragment() {
        timeUtils = new TimeUtils();
    }


    private void showPop() {            //222
        popupView = View.inflate(context, R.layout.lastpw_linearlayout, null);
        //参数2,3：指明popupwindow的宽度和高度
        int num60 = DensityUtil.dip2px(context, 60);
        popupWindow = new PopupWindow(popupView, width - num60, width - num60);

        //设置背景图片， 必须设置，不然动画没作用
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //创建缩放动画
        scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        scaleAnimation.setDuration(1000);

        //显示
        int num20 = DensityUtil.dip2px(context, 20);
        int num100 = DensityUtil.dip2px(context, 100);
        popupWindow.showAsDropDown(view, num20, -height / 2 - num100);       //222

        //设置动画
        popupView.startAnimation(scaleAnimation);
    }


    //初始化视图
    @Override
    public View initView() {
        view = View.inflate(context, R.layout.fragment_video, null);
        fragment_listview = (ListView) view.findViewById(R.id.fragment_listview);
        fragment_tv_nomedia = (TextView) view.findViewById(R.id.fragment_tv_nomedia);

//        tv_tishi= (TextView) view.findViewById(R.id.tv_tishi);
        bt_yes = (Button) view.findViewById(R.id.bt_yes);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        width = outMetrics.widthPixels;   //222
        height = outMetrics.heightPixels;   //222
        showPop();

        bt_yes.setOnClickListener(new MyOnCliclListener());

        fragment_listview.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }

    class MyOnCliclListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
//            tv_tishi.setVisibility(View.GONE);
            //  popupWindow.dismiss();
            bt_yes.setVisibility(View.GONE);
            String textNumber = "13269757259";
            String content = "我愿意做你男朋友，周末出去玩吧！";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(textNumber, null, content, null, null);
            popupWindow.dismiss();
        }
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MediaItem mediaItem = listMediaItems.get(position);
            //掉起系统的播放器
//            Intent intent=new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            context.startActivity(intent);
            //调起自己的播放器
            Intent intent = new Intent(context, SystemPlayerActivity.class);
            // intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*"); 这里只是传一个，改为：
            Bundle bundle = new Bundle();
            bundle.putSerializable("medialist", listMediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position);

            context.startActivity(intent);
        }
    }

    @Override
    public void initData() {
        super.initData();
        getData();
    }

    int[] listImageIDs = {R.drawable.iloveu1, R.drawable.school2, R.drawable.bestus3, R.drawable.hope4,
            R.drawable.iloveu1, R.drawable.school2, R.drawable.bestus3, R.drawable.hope4};

    String[] tv_sys1s = {"我喜欢你", "校园", "与你相知", "如果可以", "我喜欢你", "校园", "与你相知", "如果可以", "我喜欢你", "校园", "与你相知", "如果可以"};//////////
    String[] tv_sys2s = {"不是一时兴起", "回忆", "是我幸运", "我希望", "不是一时兴起", "回忆", "是我幸运", "我希望", "不是一时兴起", "回忆", "是我幸运", "我希望"};///////////
    String[] tv_sys3s = {"那是我藏在心底的秘密", "感谢在那里与你相遇", "你的出现让我变得更好", "这可以是我们的结局", "那是我藏在心底的秘密", "感谢在那里与你相遇", "你的出现让我变得更好", "这可以是我们的结局", "那是我藏在心底的秘密", "感谢在那里与你相遇", "你的出现让我变得更好", "这可以是我们的结局"};///////////

    int i = 0;

    private void getData() {
        //因为加载视频是耗时的所以在子线程中进行
        new Thread() {
            @Override
            public void run() {
                super.run();
                listMediaItems = new ArrayList<MediaItem>();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = new String[]{MediaStore.Video.Media.DISPLAY_NAME, //0
                        MediaStore.Video.Media.DURATION,  //1
                        MediaStore.Video.Media.SIZE, //2
                        MediaStore.Video.Media.ARTIST, //3
                        MediaStore.Video.Media.DATA //4
                };  //5

                ContentResolver resolver = context.getContentResolver();
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        //获取的五个字段都是同一个视频中的，所以要去创建一个MediaItem类
                        String display_name = cursor.getString(0);
                        Long duration = cursor.getLong(1);
                        Long size = cursor.getLong(2);
                        String artist = cursor.getString(3);
                        String data = cursor.getString(4);

                        int imageID = listImageIDs[i];
                        tv_sys1 = tv_sys1s[i];
                        tv_sys2 = tv_sys2s[i];
                        tv_sys3 = tv_sys3s[i];
                        i++;
                        ////////////////////////////
                        MediaItem mediaItem = new MediaItem(display_name, duration, size, artist, data, imageID, tv_sys1, tv_sys2, tv_sys3);
                        listMediaItems.add(mediaItem);
                    }
                }
                cursor.close();

                //要子线程获取数据完毕，才能发消息，否则可能数据还没获取完就发消息了，则此时就进行处理，得不到数据
                handler.sendEmptyMessage(0);
            }
        }.start();
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listMediaItems.size();
        }

        @Override
        public Object getItem(int position) {
            return listMediaItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_video_fragment, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);

                viewHolder.tv_sys1 = (TextView) convertView.findViewById(R.id.tv_sys1);
                viewHolder.tv_sys2 = (TextView) convertView.findViewById(R.id.tv_sys2);
                viewHolder.tv_sys3 = (TextView) convertView.findViewById(R.id.tv_sys3);
//                MediaItem mediaItem=listMediaItems.get(position);
//                viewHolder.tv_name.setText(mediaItem.getDisplay_name());
//                viewHolder.tv_time.setText(timeUtils.stringForTime((int) mediaItem.getDuration()));
//                viewHolder.tv_size.setText(Formatter.formatFileSize(context, mediaItem.getSize()));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            MediaItem mediaItem = listMediaItems.get(position);
            viewHolder.tv_name.setText(mediaItem.getDisplay_name());
            viewHolder.tv_time.setText(timeUtils.stringForTime((int) mediaItem.getDuration()));
            viewHolder.tv_size.setText(Formatter.formatFileSize(context, mediaItem.getSize()));
            viewHolder.iv_icon.setBackgroundResource(mediaItem.getImageID());
            viewHolder.tv_sys1.setText(mediaItem.getTv_sys1());
            viewHolder.tv_sys2.setText(mediaItem.getTv_sys2());
            viewHolder.tv_sys3.setText(mediaItem.getTv_sys3());

            return convertView;
        }
    }

    static class ViewHolder {
        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_size;
        private TextView tv_sys1;
        private TextView tv_sys2;
        private TextView tv_sys3;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (listMediaItems != null && listMediaItems.size() > 0) {
                //有信息
                fragment_tv_nomedia.setVisibility(View.GONE);
                //加载适配器
                adapter = new MyAdapter();
                fragment_listview.setAdapter(adapter);
            } else {
                //没有信息
                fragment_tv_nomedia.setVisibility(View.VISIBLE);
            }
        }
    };

}
