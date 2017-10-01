package com.example.raulrcg.proyecou2_medico;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class DBAdapter {

    //Tabla Alumno
    static final String ROWID_P = "_id";
    static final String NAME_P = "name";
    static final String ADDRESS_P = "celular";
    static final String EMAIL_P = "email";
    static final String DATE_P = "career";
    //Tabla Actividad
    static final String ROWID_M = "_id";
    static final String ROWID_P_M = "_idAlumno";
    static final String NAME_M = "tipo_act";
    static final String DATE_INI_M = "fecha_ini";
    static final String DATE_FIN_M = "fecha_fin";
    static final String VIGENCIA_M = "credito";

    //Nombre de tablas
    static final String DATABASE_TABLE_P = "alumno";
    static final String DATABASE_TABLE_M = "actividad";
    //Constantes
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "MyDB";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATE ="create table alumno (_id integer primary key autoincrement," +
            "name text not null,celular text not null,email text not null," +
            "career text not null);";

    static final String DATABASE_CREATE1="create table actividad (_id integer primary key autoincrement," +
            "_idAlumno integer not null, tipo_act text not null," +
            "fecha_ini text not null, fecha_fin text not null, credito text not null," +
            "FOREIGN KEY(_idAlumno) REFERENCES alumno(_id));";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    //---Abrir la base de datos---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //---Cerrar la base de datos---
    public void close() {
        DBHelper.close();
    }

    //Insertar paciente
    public boolean insertPaciente(String name, String phone, String email, String carrera) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NAME_P, name);
        initialValues.put(ADDRESS_P, phone);
        initialValues.put(EMAIL_P, email);
        initialValues.put(DATE_P, carrera);

        return db.insert(DATABASE_TABLE_P, null, initialValues)>0;
    }
    //Insertar medicamento
    public long insertMedicamento(int idp,String nombre,String fini,String ffin,String vig) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ROWID_P_M, idp);
        initialValues.put(NAME_M, nombre);
        initialValues.put(DATE_INI_M, fini);
        initialValues.put(DATE_FIN_M, ffin);
        initialValues.put(VIGENCIA_M, vig);

        return db.insert(DATABASE_TABLE_M, null, initialValues);
    }

    //---Recuperar todos los pacientes---
    public Cursor getAllPacientes() {
        return db.query(DATABASE_TABLE_P, new String[] {ROWID_P, NAME_P,
                EMAIL_P, DATE_P}, null, null, null, null, null);
    }

    //-- Recuperar todos los pacientes de AZ
    public Cursor getAllPacientesAZ() {
        String sql="SELECT * FROM "+DATABASE_TABLE_P+" ORDER BY "+NAME_P+" ; ";
        return db.rawQuery(sql,null);
    }
    //consulta que cuenta
    public Cursor getCountPacientesAz(int rowid){
        String sql="select sum(credito) from"+ DATABASE_TABLE_M+ "where" +ROWID_P_M +"=" + rowid;
        return db.rawQuery(sql,null);
    }


    //-- Recuperar paciente
    public Cursor getAllPaciente(int rowId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_P, new String[] {ROWID_P,
                                NAME_P, ADDRESS_P,EMAIL_P,DATE_P}, ROWID_P + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //-- Recuperar todos lo medicamentos de un paciente de AZ
    public Cursor getAllMedicamentosAZ(int idp) {
        return db.query(true, DATABASE_TABLE_M,
                        new String[] {ROWID_M,ROWID_P_M,NAME_M, DATE_INI_M},
                        ROWID_P_M + "=" + idp, null,
                        null, null,NAME_M, null);
    }

    //-- Recuperar todos lo medicamentos de un paciente de AZ
    public Cursor getMedicamento(int id) {
        Cursor mCursor=db.query(true, DATABASE_TABLE_M,
                new String[] {NAME_M, DATE_INI_M,DATE_FIN_M,VIGENCIA_M},
                ROWID_M + "=" + id, null,
                null, null,NAME_M, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //-- Recuperar medicamentos vigentes de la AZ de un paciente
    public Cursor getAllMedicamentosVigAZ(int idp) {
        return db.query(true, DATABASE_TABLE_M,
                new String[] {ROWID_M,ROWID_P_M,NAME_M, DATE_INI_M},
                ROWID_P_M + "=" + idp+" AND "+VIGENCIA_M+"=1", null,
                null, null,NAME_M, null);
    }

    //update paciente
    public boolean updatePaciente(long rowId, String name, String phone, String email,String dt) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NAME_P, name);
        initialValues.put(ADDRESS_P, phone);
        initialValues.put(EMAIL_P, email);
        initialValues.put(DATE_P, dt);
        return db.update(DATABASE_TABLE_P, initialValues, ROWID_P + "=" + rowId, null) > 0;
    }
    //Update medicamento
    public boolean updateMedicamento(long rowId,int idp,String nombre,String fini,String ffin,String vig) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ROWID_P_M, idp);
        initialValues.put(NAME_M, nombre);
        initialValues.put(DATE_INI_M, fini);
        initialValues.put(DATE_FIN_M, ffin);
        initialValues.put(VIGENCIA_M, vig);
        return db.update(DATABASE_TABLE_M, initialValues, ROWID_M + "=" + rowId, null) > 0;
    }

    //Ver cuantos pacientes existen
    public int lengthPacientes(){
        int n=0;
        Cursor result=getAllPacientes();
        result.moveToFirst();
        while (!result.isAfterLast()) {
            n++;
            result.moveToNext();
        }
        result.close();
        return n;
    }

    //Ver cuantos medicamentos tiene un paciente
    public int lengthMedicamentos(int idp){
        int n=0;
        Cursor result=getAllMedicamentosAZ(idp);
        result.moveToFirst();
        while (!result.isAfterLast()) {
            n++;
            result.moveToNext();
        }
        result.close();
        return n;
    }
    //Ver cuantos medicamentos vigentes tiene un paciente
    public int lengthMedicamentosVig(int idp){
        int n=0;
        Cursor result=getAllMedicamentosVigAZ(idp);
        result.moveToFirst();
        while (!result.isAfterLast()) {
            n++;
            result.moveToNext();
        }
        result.close();
        return n;
    }

    // Clase de tipo SQLIteOpenHelper
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE1);
            //try {} catch (SQLException e) {e.printStackTrace();}
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Actualizando desde la versión " + oldVersion + " a "
                    + newVersion + ", Se destruiran todos los datos");
            db.execSQL("se aplica DROP TABLE ");
            onCreate(db);
        }
    }
}
