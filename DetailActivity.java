package com.example.lucky.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.lucky.myapplication.MainActivity.EXTRA_DATE;
import static com.example.lucky.myapplication.MainActivity.EXTRA_DESC;
import static com.example.lucky.myapplication.MainActivity.EXTRA_ID;
import static com.example.lucky.myapplication.MainActivity.EXTRA_RATING;
import static com.example.lucky.myapplication.MainActivity.EXTRA_Title;
import static com.example.lucky.myapplication.MainActivity.EXTRA_URL;
import static com.example.lucky.myapplication.MainActivity.EXTRA_URL_BACK;

public class DetailActivity extends AppCompatActivity {
    public String id;
    public String title;
    public String date;
    public String imageUrl;
    public String backimageurl;
    public String description;
    public String rating;
    public String trailer_url;
    public String you_tube_link;
    public String overall_review = "";
    public String review_content = null;
    CardView cardview;
    TextView t_review;
    TextView t_no_of_reviews;
    Button favourite_button;
    ToDoListDBAdapter mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();
        cardview = (CardView) findViewById(R.id.card_trailer);
        t_review = (TextView) findViewById(R.id.review);
        t_no_of_reviews = (TextView) findViewById(R.id.no_of_reviews);
        favourite_button = (Button) findViewById(R.id.add_to_fav_button);
        mydb = new ToDoListDBAdapter(this);
        Intent intent = getIntent();
        id = intent.getStringExtra(EXTRA_ID);
        title = intent.getStringExtra(EXTRA_Title);
        date = intent.getStringExtra(EXTRA_DATE);
        imageUrl = intent.getStringExtra(EXTRA_URL);
        backimageurl = intent.getStringExtra(EXTRA_URL_BACK);
        description = intent.getStringExtra(EXTRA_DESC);
        rating = intent.getStringExtra(EXTRA_RATING);
        final TextView title1 = findViewById(R.id.title2);
        TextView desc = findViewById(R.id.desc2);
        ImageView image2 = findViewById(R.id.back_poster);
        TextView r_date = findViewById(R.id.release_date);
        TextView rating1 = findViewById(R.id.rating);
        rating = rating + "/10";
        Picasso.with(this).load(backimageurl).fit().centerCrop().into(image2);
        title1.setText(title);
        desc.setText(description);
        r_date.setText(date);
        rating1.setText(rating);
        fetch_review(id);
        trailer_url = "http://api.themoviedb.org/3/movie/" + id;
        trailer_url += "/videos?api_key=6de3dca764621043d6705a4ae12a7c21";
        setYouTubeLink();
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String you_tube_link_of_movie = "https://www.youtube.com/watch?v=" + you_tube_link;
                Uri webpage = Uri.parse(you_tube_link_of_movie);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = mydb.insert(id, title, date, imageUrl, backimageurl, description, rating);
                if (isInserted == true)
                    Toast.makeText(DetailActivity.this, "Movie Added successfully", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(DetailActivity.this, "Movie already added to the favourite list", Toast.LENGTH_LONG).show();
                    favourite_button.setEnabled(false);
                }
            }
        });


    }

    private void setYouTubeLink() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, trailer_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("results");
                    JSONObject o = array.getJSONObject(0);
                    String key_value = o.getString("key");
                    you_tube_link = key_value;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void fetch_review(String str) {
        String review = "http://api.themoviedb.org/3/movie/" + str + "/reviews?api_key=6de3dca764621043d6705a4ae12a7c21";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, review, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int count = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        String author_name = o.getString("author");
                        review_content = o.getString("content");
                        count++;
                        overall_review += count + "." + "Author:\n" + author_name + '\n' + "Comments:" + '\n' + review_content + '\n' + '\n' + '\n';
                    }
                    if (review_content == null) {
                        t_no_of_reviews.setText("No reviews Yet");
                    } else {
                        t_review.setText(overall_review);
                        t_no_of_reviews.setText("Total No.of Reviews " + count);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
