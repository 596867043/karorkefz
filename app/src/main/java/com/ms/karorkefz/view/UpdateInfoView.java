package com.ms.karorkefz.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ms.karorkefz.util.DpUtil;
import com.ms.karorkefz.util.StyleUtil;
import com.ms.karorkefz.util.UrlUtil;


/**
 * Created by Jason on 2017/9/9.
 */

public class UpdateInfoView extends DialogFrameLayout {

    private String mTitle;
    private TextView mContentTextView;

    public UpdateInfoView(@NonNull Context context) {
        super( context );
        initView( context );
    }

    public UpdateInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
        initView( context );
    }

    public UpdateInfoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        initView( context );
    }

    private void initView(Context context) {

        TextView contentTextView = new TextView( context );
        contentTextView.setClickable( true );
        contentTextView.setAutoLinkMask( Linkify.WEB_URLS );
        contentTextView.setTextSize( TypedValue.COMPLEX_UNIT_DIP, StyleUtil.TEXT_SIZE_SMALL );
        contentTextView.setTextColor( StyleUtil.TEXT_COLOR_DEFAULT );

        int defHPadding = DpUtil.dip2px( context, 20 );
        contentTextView.setPadding( defHPadding, 0, defHPadding, 0 );
        contentTextView.setMovementMethod( LinkMovementMethod.getInstance() );
        this.addView( contentTextView, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );

        withNeutralButtonText( "跳过这个版本" );
        withNegativeButtonText( "取消" );
        withPositiveButtonText( "前往更新页" );

        mContentTextView = contentTextView;
    }

    public void setContent(String text) {
        text = text.replaceAll( "(((https|http|ftp|rtsp|mms):\\/\\/)[^\\s]+)", "<a href=\"$1\">$1</a>" );
        CharSequence span = getClickableHtml( text );
        mContentTextView.setText( span );
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String getDialogTitle() {
        return mTitle;
    }

    private void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder, final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart( urlSpan );
        int end = clickableHtmlBuilder.getSpanEnd( urlSpan );
        int flags = clickableHtmlBuilder.getSpanFlags( urlSpan );
        ClickableSpan clickableSpan = new ClickableSpan() {
            public void onClick(View view) {
                UrlUtil.openUrl( view.getContext(), urlSpan.getURL() );
            }
        };
        clickableHtmlBuilder.setSpan( clickableSpan, start, end, flags );
    }

    private CharSequence getClickableHtml(String html) {
        html = html.replace( "\n", "<br/>" );
        Spanned spannedHtml = Html.fromHtml( html );
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder( spannedHtml );
        URLSpan[] urls = clickableHtmlBuilder.getSpans( 0, spannedHtml.length(), URLSpan.class );
        for (final URLSpan span : urls) {
            setLinkClickable( clickableHtmlBuilder, span );
        }
        return clickableHtmlBuilder;
    }

    /**
     * This method will be invoked when the dialog is dismissed.
     *
     * @param dialog the dialog that was dismissed will be passed into the
     *               method
     */
    @Override
    public void onDismiss(DialogInterface dialog) {

    }
}
