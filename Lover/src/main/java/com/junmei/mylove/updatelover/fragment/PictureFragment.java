package com.junmei.mylove.updatelover.fragment;


import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.junmei.mylove.R;
import com.junmei.mylove.updatelover.activity.AudioPlayerActivity;
import com.junmei.mylove.updatelover.base.BaseFragment;

/**
 * Created by junmei on 2016/10/3.
 */
//public class PictureFragment extends BaseFragment implements HttpData.HttpGetListener{
public class PictureFragment extends BaseFragment {
    private TextView tv_fpic2;
    private ImageView ig_light;
    private ScaleAnimation scaleAnimation;

    /**
     * 初始化视图*/
    @Override
    public View initView() {
//        textView=new TextView(context);
//        textView.setTextSize(20);
//        textView.setTextColor(Color.RED);
//        textView.setGravity(Gravity.CENTER);
//        View view=View.inflate(context, R.layout.fragment_picture,null);
        View view=View.inflate(context, R.layout.fragment_picture,null);
        tv_fpic2 = (TextView)view.findViewById(R.id.tv_fpic2);
        ig_light=(ImageView)view.findViewById(R.id.ig_light);

        scaleAnimation=tvAnimation();
        tv_fpic2.startAnimation(scaleAnimation);

        tv_fpic2.setOnClickListener(new MyOnClickListener());

        return view;
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            Intent intent1=new Intent(context, AnimationPictureActivity.class);
            Intent intent1=new Intent(context, AudioPlayerActivity.class);
            context.startActivity(intent1);
        }
    }

    public ScaleAnimation tvAnimation(){
        ScaleAnimation scaleAnimation=new ScaleAnimation(1f,1.3f,1f,1.3f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setRepeatCount(100);
        scaleAnimation.setInterpolator(new LinearInterpolator());

        return scaleAnimation;
    }

    @Override
    public void initData() {
        super.initData();
//        textView.setText("照片记忆~");
    }

}


//
////    private ListView lv_chat_msg;
////    private Button btn_send;
////    private EditText et_input;
////    private ArrayList<Msg> messages;
////    private MyTuLingAdapter adapter;
////    private String[] array;
////    private View view;
////    private HttpData httpData;
////    private final String url = "http://apis.haoservice.com/efficient/robot?info=";
////    private String key;
////
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        Log.e("TAG", "Fragment2--->onCreate");
////    }
////
////    @Override
////    public View initView() {
////view = View.inflate(getActivity(), R.layout.fragment_picture2, null);
////        lv_chat_msg = (ListView) view.findViewById(R.id.lv_chat_msg);
////        lv_chat_msg.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
////        et_input = (EditText) view.findViewById(R.id.et_input);
////        btn_send = (Button) view.findViewById(R.id.btn_send);
////        btn_send.setOnClickListener(new MyOnClickListener());
////        return view;
////    }
////
////    //http://apis.haoservice.com/efficient/robot?info=你好&address=&key=4dd0c15c3e5444f8a1469e151934491a
////    @Override
////    public void initData() {
////        messages = new ArrayList<>();
////        array = getActivity().getResources().getStringArray(R.array.welcome_array);
////        int r = new Random().nextInt(array.length);
////        key = "&address=&key=" + getActivity().getResources().getString(R.string.TuLingKey);
////        Msg msg = new Msg(array[r], Msg.TYPE_LEFT);
////        messages.add(msg);
////        adapter = new MyTuLingAdapter();
////        lv_chat_msg.setAdapter(adapter);
////    }
////
////    class MyOnClickListener implements View.OnClickListener {
////
////        @Override
////        public void onClick(View v) {
////            String content = et_input.getText().toString().trim();
////            if (content == "") {
////                Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
////            } else {
////                Msg message = new Msg(content, Msg.TYPE_RIGHT);
////                messages.add(message);
////                httpData = new HttpData(url + content + key, PictureFragment.this);
////                httpData.execute();
////                adapter.notifyDataSetChanged();
////            }
////            et_input.setText("");
////        }
////    }
////
////    @Override
////    public void getDataUri(String data) {
////        Msg msg = new Msg(data, Msg.TYPE_LEFT);
////        messages.add(msg);
////        adapter.notifyDataSetChanged();
////    }
////
////    public class MyTuLingAdapter extends BaseAdapter {
////
////        @Override
////        public int getCount() {
////            return messages.size();
////        }
////
////        @Override
////        public Msg getItem(int position) {
////            return messages.get(position);
////        }
////
////        @Override
////        public long getItemId(int position) {
////            return position;
////        }
////
////        @Override
////        public View getView(int position, View convertView, ViewGroup parent) {
////            Msg msg = messages.get(position);
////            ViewHolder vHolder;
////            View view;
////            if (convertView == null) {
////                view = View.inflate(getActivity(), R.layout.item_chat, null);
////                vHolder = new ViewHolder();
////                vHolder.linear_left = (LinearLayout) view
////                        .findViewById(R.id.linear_left);
////                vHolder.linear_right = (LinearLayout) view
////                        .findViewById(R.id.linear_right);
////                vHolder.msg_left = (TextView) view.findViewById(R.id.mes_left);
////                vHolder.msg_right = (TextView) view.findViewById(R.id.mes_right);
////                view.setTag(vHolder);
////            } else {
////                view = convertView;
////                vHolder = (ViewHolder) view.getTag();
////            }
////            if (msg.getType() == Msg.TYPE_LEFT) {
////                vHolder.linear_left.setVisibility(View.VISIBLE);
////                vHolder.linear_right.setVisibility(View.GONE);
////                vHolder.msg_left.setText(msg.getContent());
////            } else {
////                vHolder.linear_right.setVisibility(View.VISIBLE);
////                vHolder.linear_left.setVisibility(View.GONE);
////                vHolder.msg_right.setText(msg.getContent());
////            }
////            return view;
////        }
////
////        class ViewHolder {
////            LinearLayout linear_left;
////            LinearLayout linear_right;
////            TextView msg_left;
////            TextView msg_right;
////        }
////
////    }
////}

