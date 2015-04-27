/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.simon.material.SubjectList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simon.material.R;
import com.example.simon.material.Utility.MLRoundedImageView;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

public class MyExpandableItemAdapter
        extends AbstractExpandableItemAdapter<MyExpandableItemAdapter.MyGroupViewHolder, MyExpandableItemAdapter.MyChildViewHolder> {
    private static final String TAG = "MyExpandableItemAdapter";

    private AbstractExpandableDataProvider mProvider;

    public static abstract class MyBaseViewHolder extends AbstractExpandableItemViewHolder {
        public ViewGroup mContainer;
        public View mDragHandle;
        public TextView mTextView;

        public MyBaseViewHolder(View v) {
            super(v);
            mContainer = (ViewGroup) v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mTextView = (TextView) v.findViewById(android.R.id.text1);

            // hide the drag handle
            mDragHandle.setVisibility(View.GONE);
        }
    }

    public static class MyGroupViewHolder extends MyBaseViewHolder {
        MLRoundedImageView subjectround;
        public MyGroupViewHolder(View v) {
            super(v);
            subjectround = (MLRoundedImageView) v.findViewById(R.id.subjectround);
        }
    }

    public static class MyChildViewHolder extends MyBaseViewHolder {
        public MyChildViewHolder(View v) {
            super(v);
        }
    }

    public MyExpandableItemAdapter(AbstractExpandableDataProvider dataProvider) {
        mProvider = dataProvider;

        // ExpandableItemAdapter requires stable ID, and also
        // have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
    }

    @Override
    public int getGroupCount() {
        return mProvider.getGroupCount();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mProvider.getChildCount(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mProvider.getGroupItem(groupPosition).getGroupId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mProvider.getChildItem(groupPosition, childPosition).getChildId();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.frayment_recyclerview_groupitems, parent, false);
        return new MyGroupViewHolder(v);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.fragment_recyclerview_subitem, parent, false);
        return new MyChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(MyGroupViewHolder holder, int groupPosition, int viewType) {
        // child item
        final AbstractExpandableDataProvider.BaseData item = mProvider.getGroupItem(groupPosition);

        // set text
        holder.mTextView.setText(item.getText());
        Bitmap bm = MLRoundedImageView.getCroppedBitmap(BitmapFactory.decodeResource(holder.subjectround.getContext().getResources(), R.drawable.uct),200);
        holder.subjectround.setImageBitmap(bm);
        // mark as clickable
        holder.itemView.setClickable(true);

       // set background resource (target view ID: container)
        final int expandState = holder.getExpandStateFlags();

        if ((expandState & RecyclerViewExpandableItemManager.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;

            if ((expandState & RecyclerViewExpandableItemManager.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.color.accent;
            } else {
                bgResId = R.color.grey;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }
    }

    @Override
    public void onBindChildViewHolder(MyChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        // group item
        final AbstractExpandableDataProvider.ChildData item = mProvider.getChildItem(groupPosition, childPosition);

        // set text
        holder.mTextView.setText(item.getText());

/*        // set background resource (target view ID: container)
        int bgResId;
        bgResId = R.color.primary;
        holder.mContainer.setBackgroundResource(bgResId);*/
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {

        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }

        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return !hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    public static boolean hitTest(View v, int x, int y) {
        final int tx = (int) (ViewCompat.getTranslationX(v) + 0.5f);
        final int ty = (int) (ViewCompat.getTranslationY(v) + 0.5f);
        final int left = v.getLeft() + tx;
        final int right = v.getRight() + tx;
        final int top = v.getTop() + ty;
        final int bottom = v.getBottom() + ty;

        return (x >= left) && (x <= right) && (y >= top) && (y <= bottom);
    }
}
