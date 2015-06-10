package com.josevargas.restaurate_mapas;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ActionBarActivity {

    int frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Pagina_principal fragment0 = new Pagina_principal();
        fragmentTransaction.replace(android.R.id.content, fragment0).commit();
        frag=1;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fragm",frag);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        frag=savedInstanceState.getInt("fragm");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (frag == 1) {
            Pagina_principal fragment0 = new Pagina_principal();
            fragmentTransaction.replace(android.R.id.content, fragment0).commit();
            frag=1;
        }
        if (frag == 2) {
            Editar_sedes fragment = new Editar_sedes();
            fragmentTransaction.replace(android.R.id.content, fragment).commit();
            frag=2;
        }

        if (frag == 3) {
            mapa2 fragment2 = new mapa2();
            fragmentTransaction.replace(android.R.id.content, fragment2).commit();
            frag=3;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //noinspection SimplifiableIfStatement

        if (id == R.id.menu_home_page) {
            Pagina_principal fragment0 = new Pagina_principal();
            fragmentTransaction.replace(android.R.id.content, fragment0).commit();
            frag=1;
            return true;
        }

        if (id == R.id.menu_editarSedes) {
            Editar_sedes fragment = new Editar_sedes();
            fragmentTransaction.replace(android.R.id.content, fragment).commit();
            frag=2;
            return true;
        }

        if (id == R.id.menu_mapa2) {
            mapa2 fragment = new mapa2();
            fragmentTransaction.replace(android.R.id.content, fragment).commit();
            frag=3;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(frag==1) {
            super.onBackPressed();
        }

        if(frag==2) {
            Pagina_principal fragment0 = new Pagina_principal();
            fragmentTransaction.replace(android.R.id.content, fragment0).commit();
            frag=1;
        }

        if(frag==3) {
            Pagina_principal fragment0 = new Pagina_principal();
            fragmentTransaction.replace(android.R.id.content, fragment0).commit();
            frag=1;
        }



    }
}
