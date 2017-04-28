package com.study.hancom.sharephoto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.study.hancom.sharephoto.R;
import com.study.hancom.sharephoto.adapter.base.RecyclerClickableItemAdapter;
import com.study.hancom.sharephoto.model.PageLayout;
import com.study.hancom.sharephoto.model.Picture;
import com.study.hancom.sharephoto.util.WebViewUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LayoutGridAdapter extends RecyclerClickableItemAdapter<LayoutGridAdapter.ViewHolder> {
    private Context mContext;

    private List<PageLayout> mPageLayoutList;
    private int mSelectedPosition;

    private Set<View> mWebViewSet = new HashSet<>();

    public LayoutGridAdapter(Context context, List<PageLayout> pageLayoutList) {
        mContext = context;
        mPageLayoutList = pageLayoutList;
        mSelectedPosition = -1;
    }

    public PageLayout getItem(int position) {
        return mPageLayoutList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mPageLayoutList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.album_editor_new_layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (mSelectedPosition == position) {
            holder.webView.setAlpha(0.2f);
        } else {
            holder.webView.setAlpha(1.0f);
        }

        holder.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                injectAll(position, view);
                mWebViewSet.add(view);
            }
        });

        if (mWebViewSet.contains(holder.webView)) {
            injectAll(position, holder.webView);
        }
    }

    public void setPageLayoutList(List<PageLayout> pageLayoutList) {
        mPageLayoutList = pageLayoutList;
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    private boolean injectAll(int position, WebView view) {
        PageLayout pageLayout = mPageLayoutList.get(position);

        int pictureCount = pageLayout.getElementNum();
        ArrayList<Picture> pictureList = new ArrayList<>();

        for (int i = 0; i < pictureCount; i++) {
            pictureList.add(null);
        }

        WebViewUtil.injectStyleByScript(view, pageLayout.getPath());
        WebViewUtil.injectPictureElementByScript(view, pictureList);

        return true;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        WebView webView;

        ViewHolder(View itemView) {
            super(itemView);
            webView = (WebView) itemView.findViewById(R.id.new_layout_grid_item_web_view);
            webView.loadDataWithBaseURL("file:///android_asset/", WebViewUtil.getDefaultHTMLData(mContext), "text/html", "UTF-8", null);

        }
    }
}