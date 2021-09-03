package com.simon.vpohode;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.simon.vpohode.assync_tasks.TakeImageFromStorage;
import com.simon.vpohode.screens.ConfigItem;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PageFragment extends Fragment {

    private int pageNumber;

    public static PageFragment newInstance(int page) {
        PageFragment fragment = new PageFragment();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public PageFragment() {
        // default implementation
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_page, container, false);

        CardView[] cardViews = {result.findViewById(R.id.colorCard1),
                result.findViewById(R.id.colorCard2),
                result.findViewById(R.id.colorCard3),
                result.findViewById(R.id.colorCard4),
                result.findViewById(R.id.colorCard5),
                result.findViewById(R.id.colorCard6)};
        ImageView[] imageViews = {result.findViewById(R.id.colorView1),
                result.findViewById(R.id.colorView2),
                result.findViewById(R.id.colorView3),
                result.findViewById(R.id.colorView4),
                result.findViewById(R.id.colorView5),
                result.findViewById(R.id.colorView6)};
        Item[] items = MyAdapter.getLooks().get(pageNumber);
        LinearLayout rightLayout = result.findViewById(R.id.right_layout);
        LinearLayout leftLayout = result.findViewById(R.id.left_layout);
        View relativeLayout;
        ImageView photo;
        TextView name;
        TextView style;
        TextView brand;
        TextView usedItem;

        for(int i = 0 ; i < items.length; i++){
            final int id = i;
            relativeLayout = inflater.inflate(R.layout.list_item,container,false);
            relativeLayout.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ConfigItem.class);
                long itemId = MyAdapter.getLooks().get(pageNumber)[id].getId();
                intent.putExtra("id", itemId);
                inflater.getContext().startActivity(intent);
            });
            photo = relativeLayout.findViewById(R.id.imageViewPhoto);

            if(items[i].getFoto() != null){
                File fileFoto = new File(items[i].getFoto());
                Picasso.get().load(fileFoto).into(photo);
            }

            name = relativeLayout.findViewById(R.id.nameItem);
            name.setText(items[i].getName());

            brand = relativeLayout.findViewById(R.id.brandItem);
            brand.setText(items[i].getBrand());

            style = relativeLayout.findViewById(R.id.styleItem);
            String styleText = getResources().getString(R.string.style) + ": " + getResources().getString(items[i].getStyle());
            style.setText(styleText);

            usedItem = relativeLayout.findViewById(R.id.usedItem);
            String usedText = getResources().getString(R.string.used_times )+ ": " + items[i].getUsed();
            usedItem.setText(usedText);

            if((i%2) == 0){
                leftLayout.addView(relativeLayout);
            }else{
                rightLayout.addView(relativeLayout);
            }
            cardViews[i].setAlpha(1);
            cardViews[i].setCardElevation(5);
            imageViews[i].setBackgroundColor(items[i].getColor());
        }
        return result;
    }
}
