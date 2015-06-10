package com.josevargas.restaurate_mapas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jose on 01/06/2015.
 */
public class DataBaseManager  {

    public static final String TABLE_NAME ="contactos";
    public static final String CN_ID ="_id";
    public static final String CN_NAME ="nombre";
    public static final String CN_LAT ="latitud";
    public static final String CN_LON ="longitud";

    public static final String CREATE_TABLE = " create table "
            +TABLE_NAME+ " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_NAME + " text not null,"
            + CN_LAT + " text not null,"
            + CN_LON + " text);";

    private DbHelper helper;
    private SQLiteDatabase db;

    public DataBaseManager(Context context) {
        //Generar el constructor de DBM
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();
    }



    public void insertar (String nombre, String latitud, String longitud){
        ContentValues valores = new ContentValues();
        valores.put(CN_NAME, nombre);
        valores.put(CN_LAT, latitud);
        valores.put(CN_LON, longitud);
        db.insert(TABLE_NAME,null,valores);
    }

    public void eliminar_id(String _id){
        db.delete(TABLE_NAME,CN_ID+"=?", new String[] {_id});
    }

    public void eliminar_nombre(String nombre){
        db.delete(TABLE_NAME,CN_NAME+"=?", new String[] {nombre});
    }

    public void modificar_coordenadas(String nombre, String nuevalatitud,String nuevalongitud){
        db.update(TABLE_NAME,generarContentValues(nombre,nuevalatitud,nuevalongitud),CN_NAME + "=?", new String[] {nombre});
    }

    private ContentValues generarContentValues(String nombre, String latitud,String longitud) {
        ContentValues valores = new ContentValues();
        valores.put(CN_NAME, nombre);
        valores.put(CN_LAT, latitud);
        valores.put(CN_LON, longitud);
        return valores;
    }

    public Cursor cargarCursor(){
        //query(Table,columns,String selection, String selection args,String Group BY,String Having,String OrderBy)
        String [] columnas= new String[] {CN_ID, CN_NAME, CN_LAT, CN_LON};
        return db.query(TABLE_NAME,columnas,null,null,null,null,null);
    }

    public Cursor buscarContacto(String nombre){

        String[] columnas = new String[] {CN_ID,CN_NAME,CN_LAT,CN_LON};
        return db.query(TABLE_NAME,columnas,CN_NAME + "=?", new String[] {nombre},null,null,null);
    }
}
