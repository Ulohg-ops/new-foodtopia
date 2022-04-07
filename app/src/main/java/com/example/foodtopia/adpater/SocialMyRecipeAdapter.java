package com.example.foodtopia.adpater;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodtopia.model.Post;
import com.example.foodtopia.R;
import com.example.foodtopia.account.SavedItemActivity;

import java.util.ArrayList;
import java.util.List;

public class SocialMyRecipeAdapter extends RecyclerView.Adapter<SocialMyRecipeAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    public SocialMyRecipeAdapter(Context context, List<Post> posts){
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public SocialMyRecipeAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_social_fotos, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SocialMyRecipeAdapter.ImageViewHolder holder, final int position) {

        final Post post = mPosts.get(position);

        Glide.with(mContext).load(post.getPostimage()).into(holder.post_image);

        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, SavedItemActivity.class);
                intent.putExtra("postid",post.getPostid());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView post_image;


        public ImageViewHolder(View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.post_image);

        }
    }

    public static class VPAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
        private final ArrayList<String> fragmentTitle=new ArrayList<>();

        public VPAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }
        public void addFragment(Fragment fragment,String title){
            fragmentArrayList.add(fragment);
            fragmentTitle.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}