/////////////////////////////////////////////////////
//    private ListView fragment_listview;
//    private TextView fragment_tv_nomedia;
//    private ArrayList<MediaItem> listMediaItems;
//    private MyAdapter adapter;
//    private ViewHolder viewHolder;
//    private TimeUtils utils;
////    private String tv_sys1;
////    private String tv_sys2;
////    private String tv_sys3;
//
//    public PictureFragment(){
//        utils=new TimeUtils();
//    }
//
//    //初始化视图
//    @Override
//    public View initView() {
//      View view=View.inflate(context, R.layout.fragment_audiopicture,null);
// //       View view=View.inflate(context, R.layout.activity_audio_player,null);
//        fragment_listview= (ListView) view.findViewById(R.id.fragment_listview);
//        fragment_tv_nomedia= (TextView)view.findViewById(R.id.fragment_tv_nomedia);
//
//        fragment_listview.setOnItemClickListener(new MyOnItemClickListener());
//
//        return view;
//    }
//
//    class MyOnItemClickListener implements AdapterView.OnItemClickListener{
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            MediaItem mediaItem=listMediaItems.get(position);
//
//            //不用传列表了，因为马上通过服务来传了
//            Intent intent=new Intent(context,AudioPlayerActivity.class);
//            // intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*"); 这里只是传一个，改为：
////            Bundle bundle=new Bundle();
////            bundle.putSerializable("medialist",listMediaItems);
////            intent.putExtras(bundle);
//            intent.putExtra("position",position);
//
//            context.startActivity(intent);
//
//
//
//
//        }
//    }
//
//    @Override
//    public void initData() {
//        super.initData();
//
//        getData();
//
//    }
//
////    int[] listImageIDs={R.drawable.iloveu1,R.drawable.school2,R.drawable.bestus3,R.drawable.hope4,
////            R.drawable.iloveu1,R.drawable.school2,R.drawable.bestus3,R.drawable.hope4};
//
////    String[] tv_sys1s={"我喜欢你","校园","与你相知","如果可以","我喜欢你","校园","与你相知","如果可以","我喜欢你","校园","与你相知","如果可以"};//////////
////    String[] tv_sys2s={"不是一时兴起","回忆","是我幸运","我希望","不是一时兴起","回忆","是我幸运","我希望","不是一时兴起","回忆","是我幸运","我希望"};///////////
////    String[] tv_sys3s={"那是我藏在心底的秘密","感谢在那里与你相遇","你的出现让我变得更好","这可以是我们的结局","那是我藏在心底的秘密","感谢在那里与你相遇","你的出现让我变得更好","这可以是我们的结局","那是我藏在心底的秘密","感谢在那里与你相遇","你的出现让我变得更好","这可以是我们的结局"};///////////
////
//
////    int i=0;
//    private void getData() {
//        //因为加载视频是耗时的所以在子线程中进行
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                listMediaItems=new ArrayList<MediaItem>();
//                Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                String[] objs=new String[]{MediaStore.Video.Media.DISPLAY_NAME, //0
//                        MediaStore.Audio.Media.DURATION,  //1
//                        MediaStore.Audio.Media.SIZE, //2
//                        MediaStore.Audio.Media.ARTIST, //3
//                        MediaStore.Audio.Media.DATA //4
//                };  //5
//
//
//
//                ContentResolver resolver=context.getContentResolver();
//                Cursor cursor=resolver.query(uri, objs, null, null, null);
//                if (cursor!=null){
//                    while (cursor.moveToNext()){
//                        //获取的五个字段都是同一个视频中的，所以要去创建一个MediaItem类
//                        String display_name=cursor.getString(0);
//                        Long duration=cursor.getLong(1);
//                        Long size=cursor.getLong(2);
//                        String artist=cursor.getString(3);
//                        String data=cursor.getString(4);
//                        MediaItem mediaItem=new MediaItem(display_name,duration,size,artist,data);
//                        listMediaItems.add(mediaItem);
//                    }
//                }
//                cursor.close();
//
//                //要子线程获取数据完毕，才能发消息，否则可能数据还没获取完就发消息了，则此时就进行处理，得不到数据
//                handler.sendEmptyMessage(0);
//            }
//        }.start();
//
//    }
//
//
//
//    class MyAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return listMediaItems.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return listMediaItems.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if(convertView==null) {
//                convertView=View.inflate(context,R.layout.item_audiopicture_fragment,null);
//                viewHolder=new ViewHolder();
////                viewHolder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
//                viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
//                viewHolder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
//                viewHolder.tv_size= (TextView) convertView.findViewById(R.id.tv_size);
//                convertView.setTag(viewHolder);
//            }else{
//                viewHolder= (ViewHolder) convertView.getTag();
//            }
//
//            MediaItem mediaItem=listMediaItems.get(position);
//            viewHolder.tv_name.setText(mediaItem.getDisplay_name());
//            viewHolder.tv_time.setText(utils.stringForTime((int) mediaItem.getDuration()));
//            viewHolder.tv_size.setText(Formatter.formatFileSize(context, mediaItem.getSize()));
////            viewHolder.iv_icon.setBackgroundResource(mediaItem.getImageID());
////            viewHolder.tv_sys1.setText(mediaItem.getTv_sys1());
////            viewHolder.tv_sys2.setText(mediaItem.getTv_sys2());
////            viewHolder.tv_sys3.setText(mediaItem.getTv_sys3());
//
//            return convertView;
//        }
//    }
//
//    static class ViewHolder{
////        private ImageView iv_icon;
//        private TextView tv_name;
//        private TextView tv_time;
//        private TextView tv_size;
////        private TextView tv_sys1;
////        private TextView tv_sys2;
////        private TextView tv_sys3;
//
//    }
//    Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if(listMediaItems!=null && listMediaItems.size()>0) {
//                //有信息
//                fragment_tv_nomedia.setVisibility(View.GONE);
//                //加载适配器
//                adapter=new MyAdapter();
//                fragment_listview.setAdapter(adapter);
//            }else{
//                //没有信息
//                fragment_tv_nomedia.setVisibility(View.VISIBLE);
//            }
//        }
//    };
//
//}
