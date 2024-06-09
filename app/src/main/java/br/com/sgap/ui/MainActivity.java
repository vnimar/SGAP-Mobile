package br.com.sgap.ui;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.sgap.R;

import br.com.sgap.ui.activity.AtendimentoActivity;
import br.com.sgap.ui.activity.FuncionariosActivity;
import br.com.sgap.ui.activity.GravaAtendimentosActivity;
import br.com.sgap.ui.activity.GravaFuncionariosActivity;
import br.com.sgap.ui.activity.GravaPacientesActivity;
import br.com.sgap.ui.activity.PacientesActivity;

public class MainActivity extends AppCompatActivity {
    Button btcriabd, btCadastrarFuncionario, btfuncionarios, btCadastrarPaciente, btpacientes, btGerarAtendimentos, btatendimentos;
    SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btcriabd = findViewById(R.id.btcriabd);
        btCadastrarFuncionario = findViewById(R.id.btCadastrarFuncionario);
        btfuncionarios = findViewById(R.id.btFuncionarios);
        btCadastrarPaciente = findViewById(R.id.btCadPac);
        btpacientes = findViewById(R.id.btPacientes);
        btGerarAtendimentos = findViewById(R.id.btGerarAtendimento);
        btatendimentos = findViewById(R.id.btAtendimentos);

        btCadastrarFuncionario.setOnClickListener(v -> {
            Intent gravaFuncionariosActivity = new Intent(MainActivity.this,
                    GravaFuncionariosActivity.class);
            MainActivity.this.startActivity(gravaFuncionariosActivity);
        });

        btCadastrarPaciente.setOnClickListener(v -> {
            Intent gravaPaciente = new Intent(MainActivity.this,
                    GravaPacientesActivity.class);
            MainActivity.this.startActivity(gravaPaciente);
        });

        btGerarAtendimentos.setOnClickListener(v -> {
            Intent gravaAtendimento = new Intent(MainActivity.this,
                    GravaAtendimentosActivity.class);
            MainActivity.this.startActivity(gravaAtendimento);
        });

        btfuncionarios.setOnClickListener(v -> {
            Intent consultaFuncionarios = new Intent(MainActivity.this,
                    FuncionariosActivity.class);
            MainActivity.this.startActivity(consultaFuncionarios);
        });

        btpacientes.setOnClickListener(v -> {
            Intent consultaPaciente = new Intent(MainActivity.this,
                    PacientesActivity.class);
            MainActivity.this.startActivity(consultaPaciente);
        });

        btatendimentos.setOnClickListener(v -> {
            Intent consultaAtendimento = new Intent(MainActivity.this,
                    AtendimentoActivity.class);
            MainActivity.this.startActivity(consultaAtendimento);
        });

        btcriabd.setOnClickListener(view -> {
            try {
                bd = openOrCreateDatabase("bd_sgap", Context.MODE_PRIVATE, null);
                bd.execSQL("create table if not exists funcionarios(numreg integer primary key" +
                        " autoincrement, nome text not null, telefone text not null, " +
                        " email text not null, cargo text not null)");
                bd.execSQL("create table if not exists pacientes(id integer primary key" +
                        " autoincrement, nome text not null, telefone text not null, email text not null)");
                bd.execSQL("create table if not exists atendimentos(id integer primary key" +
                        " autoincrement, paciente_id integer not null, medico_id integer not null, data text not null, " +
                        " observacoes text, foreign key(paciente_id) references pacientes(id)," +
                        " foreign key(medico_id) references funcionarios(numreg))");

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