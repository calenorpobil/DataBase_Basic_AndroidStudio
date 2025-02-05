package com.merlita.database_basic;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etCodigo, etNombre;
    TextView tvResultados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        tvResultados = findViewById(R.id.tvResultados);

        DBHelper usdbh = new DBHelper(this, "dbUsuarios", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("delete from usuarios");

        assert db != null;
        for (int i=0; i<5; i++){
            int codigo=i;
            String nombre = "Usuario "+i;

            db.execSQL(
                    "INSERT INTO Usuarios (codigo, nombre) VALUES (" + codigo + ", '" + nombre +"')");
        }
        //db.close();

        muestraBase(db);
        db.close();
    }

    public void btEliminar(View v){
        eliminar();
        eliminarQuery();
    }

    private void eliminarQuery() {
        DBHelper usdbh = new DBHelper(this, "dbUsuarios", null, 1);
        int codigo=0;

        try{
            codigo = Integer.parseInt(etCodigo.getText().toString());
            SQLiteDatabase db = usdbh.getWritableDatabase();


            Integer[] args = new Integer[] {codigo};

            String[] campos = new String[] {"codigo", "nombre"};
            Cursor c = db.query("Usuarios", campos, null, args, null, null, null);

        }catch(NumberFormatException ex){

        }
    }

    private void eliminar() {
        DBHelper usdbh = new DBHelper(this, "dbUsuarios", null, 1);
        int codigo=0;

        try{
            codigo = Integer.parseInt(etCodigo.getText().toString());
            SQLiteDatabase db = usdbh.getWritableDatabase();
            db.execSQL("DELETE FROM Usuarios WHERE codigo="+codigo);
            muestraBase(db);
        }catch(NumberFormatException ex){

        }
    }

    public void btInsertar(View v){
        DBHelper usdbh = new DBHelper(this, "dbUsuarios", null, 1);
        SQLiteDatabase dbw = usdbh.getWritableDatabase();

        int codigo=Integer.parseInt(etCodigo.getText().toString());
        String nombre=etNombre.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put("CODIGO", codigo);
        cv.put("NOMBRE", nombre);

        dbw.insert("Usuarios", null, cv);

        muestraBase(dbw);
    }
    public void btConsultar(View v){
        DBHelper usdbh=null;
        try{
            usdbh = new DBHelper(this, "dbUsuarios", null, 1);
        } catch (SQLException ex){

        }
        int codigo=0;

        try{
            codigo = Integer.parseInt(etCodigo.getText().toString());
            assert usdbh != null;
            SQLiteDatabase db = usdbh.getReadableDatabase();
            ArrayList<String> resultados= new ArrayList<>();

            Cursor c = db.rawQuery(" SELECT codigo,nombre " +
                    "FROM Usuarios where codigo="+codigo, null);

            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    resultados.add(c.getString(0));
                    resultados.add(c.getString(1));
                } while(c.moveToNext());
            }
            tvResultados.setText(resultados.toString());
        }catch(NumberFormatException ex){

        }
    }
    public void btActualizar(View v){
        DBHelper usdbh = new DBHelper(this, "dbUsuarios", null, 1);
        int codigo=0;

        try{
            codigo = Integer.parseInt(etCodigo.getText().toString());
            SQLiteDatabase db = usdbh.getWritableDatabase();
            //db.execSQL("DELETE FROM Usuarios WHERE codigo="+codigo);
            muestraBase(db);
        }catch(NumberFormatException ex){

        }
    }

    private void muestraBase(SQLiteDatabase db) {
        ArrayList<String> resultados= new ArrayList<>();

        Cursor c = db.rawQuery(" SELECT codigo,nombre FROM Usuarios", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                resultados.add(c.getString(0));
                resultados.add(c.getString(1));
            } while(c.moveToNext());
        }
        tvResultados.setText(resultados.toString());
    }

}