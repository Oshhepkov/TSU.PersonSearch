package com.github.oshhepkov.app;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class CommonDiffUtil extends DiffUtil.Callback {

    private final List<CommonItem> oldList;
    private final List<CommonItem> newList;

    CommonDiffUtil(List<CommonItem> oldList, List<CommonItem> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).isTheSame(newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).isContentTheSame(newList.get(newItemPosition));
    }
}
