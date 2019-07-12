package com.example.mitienda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitienda.listDataProducto.CustomAdapter;
import com.example.mitienda.listDataProducto.ItemList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // private ListView LIST;
    private GridView LIST;
    private ArrayList<ItemList> LISTINFO;
    private CustomAdapter ADAPTER;
    private Context root;

    private TextView userEmail;
    private  String miToken;
    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //verificar si ya inicio sesión

        //-----acceder al textview del nav
        View v = navigationView.getHeaderView(0);
        userEmail =(TextView) v.findViewById(R.id.tv_userEmail);

        Bundle intentExtras = this.getIntent().getExtras();
        //String datos = intentExtras.getString("usuario");
        //recuperando datos usuario y token del usuario logeado;
        preferencias = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
        String email = preferencias.getString("usuario", "vacio");
        miToken = preferencias.getString("token", "");

        //llamando inicioSession para verificar si existe el token y cerrar el acceso al mainActivity
        inicioSesion();
        userEmail.setText(email);
        //para la listwiew----adapter
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        root = this;
        LISTINFO = new ArrayList<ItemList>();
        String aux = null;
        loadInitRestData(aux);
        loadComponents();

    }
