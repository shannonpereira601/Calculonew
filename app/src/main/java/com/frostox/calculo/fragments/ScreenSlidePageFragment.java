/*
 * Copyright 2012 The Android Open Source Project
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

package com.frostox.calculo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import calculo.frostox.com.calculo.R;


public class ScreenSlidePageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    public static final String ARG_NOTEID = "note_id";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    private String noteid;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber, String id) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putString(ARG_NOTEID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        noteid = getArguments().getString(ARG_NOTEID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        WebView webView = (WebView)rootView.findViewById(R.id.web);

        webView.loadUrl("https://www.frostox.com/extraclass/uploads/"+noteid);
        Log.d("onn1CreateViewfrag", "https://www.frostox.com/extraclass/uploads/"+noteid);

        switch (getPageNumber()){
            case 0:
                ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                        getString(R.string.title_template_step, mPageNumber + 1) + getString(R.string.macromol));

                ((TextView) rootView.findViewById(R.id.text2)).setText(
                        getString(R.string.macromolans));


                break;

            case 1:
                ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                        getString(R.string.title_template_step, mPageNumber + 1) + getString(R.string.metabolism));

                ((TextView) rootView.findViewById(R.id.text2)).setText(
                        getString(R.string.metabolismans));
                break;

            case 2:
                ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                        getString(R.string.title_template_step, mPageNumber + 1) + getString(R.string.short_note_cellular_pool));

                ((TextView) rootView.findViewById(R.id.text2)).setText(
                        getString(R.string.short_note_cellular_poolans));
                break;
            case 3:
                ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                        getString(R.string.title_template_step, mPageNumber + 1) + getString(R.string.cellular_pool));

                ((TextView) rootView.findViewById(R.id.text2)).setText(
                        getString(R.string.cellular_poolans));
                break;

            case 4:
                ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                        getString(R.string.title_template_step, mPageNumber + 1) + getString(R.string.cell));

                ((TextView) rootView.findViewById(R.id.text2)).setText(
                        getString(R.string.cellans));
                break;
        }


        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
