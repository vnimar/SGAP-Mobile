package br.com.sgap.ui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.sgap.R;

public class PacientesActivity extends AppCompatActivity {
    EditText txtnomep, txttelefonep, txtemailp;
    TextView txtstatusPac;
    SQLiteDatabase db;
    ImageView imgprimeiro, imganterior, imgproximo, imgultimo;
    Button btvoltarp, btalterarp, btexlcuirp;
    int indice, id;
    Cursor c;
    DialogInterface.OnClickListener diAlteraInformacoes;
    DialogInterface.OnClickListener diExcluiRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes);

        txtnomep = findViewById(R.id.txtnomeP);
        txttelefonep = findViewById(R.id.txttelefoneP);
        txtemailp = findViewById(R.id.txtemailP);
        txtstatusPac = findViewById(R.id.txtstatus_paciente);

        imganterior = findViewById(R.id.imganterior);
        imgproximo = findViewById(R.id.imgproximo);
        imgprimeiro = findViewById(R.id.imgprimeiro);
        imgultimo = findViewById(R.id.imgultimo);

        btvoltarp = findViewById(R.id.btvoltarp);
        btalterarp = findViewById(R.id.btalterarp);
        btexlcuirp = findViewById(R.id.btexcluirp);

        try {
            db = openOrCreateDatabase("bd_sgap", Context.MODE_PRIVATE, null);
            CarregaDados();

            imgprimeiro.setOnClickListener(new View.OnClickListener() {
                @Override   public void onClick(View v) {
                    if(c.getCount() > 0){
                        c.moveToFirst();
                        indice = 1;
                        id = c.getInt(0);
                        txtnomep.setText(c.getString(1));
                        txttelefonep.setText(c.getString(2));
                        txtemailp.setText(c.getString(3));
                        txtstatusPac.setText(indice + " / " + c.getCount());
                    }
                }
            });

            imganterior.setOnClickListener(view -> {
                if (c.getCount() > 0) {
                    if (indice > 1) {
                        indice--;
                        c.moveToPrevious();
                        id = c.getInt(0);
                        txtnomep.setText(c.getString(1));
                        txttelefonep.setText(c.getString(2));
                        txtemailp.setText(c.getString(3));
                        txtstatusPac.setText(indice + " / " + c.getCount());
                    }
                }
            });

            imgproximo.setOnClickListener(view -> {
                if (c.getCount() > 0) {
                    if(indice != c.getCount()) {
                        indice++;
                        c.moveToNext();
                        id = c.getInt(0);
                        txtnomep.setText(c.getString(1));
                        txttelefonep.setText(c.getString(2));
                        txtemailp.setText(c.getString(3));
                        txtstatusPac.setText(indice + " / " + c.getCount());
                    }
                }
            });

            imgultimo.setOnClickListener(view -> {
                if (c.getCount() > 0) {
                    c.moveToLast();
                    indice = c.getCount();
                    id = c.getInt(0);
                    txtnomep.setText(c.getString(1));
                    txttelefonep.setText(c.getString(2));
                    txtemailp.setText(c.getString(3));
                    txtstatusPac.setText(indice + " / " + c.getCount());
                }
            });

            diAlteraInformacoes = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String nome = txtnomep.getText().toString();
                    String telefone = txttelefonep.getText().toString();
                    String email = txtemailp.getText().toString();

                    try {
                        ContentValues valor = new ContentValues();
                        valor.put("nome", nome);
                        valor.put("telefone", telefone);
                        valor.put("email", email);
                        db.update("pacientes", valor, "id=" + id, null);
                        MostraMensagem("Dados alterados com sucesso.");
                    }   catch(Exception e) {
                        MostraMensagem("Erro: " + e.toString());
                    }
                }
            };

            diExcluiRegistro = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.execSQL("delete from pacientes where id = " + id);
                    CarregaDados();
                    MostraMensagem("Dados excluidos com sucesso!");
                }
            };

            btalterarp.setOnClickListener(new View.OnClickListener() {
                @Override  public void onClick(View v) {
                    AlertDialog.Builder dialogo = new
                            AlertDialog.Builder(PacientesActivity.this);
                    dialogo.setTitle("Confirma");
                    dialogo.setMessage("Deseja alterar as informações");
                    dialogo.setNegativeButton("Não", null);
                    dialogo.setPositiveButton("Sim", diAlteraInformacoes);
                    dialogo.show();
                }
            });

            btexlcuirp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (c.getCount() > 0) {
                        android.app.AlertDialog.Builder dialogo = new
                                android.app.AlertDialog.Builder(PacientesActivity.this);
                        dialogo.setTitle("Confirma");
                        dialogo.setMessage("Deseja excluir esse registro ?");
                        dialogo.setNegativeButton("Não", null);
                        dialogo.setPositiveButton("Sim", diExcluiRegistro);
                        dialogo.show();
                    } else {
                        MostraMensagem("Não existem registros para excluir!");
                    }
                }
            });

        } catch (Exception e) {
            MostraMensagem("Erro: " + e);
        }
        btvoltarp.setOnClickListener(view -> {
            PacientesActivity.this.finish();
        });
    }

    public void CarregaDados() {
        c = db.query("pacientes", new String[]
                        {"id", "nome", "telefone", "email"},
                null, null, null, null, null);
        txtnomep.setText("");
        txttelefonep.setText("");
        txtemailp.setText("");
        if (c.getCount() > 0) {
            c.moveToFirst();
            indice = 1;
            id = c.getInt(0);
            txtnomep.setText(c.getString(1));
            txttelefonep.setText(c.getString(2));
            txtemailp.setText(c.getString(3));
            txtstatusPac.setText(indice + " / " + c.getCount());
        } else {
            txtstatusPac.setText("Nenhum Registro");
        }
    }

    public void MostraMensagem(String srt) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(PacientesActivity.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(srt);
        dialogo.setMessage("");
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }
}