package com.example.lucky.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapteer.OnItemClickListener {
    public static final String EXTRA_URL = "imageurl";
    public static final String EXTRA_URL_BACK = "imageurl2";
    public static final String EXTRA_Title = "movietitle";
    public static final String EXTRA_DESC = "description";
    public static final String EXTRA_RATING = "rating";
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_ID = "id_of_movies";
    private static final String Url_for_popular = "https://api.themoviedb.org/3/movie/popular?api_key=6de3dca764621043d6705a4ae12a7c21";
    private static final String Url_for_top_rated = "https://api.themoviedb.org/3/movie/top_rated?api_key=6de3dca764621043d6705a4ae12a7c21";
    private static final String Url_for_now_playing = "https://api.themoviedb.org/3/movie/now_playing?api_key=6de3dca764621043d6705a4ae12a7c21";
    private static final String Url_for_upcoming = "https://api.themoviedb.org/3/movie/upcoming?api_key=6de3dca764621043d6705a4ae12a7c21";
    public static String sortby = Url_for_now_playing;
    public static String movie_category = "Now Playing";
    public String URL_DATA = "";
    int page_count = 1;
    String popular = Url_for_popular;
    String toprated = Url_for_top_rated;
    String upcoming = Url_for_upcoming;
    TextView movie_type;
    TextView page_no;
    Button next, prev;
    private RecyclerView recyclerView;
    private MyAdapteer adapteer;
    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_number);
        movie_type = (TextView) findViewById(R.id.movie_type);
        page_no=(TextView)findViewById(R.id.page);
        next = (Button) findViewById(R.id.next);
        prev = (Button) findViewById(R.id.prev);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        listItems = new ArrayList<>();
        listItems.clear();
        setdatailnRecyclerView(sortby);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page_count++;
                sortby += "&page=" + page_count;
                setdatailnRecyclerView(sortby);
                if(page_count>1)
                 prev.setEnabled(true);

            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                page_count--;
                sortby += "&page=" + page_count;
                setdatailnRecyclerView(sortby);
                if (page_count == 1)
                    prev.setEnabled(false);


            }

        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void setdatailnRecyclerView(String url) {
        listItems.clear();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
        if(page_count==1)
            prev.setEnabled(false);
        else
            next.setEnabled(true);
        movie_type.setText(movie_category);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        String title = o.getString("title");
                        String overv = o.getString("overview");
                        String img_URL = "http://image.tmdb.org/t/p/original";
                        img_URL += o.getString("poster_path");
                        String backurl = "http://image.tmdb.org/t/p/w780";
                        backurl += o.getString("backdrop_path");
                        String release_date = o.getString("release_date");
                        String rating_of_movie = o.getString("vote_average");
                        String id_of_movie = o.getString("id");
                        ListItem item = new ListItem(id_of_movie, title, release_date, img_URL, backurl, overv, rating_of_movie);
                        listItems.add(item);
                    }
                    //Toast.makeText(getApplicationContext(), "Total Results: " + array.length(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Page : " + page_count, Toast.LENGTH_SHORT).show();
                    adapteer = new MyAdapteer(listItems, getApplicationContext());
                    recyclerView.setAdapter(adapteer);
                    adapteer.setOnItemClickListener(MainActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.popular_movies) {
            movie_category = "popular movies";
            movie_type.setText(movie_category);
            sortby = Url_for_popular;
            setdatailnRecyclerView(sortby);
            page_count = 1;
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (movie_type.getText().toString().equalsIgnoreCase("popular movies")) {
                        page_count++;
                        sortby += "&page=" + page_count;
                        setdatailnRecyclerView(sortby);
                        prev.setEnabled(true);
                    }
                }
            });
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (movie_type.getText().toString().equalsIgnoreCase("popular movies")) {
                        page_count--;
                        sortby += "&page=" + page_count;
                        setdatailnRecyclerView(sortby);
                        if (page_count == 1)
                            prev.setEnabled(false);
                    }

                }
            });

        } else if (i == R.id.top_rated_movies) {
            movie_category = "top rated";
            movie_type.setText(movie_category);
            sortby = Url_for_top_rated;
            setdatailnRecyclerView(sortby);
            page_count = 1;
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (movie_type.getText().toString().equalsIgnoreCase("top rated")) {
                        page_count++;
                        sortby += "&page=" + page_count;
                        setdatailnRecyclerView(sortby);
                        prev.setEnabled(true);
                    }
                }
            });
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (movie_type.getText().toString().equalsIgnoreCase("top rated")) {
                        page_count--;
                        sortby += "&page=" + page_count;
                        setdatailnRecyclerView(sortby);
                        if (page_count == 1)
                            prev.setEnabled(false);
                    }

                }
            });

        } else if (i == R.id.upcoming_movies) {
            movie_category = "upcoming movies";
            movie_type.setText(movie_category);
            sortby = Url_for_upcoming;
            setdatailnRecyclerView(sortby);
            page_count = 1;
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (movie_type.getText().toString().equalsIgnoreCase("upcoming movies")) {
                        page_count++;
                        sortby += "&page=" + page_count;
                        setdatailnRecyclerView(sortby);
                        prev.setEnabled(true);
                    }
                }
            });
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (movie_type.getText().toString().equalsIgnoreCase("upcoming movies")) {
                        page_count--;
                        sortby += "&page=" + page_count;
                        setdatailnRecyclerView(sortby);
                        if (page_count == 1)
                            prev.setEnabled(false);
                    }

                }
            });

        } else {
            Intent intent = new Intent(MainActivity.this, favourite_movie.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("my_url", sortby);
        outState.putString("movie_type1", movie_category);
        outState.putInt("pagecount", page_count);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        sortby = savedInstanceState.getString("my_url");
        movie_category = savedInstanceState.getString("movie_type1");
        page_count = savedInstanceState.getInt("pagecount");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
