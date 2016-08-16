package com.ipn.hayoferta;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    int mCurrentPosition = 0;
    private ActionBarDrawerToggle drawerToggle;
    private static final String TAG = "RecyclerViewExample";
    private RecyclerView mRecyclerView;
    private CardsAdapter mAdapter;
    private List<Ofertas> ofertasList = new ArrayList<>();
    private ListView leftDrawerList;
    ListViewAdapter adapter;
    private String[] title = new String[]{
            "Promos Vigentes",
            "Establecimientos",
            "Favoritos",
    };
    private int[] images = {
            R.drawable.ic_date,
            R.drawable.ic_offer,
            R.drawable.heart,
    };
    private SwipeRefreshLayout swipeLayout;
    private ProgressBar mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        initView();
        if (toolbar != null) {
            toolbar.setTitle(R.string.recent_promo);
            setSupportActionBar(toolbar);
        }
        initDrawer();

        if (Build.VERSION.SDK_INT >= 21) {
            Transition exitTrans = new Explode();
            getWindow().setExitTransition(exitTrans);
            Transition reenterTrans = new Slide();
            getWindow().setReenterTransition(reenterTrans);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_cards);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        final String url = "http://fernon866.esy.es/ofertas/consul.json";
        new AsyncHttpTask().execute(url);

        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.blue, R.color.red, R.color.green);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ofertasList.clear();
                new AsyncHttpTask().execute(url);
                swipeLayout.setRefreshing(true);
            }
        });
    }

    private void initView() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        adapter = new ListViewAdapter(this, title, images);
        leftDrawerList.setAdapter(adapter);

        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                selectItem(position);
            }
        });
    }

    private void selectItem(int position) {
        leftDrawerList.setItemChecked(mCurrentPosition, true);
        drawerLayout.closeDrawer(GravityCompat.START);
        if (position == 1) {
            Intent intent = new Intent(this, BrandsActivity.class);
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
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            mProgress.setProgress(100);
            Integer result = 0;
            HttpURLConnection urlConnection;
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
            //super.onPostExecute(result);
            swipeLayout.setRefreshing(false);
            mProgress.setVisibility(View.GONE);
            if (result == 1) {
                mAdapter = new CardsAdapter(getApplicationContext(), ofertasList);
                ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(mAdapter);
                animationAdapter.setDuration(500);
                animationAdapter.setFirstOnly(false);
                animationAdapter.setInterpolator(new OvershootInterpolator());
                mRecyclerView.setAdapter(animationAdapter);

                mAdapter.SetOnItemClickListener(new CardsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Ofertas item = ofertasList.get(position);
                        Intent intent = new Intent(MainActivity.this, PromoInfo.class);
                        intent.putExtra("descripcion", item.getDescripcion());
                        intent.putExtra("marca", item.getMarca());
                        intent.putExtra("imageURL", item.getImagen());
                        intent.putExtra("latitud", item.getLat());
                        intent.putExtra("longitud", item.getLad());
                        if (Build.VERSION.SDK_INT >= 21) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                            startActivity(intent, options.toBundle());
                        } else {
                            startActivity(intent);
                        }
                    }
                });
            } else {
                Log.e(TAG, "Error al recibir datos");
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONArray response = new JSONArray(result);
            if (ofertasList == null) {
                ofertasList = new ArrayList<>();
            }
            for (int i=0; i<response.length(); i++) {
                JSONObject post = response.optJSONObject(i);
                Ofertas item = new Ofertas();
                item.setDescripcion(post.optString("descripcion"));
                item.setMarca(post.optString("marca"));
                item.setImagen(post.optString("imagen"));
                item.setLat(post.optString("latitud"));
                item.setLad(post.optString("longitud"));
                ofertasList.add(item);
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Buscar ofertas");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
