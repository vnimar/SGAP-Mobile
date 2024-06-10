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

public class GravaPacientesActivity extends AppCompatActivity {
    Button btcadastrarPaciente, btvoltarcp;
    EditText ednomep, edtelefonep, edemailp;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grava_pacientes);

        btcadastrarPaciente = findViewById(R.id.btcadastrarPaciente);
        btvoltarcp = findViewById(R.id.btvoltarcp);

        ednomep = findViewById(R.id.ednomeP);
        edtelefonep = findViewById(R.id.edtelefoneP);
        edemailp = findViewById(R.id.edemailP);

        try {
            db = openOrCreateDatabase("bd_sgap", Context.MODE_PRIVATE, null);
        }catch (Exception e) {
            e.printStackTrace();
            MostraMensagem("Erro no Armazenamento interno!");
        }

        btcadastrarPaciente.setOnClickListener(v -> {
            String nome = ednomep.getText().toString();
            String telefone = edtelefonep.getText().toString();
            String email = edemailp.getText().toString();

            try{
                db.execSQL("insert into pacientes(nome, " +
                        " telefone, email) values('" + nome + "', '"
                        + telefone + "','" + email + "')");
                MostraMensagem("Dados cadastrados com sucesso");
            }catch (Exception e){
                e.printStackTrace();
                MostraMensagem("Erro ao cadastrar paciente!");
            }
        });

        btvoltarcp.setOnClickListener(view -> {
            GravaPacientesActivity.this.finish();
        });
    }

    public void MostraMensagem(String mensagem) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(GravaPacientesActivity.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(mensagem);
        dialogo.setNeutralButton("OK", (dialogInterface, i) -> {
            finish();
        });
        dialogo.show();
    }
}