/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.simon.material.ThreadPop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simon.material.Model.PostType;
import com.example.simon.material.Model.WrappedPost;
import com.example.simon.material.R;

import java.util.ArrayList;

public class SimpleHeaderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_TEXT = 1;
    private static final int VIEW_TYPE_PIC = 2;

    private LayoutInflater mInflater;
    private ArrayList<WrappedPost> mPosts;
    private View mHeaderView;

    public SimpleHeaderRecyclerAdapter(Context context, ArrayList<WrappedPost> posts, View headerView) {
        mInflater = LayoutInflater.from(context);
        mPosts = posts;
        mHeaderView = headerView;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return mPosts.size();
        } else {
            return mPosts.size() + 1;
        }
    }

    //This is not really business logic and will have to be changed later...
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else if (mPosts.get(position-1).getPostType() == PostType.POSTTEXT) { //I think i have to subtract one here to get to compensate for the header
            return VIEW_TYPE_TEXT;
        } else if (mPosts.get(position-1).getPostType() == PostType.POSTPIC) {
            return VIEW_TYPE_PIC;
        } else //for now, we default to TextViewHolder
        return VIEW_TYPE_TEXT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(mHeaderView);
            case VIEW_TYPE_TEXT:
               return new TextViewHolder(mInflater.inflate(R.layout.recyclerview_threadpop_text, parent, false));

            case VIEW_TYPE_PIC:
                return new PicViewHolder(mInflater.inflate(R.layout.recyclerview_threadpop_pic, parent, false));
            default: //for now, we default to the TextViewHolder
                return new TextViewHolder(mInflater.inflate(R.layout.recyclerview_threadpop_text, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof TextViewHolder) {
            ((TextViewHolder) viewHolder).textView.setText(mPosts.get(position - 1).getPost());
           // ((TextViewHolder) viewHolder).textView.setText(mPosts.get(position - 1).getPost());

        } else if (viewHolder instanceof PicViewHolder) {
            ((PicViewHolder) viewHolder).textView.setText(mPosts.get(position - 1).getPost());
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public TextViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.postheader);

        }
    }

    static class PicViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public PicViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.postheader);
        }
    }
}
