package com.frostox.calculo.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import calculo.frostox.com.calculo.R;

/**
 * Created by admin on 17/04/2016.
 */
public class Resultadapter extends RecyclerView.Adapter<Resultadapter.MyViewHolder> {

    private List<ResultData> rvadapter;
    private Context context;

    public Resultadapter(Context context, List<ResultData> List) {
        this.context = context;
        this.rvadapter = List;
    }

    @Override
    public int getItemCount() {
        return rvadapter.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder ViewHolder, int i) {
        ResultData resultData = rvadapter.get(i);
        Picasso.with(context).load(resultData.imgqnurl).into(ViewHolder.imgquestion);
        Picasso.with(context).load(resultData.expurl).into(ViewHolder.imgexplanation);
        Picasso.with(context).load(resultData.imgansurl).into(ViewHolder.imganswer);
        ViewHolder.ct.setImageResource(resultData.ct);
        ViewHolder.questionnumber.setText(resultData.qno);
        ViewHolder.question.setText(resultData.qn);
        ViewHolder.answer.setText(resultData.ans);
        ViewHolder.explanation.setText(resultData.exp);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.resultitem, viewGroup, false);
        Log.d("KP", "Inside Create");
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView imgquestion;
        protected ImageView imgexplanation;
        protected ImageView imganswer;
        protected ImageView ct;
        protected TextView questionnumber;
        protected TextView question;
        protected TextView answer;
        protected TextView explanation;


        public MyViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            ct = (ImageView)v.findViewById(R.id.ct);
            imgexplanation = (ImageView) v.findViewById(R.id.imgexplanation);
            imganswer= (ImageView) v.findViewById(R.id.imganswer);
            imgquestion = (ImageView) v.findViewById(R.id.imgquestion);
            questionnumber = (TextView) v.findViewById(R.id.questionnumber);
            question = (TextView) v.findViewById(R.id.question);
            answer = (TextView) v.findViewById(R.id.answer);
            explanation = (TextView) v.findViewById(R.id.explanation);
        }

        @Override
        public void onClick(View v) {
            context = v.getContext();
        }
    }
}
