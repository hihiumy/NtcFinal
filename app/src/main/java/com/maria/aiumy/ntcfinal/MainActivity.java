package com.maria.aiumy.ntcfinal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import bdcontroler.GlobalDBHelper;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener  {

    private GlobalDBHelper globalDBHelper = new GlobalDBHelper();
    ArrayList<String> listaGrupos = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ListView listView = (ListView) findViewById(R.id.listagrupos);
        listView.setBackgroundColor(Color.WHITE);
        try {
            arrayGrupos();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, PerfilActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_notification) {
            Intent intent = new Intent(this, PostagemActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_groups) {
            Intent intent = new Intent(this, MygroupsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_create) {
            Intent intent = new Intent(this, NewgroupsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_new) {
            Intent intent = new Intent(this, CriargrupoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("emailLogado");
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void criarChannel(){
    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)  {}
        NotificationChannel channel = new NotificationChannel("My_channel_id_01", "my_channel",NotificationManager.IMPORTANCE_DEFAULT);
    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    nm.createNotificationChannel(channel);

    }

   /* public void listaGrupos() throws IOException, JSONException {
        globalDBHelper = new GlobalDBHelper();
        JSONArray jsonGrupos= globalDBHelper.selectAllFromGrupos(getApplicationContext());
        ArrayList<String> listGroups = new ArrayList<String>();

        for (int i=0; i < jsonGrupos.length(); i++){
            JSONObject GruposObject = jsonGrupos.getJSONObject(i);
            String nomeGrupo= GruposObject.getString("nome");
            listGroups.add(nomeGrupo);
        }
        ListView mostraGrupos = findViewById(R.id.listagrupos);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listGroups);
        mostraGrupos.setAdapter(adapter);
    }*/


    public void arrayGrupos() throws IOException, JSONException //
    {
        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        String emailUser = sp.getString("emailLogado",null);
        JSONArray jsonGrupos = globalDBHelper.usuarioParticipa(getApplicationContext(), emailUser);


        for (int i = 0; i < jsonGrupos.length(); i++) {
            JSONObject grupoObject = jsonGrupos.getJSONObject(i);
            String nomeGrupo = grupoObject.getString("nome");
            listaGrupos.add(nomeGrupo);
        }

        ListView neoListView = (ListView) findViewById(R.id.listagrupos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaGrupos);
        neoListView.setOnItemClickListener(this);
        neoListView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        String nomeGrupo =  listaGrupos.get(position);
        Bundle b = new Bundle();
        Intent intent = new Intent(this, GrupoActivity.class);
        b.putString("nomeGrupo", nomeGrupo.toString());
        intent.putExtras(b);
        startActivity(intent);
    }


}
