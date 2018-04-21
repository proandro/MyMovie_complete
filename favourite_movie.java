package com.example.lucky.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class favourite_movie extends AppCompatActivity implements MyAdapter_for_favourite.OnItemClickListener {
    public static final String EXTRA_URL = "imageurl";
    public static final String EXTRA_URL_BACK = "imageurl2";
    public static final String EXTRA_Title = "movietitle";
    public static final String EXTRA_DESC = "description";
    public static final String EXTRA_RATING = "rating";
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_ID = "id_of_movies";
    public RecyclerView recyclerView2;
    public MyAdapter_for_favourite adapter2;
    public List<ListItem> listItems, listItems1;
    public int total_item = 0;
    TextView textView;
    ToDoListDBAdapter mydb_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movie);
        recyclerView2 = (RecyclerView) findViewById(R.id.rv_number_for_fav_movies);
        textView = (TextView) findViewById(R.id.favourite_movies);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 2));
        listItems1 = new ArrayList<>();
        mydb_result = new ToDoListDBAdapter(this);
        set_favourite_movie();
    }
    private void set_favourite_movie() {
        textView.setText("Favourtie list");
        listItems = mydb_result.getAllToDos();
        if (listItems != null && listItems.size() > 0) {

            for (ListItem toDo : listItems) {
                String id = toDo.getId();
                String title = toDo.getTitle();
                String release_date = toDo.getrelase_date();
                String image_url = toDo.getImage();
                String back_img_url = toDo.getBackurl();
                String desc = toDo.getDesc();
                String rating = toDo.getRating();
                ListItem item = new ListItem(id, title, release_date, image_url, back_img_url, desc, rating);
                listItems1.add(item);
            }
            adapter2 = new MyAdapter_for_favourite(listItems1, getApplicationContext());
            recyclerView2.setAdapter(adapter2);
            adapter2.setOnItemClickListener(favourite_movie.this);
        } else {
            Toast.makeText(getApplicationContext(), "List is Empty", Toast.LENGTH_SHORT).show();
            ;
        }
    }
    public void onItemClick(int position) {
        Intent intent = new Intent(favourite_movie.this, DetailActivity.class);
        ListItem itemclicked = listItems.get(position);
        intent.putExtra(EXTRA_URL, itemclicked.getImage());
        intent.putExtra(EXTRA_DESC, itemclicked.getDesc());
        intent.putExtra(EXTRA_Title, itemclicked.getTitle());
        intent.putExtra(EXTRA_URL_BACK, itemclicked.getBackurl());
        intent.putExtra(EXTRA_RATING, itemclicked.getRating());
        intent.putExtra(EXTRA_DATE, itemclicked.getrelase_date());
        intent.putExtra(EXTRA_ID, itemclicked.getId());
        startActivity(intent);
    }
}
