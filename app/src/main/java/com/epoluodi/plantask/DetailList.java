package com.epoluodi.plantask;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import WebService.Webservice;

public class DetailList extends Activity {


    ViewPager viewPager;
    Button btnrefresh;
    TextView title,dtbh;
    String id;
    Thread thread;
    Boolean IsRun=false;
    List<View> viewList;
    int viewpager;
    List<List<Map<String,String>>> lists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        btnrefresh = (Button)findViewById(R.id.btnrefresh);

        title = (TextView)findViewById(R.id.title);
        dtbh = (TextView)findViewById(R.id.dtbh);
        id = getIntent().getStringExtra("id");
        title.setText(getIntent().getStringExtra("title"));
        dtbh .setText("");
        viewpager=0;
        lists = new ArrayList<List<Map<String, String>>>();
        viewList = new ArrayList<View>();
        thread= new Thread(runnable);
        thread.start();
    }


    View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!IsRun)
            {
                thread= new Thread(runnable);
                thread.start();
            }
        }
    };

    Runnable runnable =new Runnable() {
        @Override
        public void run() {

            Webservice webservice = new Webservice(Common.ServerWCF,15000);
            PropertyInfo[] propertyInfos=new PropertyInfo[1];
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName("planid");
            propertyInfo.setValue(id);
            propertyInfos[0] = propertyInfo;
            IsRun = true;
            String r = webservice.PDA_GetInterFaceForStringNew(propertyInfos,"A_PDA_getplantaskDetail");
            Log.i("结果",r);
            if (r.equals("-1"))
            {
                handler.sendEmptyMessage(-1);
                return;
            }

            Message message = handler.obtainMessage();
            message.what=1;
            message.obj=r;
            handler.sendMessage(message);
        }
    };

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            IsRun=false;
            switch (msg.what)
            {
                case -1:
                    Toast.makeText(DetailList.this,"刷新失败，请重试",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(DetailList.this,"成功获取数据",Toast.LENGTH_SHORT).show();

                    lists.clear();
                    viewList.clear();
                    MakeJosn(msg.obj.toString());
                    initviewpaher();



                    break;
            }
        }
    };



    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return viewList.size();
        }



        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }


        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "";
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);


        }

    };



    void MakeJosn(String json)
    {
        List<Map<String,String>> mapList ;
        Map<String,String> map;
        try
        {

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("plantasklist");
            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                mapList = new ArrayList<Map<String, String>>();
                map = new HashMap<String, String>();
                map.put("jynum",jsonObject1.getString("jynum"));
                map.put("dtbh",jsonObject1.getString("dtbh"));

                map.put("11",jsonObject1.getString("11"));
                map.put("12",jsonObject1.getString("12"));

                map.put("21",jsonObject1.getString("21"));
                map.put("22",jsonObject1.getString("22"));

                map.put("31",jsonObject1.getString("31"));
                map.put("32",jsonObject1.getString("32"));

                map.put("41",jsonObject1.getString("41"));
                map.put("42",jsonObject1.getString("42"));

                map.put("51",jsonObject1.getString("51"));
                map.put("52",jsonObject1.getString("52"));

                map.put("61",jsonObject1.getString("61"));
                map.put("62",jsonObject1.getString("62"));

                map.put("71",jsonObject1.getString("71"));
                map.put("72",jsonObject1.getString("72"));

                map.put("81",jsonObject1.getString("81"));
                map.put("82",jsonObject1.getString("82"));

                map.put("91",jsonObject1.getString("91"));
                map.put("92",jsonObject1.getString("92"));

                map.put("101",jsonObject1.getString("101"));
                map.put("102",jsonObject1.getString("102"));

                map.put("111",jsonObject1.getString("111"));
                map.put("112",jsonObject1.getString("112"));

                map.put("121",jsonObject1.getString("121"));
                map.put("122",jsonObject1.getString("122"));
                mapList.add(map);
                lists.add(mapList);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    void  initviewpaher()
    {
        View view ;
        ListView listView;
        MyAdapter myAdapter;
        for (int i = 0; i<lists.size();i++)
        {
            view = getLayoutInflater().inflate(R.layout.viewpager,null);
            listView = (ListView)view.findViewById(R.id.list);
            myAdapter = new MyAdapter(this,lists.get(i));
            listView.setAdapter(myAdapter);
            viewList.add(view);
            Map<String,String> map = lists.get(i).get(0);
            dtbh.setText(map.get("dtbh"));
        }







        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(onPageChangeListener);
    }



    ViewPager.OnPageChangeListener onPageChangeListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            viewpager=i;
            Map<String,String> map = lists.get(i).get(0);
            dtbh.setText(map.get("dtbh"));


        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
    class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        List<Map<String,String>> mapList;
        TextView title;

        ImageView imageView1;
        ImageView imageView2;

        public MyAdapter(Context context,List<Map<String,String>> mapList1) {
            mInflater = LayoutInflater.from(context);
            mapList =mapList1;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            return 12;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return mapList.get(arg0);

        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {


            view = mInflater.inflate(R.layout.istview, null);

            title = (TextView) view.findViewById(R.id.title);

            imageView1 =(ImageView)view.findViewById(R.id.img1);
            imageView2 =(ImageView)view.findViewById(R.id.img2);

            Map<String,String> map;
            switch (i)
            {
                case 0:
                    map = mapList.get(0);
                    title.setText("一月");
                    if (map.get("11").equals("1"))
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.error));
                    if (map.get("12").equals("1"))
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.error));

                    break;
                case 1:
                    map = mapList.get(0);
                    title.setText("二月");
                    if (map.get("21").equals("1"))
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.error));
                    if (map.get("22").equals("1"))
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.error));

                    break;
                case 2:
                    map = mapList.get(0);
                    title.setText("三月");
                    if (map.get("31").equals("1"))
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.error));
                    if (map.get("32").equals("1"))
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.error));

                    break;
                case 3:
                    map = mapList.get(0);
                    title.setText("四月");
                    if (map.get("41").equals("1"))
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.error));
                    if (map.get("42").equals("1"))
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.error));

                    break;
                case 4:
                    map = mapList.get(0);
                    title.setText("五月");
                    if (map.get("51").equals("1"))
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.error));
                    if (map.get("52").equals("1"))
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.error));

                    break;
                case 5:
                    map = mapList.get(0);
                    title.setText("六月");
                    if (map.get("61").equals("1"))
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.error));
                    if (map.get("62").equals("1"))
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.error));

                    break;
                case 6:
                    map = mapList.get(0);
                    title.setText("七月");
                    if (map.get("71").equals("1"))
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.error));
                    if (map.get("72").equals("1"))
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.error));

                    break;
                case 7:
                    map = mapList.get(0);
                    title.setText("八月");
                    if (map.get("81").equals("1"))
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.error));
                    if (map.get("82").equals("1"))
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.error));

                    break;
                case 8:
                    map = mapList.get(0);
                    title.setText("九月");
                    if (map.get("91").equals("1"))
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.error));
                    if (map.get("92").equals("1"))
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.error));

                    break;
                case 9:
                    map = mapList.get(0);
                    title.setText("十月");
                    if (map.get("101").equals("1"))
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.error));
                    if (map.get("102").equals("1"))
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.error));

                    break;
                case 10:
                    map = mapList.get(0);
                    title.setText("十一月");
                    if (map.get("111").equals("1"))
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.error));
                    if (map.get("112").equals("1"))
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.error));

                    break;
                case 11:
                    map = mapList.get(0);
                    title.setText("十二月");
                    if (map.get("121").equals("1"))
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView1.setBackground(getResources().getDrawable(R.mipmap.error));
                    if (map.get("122").equals("1"))
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.ok));
                    else
                        imageView2.setBackground(getResources().getDrawable(R.mipmap.error));

                    break;
            }

            return view;
        }
    }





}
