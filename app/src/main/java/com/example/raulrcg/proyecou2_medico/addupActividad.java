package com.example.raulrcg.proyecou2_medico;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class addupActividad extends AppCompatActivity {

    EditText nom,fini,ffin,cred;
    Button btn;
    TextView titulo;

    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_actividad);

        nom = (EditText) findViewById(R.id.nombre);
        fini = (EditText) findViewById(R.id.fini);
        ffin = (EditText) findViewById(R.id.ffin);
        cred = (EditText) findViewById(R.id.creditos);
        btn = (Button) findViewById(R.id.btnGuardar);
        titulo = (TextView) findViewById(R.id.titulop);

        db=new DBAdapter(getApplicationContext());
        final int idp=this.getIntent().getExtras().getInt("idp");
        final int idm=this.getIntent().getExtras().getInt("idm");
        final int op=this.getIntent().getExtras().getInt("op");

        if(op==1){
            titulo.setText("Actualizar medicamento");
            db.open();
            Cursor resultado=db.getMedicamento(idm);
            nom.setText(resultado.getString(0));
            fini.setText(resultado.getString(1));
            ffin.setText(resultado.getString(2));
            cred.setText(resultado.getString(3));
            resultado.close();
            db.close();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(op==1){
                    db.open();
                    db.updateMedicamento(idm,idp,
                            nom.getText().toString(),
                            fini.getText().toString(),
                            ffin.getText().toString(),
                            cred.getText().toString());
                    db.close();
                }else{
                    db.open();
                    db.insertMedicamento(idp,
                            nom.getText().toString(),
                            fini.getText().toString(),
                            ffin.getText().toString(),
                            cred.getText().toString());
                    db.close();
                }
                Intent intent=new Intent(addupActividad.this,Actividad.class);
                intent.putExtra("idp",idp);
                startActivity(intent);
            }
        });
    }
}
