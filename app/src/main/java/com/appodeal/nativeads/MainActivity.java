package com.appodeal.nativeads;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeAdView;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.NativeMediaView;
import com.appodeal.ads.utils.Log;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Appodeal.setAutoCache(Appodeal.NATIVE, false);
        Appodeal.setTesting(true);
        Appodeal.setLogLevel(Log.LogLevel.verbose);
        Appodeal.initialize(this, "e3fe14f2c38f5f7abfa97c003ab681ebc4926cf12d9bd9b7", Appodeal.NATIVE, true);

        Appodeal.setNativeCallbacks(new NativeCallbacks() {
            @Override
            public void onNativeLoaded() {
                showAd();
            }

            @Override
            public void onNativeFailedToLoad() {

            }

            @Override
            public void onNativeShown(NativeAd nativeAd) {

            }

            @Override
            public void onNativeClicked(NativeAd nativeAd) {

            }

            @Override
            public void onNativeExpired() {

            }
        });

        Appodeal.cache(this, Appodeal.NATIVE, 3);
    }

    private void showAd() {
        List<NativeAd> nativeAds = Appodeal.getNativeAds(1);
        if (nativeAds.size() > 0) {
            ConstraintLayout view = findViewById(R.id.nativeview);
            view.removeAllViews();
            NativeAd nativeAd = nativeAds.get(0);
            NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.include_native_ads, null, false);

            TextView tvTitle = nativeAdView.findViewById(R.id.tv_title);
            tvTitle.setText(nativeAd.getTitle());
            nativeAdView.setTitleView(tvTitle);

            TextView tvDescription = nativeAdView.findViewById(R.id.tv_description);
            tvDescription.setText(nativeAd.getDescription());
            nativeAdView.setDescriptionView(tvDescription);

            Button ctaButton = nativeAdView.findViewById(R.id.b_cta);
            ctaButton.setText(nativeAd.getCallToAction());
            nativeAdView.setCallToActionView(ctaButton);

            NativeMediaView nativeMediaView = nativeAdView.findViewById(R.id.appodeal_media_view_content);
            nativeAdView.setNativeMediaView(nativeMediaView);

            ImageView icon = nativeAdView.findViewById(R.id.icon);
            //TODO: Remove method, do as media
            icon.setImageBitmap(nativeAd.getIcon());
            nativeAdView.setIconView(icon);

            //TODO: Rewrite
            View providerView = nativeAd.getProviderView(this);
            if (providerView != null) {
                //TODO: Why?
                if (providerView.getParent() != null && providerView.getParent() instanceof ViewGroup) {
                    ((ViewGroup) providerView.getParent()).removeView(providerView);
                }
                FrameLayout providerViewContainer = nativeAdView.findViewById(R.id.provider_view);
                //TODO: Review network docs and remove layout
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                providerViewContainer.addView(providerView);
            }
            nativeAdView.setProviderView(providerView);

            RatingBar ratingBar = nativeAdView.findViewById(R.id.rb_rating);
            if (nativeAd.getRating() > 0) {
                ratingBar.setRating(nativeAd.getRating());
                ratingBar.setStepSize(0.1f);
                ratingBar.setVisibility(View.VISIBLE);
            }
            nativeAdView.setRatingView(ratingBar);

            TextView tvAgeRestrictions = nativeAdView.findViewById(R.id.tv_age_restriction);
            if (nativeAd.getAgeRestrictions() != null) {
                tvAgeRestrictions.setText(nativeAd.getAgeRestrictions());
                tvAgeRestrictions.setVisibility(View.VISIBLE);
            }

            nativeAdView.registerView(nativeAd);
            nativeAdView.setVisibility(View.VISIBLE);
            view.addView(nativeAdView);
        }
    }

}
