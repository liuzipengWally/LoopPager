package lzp.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzp.looppager.LoopPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LoopPager mLoopPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoopPager = (LoopPager) findViewById(R.id.loopPager);
        List<String> urls = new ArrayList<>();
        urls.add("http://i0.hdslb.com/video/e0/e0814751cf26c9b319ed974d7f3d5f67.jpg");
        urls.add("http://img4.imgtn.bdimg.com/it/u=3252822932,941775074&fm=21&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=3547247058,1962251339&fm=21&gp=0.jpg");
        urls.add("http://static.vgtime.com/article/web/150515191332685.jpg");
        mLoopPager.setRemoteImageUrls(urls);
        mLoopPager.setOnClickListener(new LoopPager.OnClickListener() {
            @Override
            public void onClick(int position, ImageView imageView) {

            }

            @Override
            public void longClick(int position, ImageView imageView) {

            }
        });

        mLoopPager.setPagerChangeListener(new LoopPager.PagerChangeListener() {
            @Override
            public void pagerChange(int position) {

            }
        });
    }
}
