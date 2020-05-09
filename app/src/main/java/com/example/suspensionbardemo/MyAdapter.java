package com.example.suspensionbardemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        ImageLoader.getInstance().displayImage(getAvatarResUrl(i),myHolder.mIvAvatar);
        ImageLoader.getInstance().displayImage(getContentResUrl(i),myHolder.mIvContent);
        //nickname
        myHolder.mTvNickname.setText("NetEase " + i);
    }

    public static String getAvatarResUrl(int position) {
        switch (position % 4) {
            case 0:
                return "https://wx2.sinaimg.cn/mw600/006w0rUGgy3gem4sgqp8ej316o1kwh2g.jpg";
            case 1:
                return "https://wx3.sinaimg.cn/mw600/005PtzOXgy1gem3wz2kl6j30u011e7cf.jpg";
            case 2:
                return "https://wx3.sinaimg.cn/mw600/005Ojl6Vly1gem4nizjbpj31ds0n0qv8.jpg";
            case 3:
                return "https://wx1.sinaimg.cn/mw600/005GDmlJly1gem4j7cuqxj32c0340h7r.jpg";
        }
        return "https://wx3.sinaimg.cn/mw600/005tDt12gy1gem4kv1yh2j32872eonpe.jpg";
    }

    public static String getContentResUrl(int position) {
        switch (position % 4) {
            case 0:
                return "https://wanandroid.com/blogimgs/9b9a4c7d-00d3-4e20-8f81-685467336de1.png";
            case 1:
                return "https://ww3.sinaimg.cn/orj360/006RYJvjgy1gefpjd2utbj32tc240x6s.jpg";
            case 2:
                return "https://ww1.sinaimg.cn/bmiddle/69002366gy1gelzbneth1j20u0190tp8.jpg";
            case 3:
                return "https://ww4.sinaimg.cn/bmiddle/8446b893gy1g71f2of95aj20j60njadg.jpg";
        }
        return "https://wx4.sinaimg.cn/mw600/6f561a45gy1gem3z1uz6lj21400u07gj.jpg";
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView mIvAvatar;
        ImageView mIvContent;
        TextView mTvNickname;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mIvAvatar = itemView.findViewById(R.id.iv_avatar);
            mIvContent = itemView.findViewById(R.id.iv_content);
            mTvNickname = itemView.findViewById(R.id.tv_nickname);
        }
    }

}
