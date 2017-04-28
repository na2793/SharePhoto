package com.study.hancom.sharephoto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.study.hancom.sharephoto.R;
import com.study.hancom.sharephoto.adapter.base.RecyclerClickableItemAdapter;
import com.study.hancom.sharephoto.model.Album;
import com.study.hancom.sharephoto.model.Page;
import com.study.hancom.sharephoto.model.Picture;
import com.study.hancom.sharephoto.util.WebViewUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PageListAdapter extends RecyclerClickableItemAdapter<PageListAdapter.ViewHolder> {
    private Context mContext;
    private Album mAlbum;
    private boolean mHorizontal;

    private Set<View> mWebViewSet = new HashSet<>();

    public PageListAdapter(Context context, Album album) {
        this(context, album, false);
    }

    public PageListAdapter(Context context, Album album, boolean horizontal) {
        mContext = context;
        mAlbum = album;
        mHorizontal = horizontal;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mAlbum.getPageCount();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHorizontal) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.album_editor_horizontal_page_list_item, parent, false);
            return new ViewHolder(view);
        } else {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.album_editor_page_list_item, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        holder.textView.setText(Integer.toString(position + 1));

        holder.webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performItemClick(holder.itemView, position);
            }
        });

        // Add a WebViewClient
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

    private void injectAll(int position, WebView view) {
        Page page = mAlbum.getPage(position);
        int pictureCount = page.getPictureCount();
        ArrayList<Picture> pictureList = new ArrayList<>();

        for (int i = 0; i < pictureCount; i++) {
            pictureList.add(page.getPicture(i));
        }

        WebViewUtil.injectStyleByScript(view, page.getLayout().getPath());
        WebViewUtil.injectPictureElementByScript(view, pictureList);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        WebView webView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.page_list_item_text);
            webView = (WebView) itemView.findViewById(R.id.page_list_item_webview);
            webView.loadDataWithBaseURL("file:///android_asset/", WebViewUtil.getDefaultHTMLData(mContext), "text/html", "UTF-8", null);
        }
    }
}