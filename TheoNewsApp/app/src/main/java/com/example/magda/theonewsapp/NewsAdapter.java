package com.example.magda.theonewsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Magda on 27.06.2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    private List<News> newsList;


    public NewsAdapter(Context context, List<News> newsList) {
        super(context, -1);
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.news_list_item, parent, false);
            //create a new instance of a @ViewHolder every time we inflate a list item
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);


        }else {
            //ViewHolder is already inflated.
            viewHolder = (ViewHolder) convertView.getTag();
        }
        News news = newsList.get(position);
        viewHolder.sectionHolder.setText(news.getSectionName());
        viewHolder.titleHolder.setText(news.getTitle());

        return convertView;
    }
    @Override
    public int getCount() {
        return this.newsList.size();
    }

    class ViewHolder {
        private TextView sectionHolder;
        private TextView titleHolder;

        public ViewHolder(@NonNull View view) {
            this.sectionHolder = (TextView)view
                    .findViewById(R.id.section_name);
            this.titleHolder = (TextView)view
                    .findViewById(R.id.news_title);
        }
    }
}

