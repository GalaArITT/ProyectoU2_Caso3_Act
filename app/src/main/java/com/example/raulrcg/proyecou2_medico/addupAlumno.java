package com.example.raulrcg.proyecou2_medico;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class addupAlumno extends AppCompatActivity {
    EditText nom,cel,mail,car;
    Button btn;
    DBAdapter db;
    TextView titulo;

    int op,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alumno);

        nom=(EditText) findViewById(R.id.nombrep);
        cel=(EditText) findViewById(R.id.direccionp);
        mail=(EditText) findViewById(R.id.mailp);
        car = (EditText) findViewById(R.id.carrerap);
        btn=(Button) findViewById(R.id.buttongp);
        titulo=(TextView) findViewById(R.id.titulop);

        //String fant="";

        Bundle parametros = this.getIntent().getExtras();
        op = parametros.getInt("op");
        id = parametros.getInt("idp");

        db=new DBAdapter(getApplicationContext());
        if(op==1){
            titulo.setText("Actualizar Alumno");

            db.open();
            Cursor paciente=db.getAllPaciente(id);
                nom.setText(paciente.getString(1));
                cel.setText(paciente.getString(2));
                mail.setText(paciente.getString(3));
                car.setText(paciente.getString(4));
                //fant=paciente.getString(5);
            paciente.close();
            db.close();
        }

        //final String finalFant = fant;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(op==1){
                    db.open();
                    db.updatePaciente(id,
                            nom.getText().toString(),
                            cel.getText().toString(),
                            mail.getText().toString(),
                            car.getText().toString());
                    db.close();
                }else{
                    db.open();
                    if(db.insertPaciente(
                            nom.getText().toString(),
                            cel.getText().toString(),
                            mail.getText().toString(),
                            car.getText().toString()

                    )){
                        Toast.makeText(getApplicationContext(),"Guardado",Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }
                Intent i=new Intent(addupAlumno.this,Alumnos.class);
                startActivity(i);
            }
        });
    }
}
