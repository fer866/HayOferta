package com.ipn.hayoferta;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class BrandsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private int mCurrentPosition = 1;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private static final String TAG = "RecyclerViewExample";
    private BrandsAdapter mAdapter;
    private List<Marca> marcaList = new ArrayList<Marca>();
    private ListView leftDrawerList;
    private ListViewAdapter adapter;
    private String[] title = new String[]{
            "Promos Cercanas",
            "Establecimientos",
            "Favoritos",
    };
    private int[] images = {
            R.drawable.ic_date,
            R.drawable.ic_offer,
            R.drawable.ic_location,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands);
        initView();
        if (toolbar != null) {
            toolbar.setTitle("Establecimientos");
            setSupportActionBar(toolbar);
        }
        initDrawer();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_brands);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressBar = (ProgressBar) findViewById(R.id.pBar);

        final String url = "http://fernon866.esy.es/ofertas/marca.json";
        new AsyncHttpTask().execute(url);
    }

    private void initView() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        adapter = new ListViewAdapter(this, title, images);
        leftDrawerList.setAdapter(adapter);

        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
    }

    private void selectItem(int position) {
        leftDrawerList.setItemChecked(mCurrentPosition, true);
        drawerLayout.closeDrawer(Gravity.START);
        if (position == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close) {
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            progressBar.setProgress(100);
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line).append("\n");
                    }
                    parseResult(response.toString());
                    result = 1;
                } else {
                    result = 0;
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            if (result == 1) {
                mAdapter = new BrandsAdapter(BrandsActivity.this, marcaList);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                Log.e(TAG, "Error al recibir datos");
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONArray response = new JSONArray(result);
            if (null == marcaList) {
                marcaList = new ArrayList<Marca>();
            }
            for (int i=0; i<response.length(); i++) {
                JSONObject post = response.optJSONObject(i);
                Marca item = new Marca();
                item.setMarca(post.optString("marca"));
                marcaList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawer(Gravity.START);
        else
            super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_brands, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
