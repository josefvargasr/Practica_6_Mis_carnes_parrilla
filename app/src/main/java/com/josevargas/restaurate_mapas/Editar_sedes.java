package com.josevargas.restaurate_mapas;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Editar_sedes extends Fragment {

    Button add_sede, delete_sede, change_sede;
    EditText latitud, longitud, nombre_sede;
    private DataBaseManager manager;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private ListView lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_editar_sedes,container, false);

        add_sede = (Button) mLinearLayout.findViewById(R.id.badd_sede);
        delete_sede = (Button) mLinearLayout.findViewById(R.id.bdelete_sede);
        change_sede = (Button) mLinearLayout.findViewById(R.id.bchange_sede);

        nombre_sede= (EditText) mLinearLayout.findViewById(R.id.nombre_sede);
        latitud= (EditText) mLinearLayout.findViewById(R.id.latitud);
        longitud= (EditText) mLinearLayout.findViewById(R.id.longitud);
        lista = (ListView) mLinearLayout.findViewById(R.id.lista);

        manager = new DataBaseManager(getActivity());

        String[] from = new String[] {manager.CN_NAME,manager.CN_LAT,manager.CN_LON};
        int[] to = new int[] {android.R.id.text1,android.R.id.text2,android.R.id.text2+1};
        cursor = manager.cargarCursor();

        adapter = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_1,cursor,from,to,0);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                manager = new DataBaseManager(getActivity());
                List<String> sede = new ArrayList<String>();
                List<String> latitud = new ArrayList<String>();
                List<String> longitud = new ArrayList<String>();
                Cursor c = manager.cargarCursor();
                if(c.moveToFirst()){
                    do{
                        sede.add(c.getString(1));
                        latitud.add(c.getString(2));
                        longitud.add(c.getString(3));
                    }while (c.moveToNext());
                }
                Toast.makeText(getActivity(), sede.get(position)+ "\n" +"("+ latitud.get(position)+ "," + longitud.get(position)+")", Toast.LENGTH_SHORT).show();
            }
        });

        add_sede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cont=0;
                if (TextUtils.isEmpty(nombre_sede.getText()) || TextUtils.isEmpty(latitud.getText()) || TextUtils.isEmpty(longitud.getText())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toastcampos), Toast.LENGTH_SHORT).show();
                } else {
                    Cursor c = manager.cargarCursor();
                    if(c.moveToFirst()){
                        do{
                            if(c.getString(1).equals(nombre_sede.getText().toString())){
                                cont++;
                            }
                        }while (c.moveToNext());
                    }
                    if(cont==0) {
                        if (Float.parseFloat(latitud.getText().toString()) > 90 || Float.parseFloat(latitud.getText().toString()) < -90
                                || Float.parseFloat(longitud.getText().toString()) > 180 || Float.parseFloat(longitud.getText().toString()) < -180) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.CoordIncorr), Toast.LENGTH_SHORT).show();
                        } else {
                            manager.insertar(nombre_sede.getText().toString(), latitud.getText().toString(), longitud.getText().toString());
                            Toast.makeText(getActivity(), getResources().getString(R.string.guardado)+" "+ nombre_sede.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity(),getResources().getString(R.string.laSede)+" "+ nombre_sede.getText().toString()+" "+getResources().getString(R.string.datos), Toast.LENGTH_SHORT).show();
                    }
                }
                Cursor c = manager.cargarCursor();
                adapter.changeCursor(c);
            }
        });

        delete_sede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cont=0;
                if(TextUtils.isEmpty(nombre_sede.getText())) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.toastcampos),Toast.LENGTH_SHORT).show();
                }else {
                    Cursor c = manager.cargarCursor();
                    if(c.moveToFirst()){
                        do{
                            if(c.getString(1).equals(nombre_sede.getText().toString())){
                                cont++;
                            }
                          }while (c.moveToNext());
                    }
                    if(cont!=0){
                        manager.eliminar_nombre(nombre_sede.getText().toString());
                        Toast.makeText(getActivity(), getResources().getString(R.string.eliminado)+" " + nombre_sede.getText().toString(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(),getResources().getString(R.string.laSede)+" "+ nombre_sede.getText().toString()+" "+getResources().getString(R.string.datos), Toast.LENGTH_SHORT).show();
                    }
                }
                Cursor c = manager.cargarCursor();
                adapter.changeCursor(c);
            }
        });

        change_sede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cont=0;
                if(TextUtils.isEmpty(nombre_sede.getText()) || TextUtils.isEmpty(latitud.getText()) || TextUtils.isEmpty(longitud.getText())) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.toastcampos),Toast.LENGTH_SHORT).show();
                }else{Cursor c = manager.cargarCursor();
                    if(c.moveToFirst()){
                        do{
                            if(c.getString(1).equals(nombre_sede.getText().toString())){
                                cont++;
                            }
                        }while (c.moveToNext());
                    }
                    if(cont!=0) {
                        if (Float.parseFloat(latitud.getText().toString()) > 90 || Float.parseFloat(latitud.getText().toString()) < -90
                                || Float.parseFloat(longitud.getText().toString()) > 180 || Float.parseFloat(longitud.getText().toString()) < -180) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.CoordIncorr), Toast.LENGTH_SHORT).show();
                        } else {
                            manager.modificar_coordenadas(nombre_sede.getText().toString(), latitud.getText().toString(), longitud.getText().toString());
                            Toast.makeText(getActivity(), getResources().getString(R.string.modificado)+" " + nombre_sede.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity(),getResources().getString(R.string.laSede)+" "+ nombre_sede.getText().toString()+" "+getResources().getString(R.string.datos), Toast.LENGTH_SHORT).show();
                    }
                }
                Cursor c = manager.cargarCursor();
                adapter.changeCursor(c);
            }
        });
        return mLinearLayout;
    }



}
