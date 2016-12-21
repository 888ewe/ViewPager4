package activity.xiaobao.com.viewpager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private List<ImageView> list;
    private int prePosition = 0;
    private String[] imageDescArrs;
    private boolean isSwitchPager = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        initViewpagerData();
        viewpager.setAdapter(new Adapter());

        //设置当前viewpager要显示第几个条目
        int item = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % list.size());
        viewpager.setCurrentItem(item);

        //把第一个小圆点设置为白色，显示第一个textview内容
        ll_point_group.getChildAt(prePosition).setEnabled(true);
        tv_title.setText(imageDescArrs[prePosition]);
        //设置viewpager滑动的监听事件
        viewpager.addOnPageChangeListener(this);

        //实现自动切换的功能
        new Thread() {
            public void run() {
                while (!isSwitchPager) {
                    SystemClock.sleep(3000);
                    //拿着我们创建的handler 发消息
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    private void initViewpagerData() {

        imageDescArrs = new String[]{"标题1", "标题2", "标题3", "标题4", "标题5"};
        list = new ArrayList<ImageView>();
        int imageId[] = new int[]{R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4};
        ImageView iv;
        View dotView;

        for (int i = 0; i < imageId.length; i++) {
            iv = new ImageView(this);
            iv.setBackgroundResource(imageId[i]);
            list.add(iv);
            //准备小圆点的数据
            dotView = new View(getApplicationContext());
            dotView.setBackgroundResource(R.drawable.select_point);
            //设置小圆点的宽和高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            //设置每个小圆点之间距离
            if (i != 0) {
                params.leftMargin = 15;
            }
            dotView.setLayoutParams(params);
            //设置小圆点默认状态
            dotView.setEnabled(false);
            //把dotview加入到线性布局中
            ll_point_group.addView(dotView);
        }
    }

    @Override
    protected void onDestroy() {
        isSwitchPager = true;
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        int newPosition = position % list.size();
        ll_point_group.getChildAt(newPosition).setEnabled(true);
        ll_point_group.getChildAt(prePosition).setEnabled(false);
        tv_title.setText(imageDescArrs[newPosition]);
        prePosition = newPosition;
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    private class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int newPosition = position % list.size();
            ImageView img = list.get(newPosition);
            container.addView(img);
            return img;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
