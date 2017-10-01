package com.example.raulrcg.proyecou2_medico;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Actividad extends AppCompatActivity {

    TextView nom,dir,mail,fec;
    ListView lista;
    AdaptadorListaAlumnos al;
    DBAdapter db;
    CheckBox vig;

    int idm[];
    String nombres[];
    String fechas[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final int idp=this.getIntent().getExtras().getInt("idp");

        db=new DBAdapter(getApplicationContext());

        //Toast.makeText(this,idp+"",Toast.LENGTH_SHORT).show();

        nom=(TextView) findViewById(R.id.tvNombre);
        dir=(TextView) findViewById(R.id.tvDir);
        mail=(TextView) findViewById(R.id.tvMail);
        fec=(TextView) findViewById(R.id.tvFecha);
        lista=(ListView) findViewById(R.id.listaMedicamentos);
        //vig=(CheckBox) findViewById(R.id.vig);

        db.open();
        Cursor paciente=db.getAllPaciente(idp);
        nom.append(paciente.getString(1));
        dir.append(paciente.getString(2));
        mail.append(paciente.getString(3));
        fec.append(paciente.getString(4));
        paciente.close();


        int n = db.lengthMedicamentos(idp);
        Toast.makeText(this,n+" Actividades complementarias",Toast.LENGTH_SHORT).show();
        setLista(db.getAllMedicamentosAZ(idp),n);
        db.close();


        //Agregar medicamento
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Actividad.this,addupActividad.class);
                intent.putExtra("idp",idp);
                startActivity(intent);
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Actividad.this,addupActividad.class);
                intent.putExtra("idp",idp);
                intent.putExtra("idm",idm[i]);
                intent.putExtra("op",1);//0 insert 1 update
                startActivity(intent);
            }
        });
    }

    public void setLista(Cursor result,int n){
        if(n>0){
            nombres= new String[n];
            fechas= new String[n];
            idm=new int[n];
            int i=0;
            result.moveToFirst();
            while (!result.isAfterLast()) {
                int id = result.getInt(0);
                String name=result.getString(2);
                String fecha=result.getString(3);
                idm[i]=id;
                nombres[i]=name;
                fechas[i]=fecha;
                i++;
                result.moveToNext();
            }
            result.close();
            al=new AdaptadorListaAlumnos(getApplicationContext(),nombres,fechas);
            lista.setAdapter(al);
        }
    }
}
