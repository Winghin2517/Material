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


import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExpandableDataProvider extends AbstractExpandableDataProvider {
    private List<Pair<GroupData, List<ChildData>>> mData;

    public ExpandableDataProvider() {
        final String[] groupItems = {"School of Accounting", "School of Economics", "School of Law"};
        final String[] childItems = {"First Year", "Second Year", "Third Year"};

        mData = new LinkedList<>();

        for (int i = 0; i < groupItems.length; i++) {
            //noinspection UnnecessaryLocalVariable
            final long groupId = i;
            final String groupText = groupItems[i];
            final ConcreteGroupData group = new ConcreteGroupData(groupId, groupText);
            final List<ChildData> children = new ArrayList<>();

            for (int j = 0; j < childItems.length; j++) {
                final long childId = group.generateNewChildId();
                final String childText = childItems[j];

                children.add(new ConcreteChildData(childId, childText));
            }

            mData.add(new Pair<GroupData, List<ChildData>>(group, children));
        }
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mData.get(groupPosition).second.size();
    }

    @Override
    public GroupData getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        return mData.get(groupPosition).first;
    }

    @Override
    public ChildData getChildItem(int groupPosition, int childPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        final List<ChildData> children = mData.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }

        return children.get(childPosition);
    }


    public static final class ConcreteGroupData extends GroupData {

        private final long mId;
        private final String mText;
        private long mNextChildId;

        ConcreteGroupData(long id, String text) {
            mId = id;
            mText = text;
            mNextChildId = 0;
        }

        @Override
        public long getGroupId() {
            return mId;
        }


        @Override
        public String getText() {
            return mText;
        }


        public long generateNewChildId() {
            final long id = mNextChildId;
            mNextChildId += 1;
            return id;
        }
    }

    public static final class ConcreteChildData extends ChildData {

        private long mId;
        private final String mText;

        ConcreteChildData(long id, String text) {
            mId = id;
            mText = text;
        }

        @Override
        public long getChildId() {
            return mId;
        }

        @Override
        public String getText() {
            return mText;
        }
    }
}
