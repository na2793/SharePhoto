package com.study.hancom.sharephoto.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.study.hancom.sharephoto.activity.GalleryFullSizePictureActivity;
import com.study.hancom.sharephoto.adapter.base.GalleryAdapter;

import java.util.ArrayList;

public class SingleSelectionGalleryAdapter extends GalleryAdapter {
    private static final int REQUEST_CODE = 1;

    private int mSelectedPosition = -1;
    private ArrayList<String> mInvalidPicturePathList;

    private OnSingleItemSelectListener mOnSingleItemSelectListener;

    public SingleSelectionGalleryAdapter(Context context, ArrayList<String> picturePathList) {
        super(context, picturePathList);
    }

    @Override
    protected void bindView(ViewHolder holder, final int position) {
        if (mInvalidPicturePathList != null) {
            if (mInvalidPicturePathList.contains(getItem(position))) {
                holder.imageView.setAlpha(0.2f);
                holder.checkBox.setVisibility(View.GONE);
            } else {
                holder.imageView.setAlpha(1.0f);
                holder.checkBox.setVisibility(View.VISIBLE);
                if (mSelectedPosition == position) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
            }
        } else {
            if (mSelectedPosition == position) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mInvalidPicturePathList.contains(getItem(position))) {
                    mOnSingleItemSelectListener.onSelect(position);
                }
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GalleryFullSizePictureActivity.class);
                intent.putStringArrayListExtra("picturePathList", mPicturePathList);
                intent.putStringArrayListExtra("invalidPicturePathList",mInvalidPicturePathList);
                intent.putExtra("currentPictureIndex", position);
                intent.putExtra("isMultipleSelection", false);
                ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    public void setInvalidPicturePathList(ArrayList<String> invalidPicturePathList) {
        mInvalidPicturePathList = invalidPicturePathList;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    public void addSelectedPosition(String path) {
        mInvalidPicturePathList.add(path);
    }

    public void deselectAll() {
        mInvalidPicturePathList.clear();
    }

    public void setOnSingleItemSelectListener(OnSingleItemSelectListener listener) {
        mOnSingleItemSelectListener = listener;
    }

    public interface OnSingleItemSelectListener {
        void onSelect(int position);
    }

}
