package com.ipn.hayoferta;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;



public class PromoInfo extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtDesc;
    TextView txtMarca;
    TextView txtLocation;
    private ImageView imageLogo;
    Target target;
    private FrameLayout layout;
    private FloatingActionButton fab;
    private String marca;
    private String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_info);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                toolbar.setPadding(0, getStatusBarHeight(), 0, 0);

                Transition enterTrans = new Explode();
                getWindow().setEnterTransition(enterTrans);
                Transition returnTrans = new Slide();
                getWindow().setReturnTransition(returnTrans);
            }
        }
        initTransaction();
    }

    private void initTransaction() {
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtMarca = (TextView) findViewById(R.id.marCard);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        imageLogo = (ImageView) findViewById(R.id.imageLogo);
        layout = (FrameLayout) findViewById(R.id.baseDescription);
        fab = (FloatingActionButton) findViewById(R.id.fab_share);

        desc = getIntent().getStringExtra("descripcion");
        marca = getIntent().getStringExtra("marca");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lat = getIntent().getStringExtra("latitud");
                String lad = getIntent().getStringExtra("longitud");
                String title = getIntent().getStringExtra("marca");
                Intent intent = new Intent(PromoInfo.this, MapsActivity.class);
                intent.putExtra("latitud", lat);
                intent.putExtra("longitud", lad);
                intent.putExtra("title", title);
                intent.putExtra("description", desc);
                startActivity(intent);
            }
        });

        txtDesc.setText(desc);
        txtMarca.setText(marca);

        target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        imageLogo.setImageBitmap(bitmap);
                        int defaultColor = getResources().getColor(R.color.background_material_dark);
                        layout.setBackgroundColor(palette.getVibrantColor(defaultColor));
                        fab.setBackgroundTintList(ColorStateList.valueOf(palette.getLightMutedColor(defaultColor)));
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(this).load(getIntent().getStringExtra("imageURL")).error(R.drawable.no_image).into(target);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_promo_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Aprovecha esta oferta de " + marca + ":\n" + desc
                    + "\n\nVisto en Hay Oferta");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        }

        if (id == android.R.id.home) {
            if (Build.VERSION.SDK_INT >= 21)
                finishAfterTransition();
            else
                finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
