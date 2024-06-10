package br.com.sgap.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import br.com.sgap.R;

public class GravaFuncionariosActivity extends Activity {
    Button btcadastrarFuncionario, btvoltarf;
    EditText ednome, edtelefone, edemail, edcargo;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grava_funcionarios);

        btcadastrarFuncionario = findViewById(R.id.btcadastrarFuncionario);
        btvoltarf = findViewById(R.id.btvoltarcf);

        ednome = findViewById(R.id.ednome);
        edtelefone = findViewById(R.id.edtelefone);
        edemail = findViewById(R.id.edemail);
        edcargo = findViewById(R.id.edcargo);

        try {
            db = openOrCreateDatabase("bd_sgap", Context.MODE_PRIVATE, null);
        } catch (Exception e) {
            e.printStackTrace();
            MostraMensagem("Erro ao acessar o armazenamento!");
        }

        btcadastrarFuncionario.setOnClickListener(v -> {
            String nome = ednome.getText().toString();
            String telefone = edtelefone.getText().toString();
            String email = edemail.getText().toString();
            String cargo = edcargo.getText().toString();

            try {
                db.execSQL("insert into funcionarios(nome, " +
                        " telefone, email, cargo) values('" + nome + "', '"
                        + telefone + "','" + email + "','" + cargo + "')");
                MostraMensagem("Dados cadastrados com sucesso");
            } catch (Exception e) {
                e.printStackTrace();
                MostraMensagem("Erro ao cadastrar funcionário!");
            }
        });

        btvoltarf.setOnClickListener(view -> {
            GravaFuncionariosActivity.this.finish();
        });
    }

    public void MostraMensagem(String mensagem) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(GravaFuncionariosActivity.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(mensagem);
        dialogo.setNeutralButton("OK", (dialogInterface, i) -> {
            finish();
        });
        dialogo.show();
    }
}