package com.example.appsgap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btcriabd, btCadastrarFuncionario, btfuncionarios;
    SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btcriabd = findViewById(R.id.btcriabd);
        btCadastrarFuncionario = findViewById(R.id.btCadastrarFuncionario);
        btfuncionarios = findViewById(R.id.btFuncionarios);

        btCadastrarFuncionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gravaFuncionariosActivity = new Intent(MainActivity.this,
                        GravaFuncionariosActivity.class);
                MainActivity.this.startActivity(gravaFuncionariosActivity);
            }
        });

        btfuncionarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent consultaFuncionarios = new Intent(MainActivity.this,
                        FuncionariosActivity.class);
                MainActivity.this.startActivity(consultaFuncionarios);
            }
        });

        btcriabd.setOnClickListener(view -> {
            try {
                bd = openOrCreateDatabase("bd_sgap", Context.MODE_PRIVATE, null);
                bd.execSQL("create table if not exists funcionarios(numreg integer primary key" +
                        " autoincrement, nome text not null, telefone text not null, " +
                        " email text not null, cargo text not null)");
                bd.execSQL("create table if not exists pacientes(id integer primary key" +
                        " autoincrement, nome text not null, telefone text not null, email text not null)");
                bd.execSQL("create table if not exists consultas(id integer primary key" +
                        " autoincrement, paciente_id integer not null, data text not null, " +
                        " observacoes text, foreign key(paciente_id) references pacientes(id))");

                AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
                dialogo.setTitle("Aviso")
                        .setMessage("Banco de dados criado com sucesso!")
                        .setNeutralButton("OK", null)
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}