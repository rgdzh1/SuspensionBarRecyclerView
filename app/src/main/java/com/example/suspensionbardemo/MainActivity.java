package com.example.suspensionbardemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suspensionbardemo.databinding.ActivityMainBinding;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yey.sbrv.SuspensionBarRecyclerView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        binding.shrv.setUpdateSuspensionBarListener(new SuspensionBarRecyclerView.UpdateSuspensionBarListener() {
            @Override
            public void updateSuspensionBar(View mSuspensionBar, int mFirstVisibleIndex) {
                ImageLoader.getInstance().displayImage(MyAdapter.getAvatarResUrl(mFirstVisibleIndex), ((ImageView) mSuspensionBar.findViewById(R.id.iv_avatar_content)));
                ((TextView) mSuspensionBar.findViewById(R.id.tv_nickname_content)).setText("NetEase " + mFirstVisibleIndex);
            }
        });
        binding.shrv.setAdapter(new MyAdapter());
    }
}
