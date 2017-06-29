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

    public NewsAdapter(@NonNull Context context, List<News> newsList) {
        super(context, 0, newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the book at the given position in the list of books
        News currentNews = getItem(position);

        // Find the TextView with title
        TextView titleView = (TextView) listItemView.findViewById(R.id.news_title);
        titleView.setText(currentNews.getTitle());
        TextView sectionName = (TextView) listItemView.findViewById((R.id.section_name));
        sectionName.setText(currentNews.getSectionName());
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_TextView);
        String rawDate = currentNews.getWebPublicationDate().replace("T", " ")
                .substring(0, Math.min(currentNews.getWebPublicationDate().length(), 16));

        dateTextView.setText(rawDate);

        return listItemView;
    }
}


