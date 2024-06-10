package br.com.sgap.ui.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.sgap.R;

public class GravaAtendimentosActivity extends AppCompatActivity {
    Button btcadastrarAtendimento, btvoltarca;
    EditText edpaciente, edmedico, eddata, edobservacao;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grava_atendimentos);

        btcadastrarAtendimento = findViewById(R.id.btcadastrarAtendimento);
        btvoltarca = findViewById(R.id.btvoltarca);

        edpaciente = findViewById(R.id.pacienteid);
        edmedico = findViewById(R.id.medicoid);
        eddata = findViewById(R.id.data);
        edobservacao = findViewById(R.id.observacoes);

        try {
            db = openOrCreateDatabase("bd_sgap", Context.MODE_PRIVATE, null);
        } catch (Exception e) {
            MostraMensagem("Erro: " + e);
        }

        btcadastrarAtendimento.setOnClickListener(v -> {
            String paciente_id = edpaciente.getText().toString();
            String medico_id = edmedico.getText().toString();
            String data = eddata.getText().toString();
            String observacoes = edobservacao.getText().toString();

            try {
                if (paciente_id.isEmpty() || medico_id.isEmpty()) {
                    MostraMensagem("Por favor, preencha os IDs do paciente e do médico.");
                    return;
                }
                db.execSQL("INSERT INTO atendimentos(paciente_id, medico_id, data, observacoes) " +
                        "VALUES('" + paciente_id + "', '" + medico_id + "', '" + data + "', '" + observacoes + "')");
                MostraMensagem("Dados cadastrados com sucesso");
            } catch (Exception e) {
                MostraMensagem("Error: " + e);
            }
        });

        btvoltarca.setOnClickListener(view -> {
            GravaAtendimentosActivity.this.finish();
        });
    }

    public void MostraMensagem(String srt) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(GravaAtendimentosActivity.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(srt);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }
}