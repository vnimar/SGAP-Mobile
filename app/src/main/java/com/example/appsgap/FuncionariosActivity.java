package com.example.appsgap;

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

public class FuncionariosActivity extends AppCompatActivity {
    EditText txtnome, txttelefone, txtemail, txtcargo;
    TextView txtstatusFunc;
    SQLiteDatabase db;
    ImageView imgprimeiro, imganterior, imgproximo, imgultimo;
    Button btvoltar, btalterar, btexlcuir;
    int indice, numreg;
    Cursor c;
    DialogInterface.OnClickListener diAlteraInformacoes;
    DialogInterface.OnClickListener diExcluiRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcionarios);

        txtnome = findViewById(R.id.txtnome);
        txttelefone = findViewById(R.id.txttelefone);
        txtemail = findViewById(R.id.txtemail);
        txtcargo = findViewById(R.id.txtcargo);
        txtstatusFunc = findViewById(R.id.txtstatus_funcionario);

        imganterior = findViewById(R.id.imganterior);
        imgproximo = findViewById(R.id.imgproximo);
        imgprimeiro = findViewById(R.id.imgprimeiro);
        imgultimo = findViewById(R.id.imgultimo);

        btvoltar = findViewById(R.id.btvoltar);
        btalterar = findViewById(R.id.btalterarfuncionario);
        btexlcuir = findViewById(R.id.btexcluirfuncionario);

        try {
            db = openOrCreateDatabase("bd_sgap", Context.MODE_PRIVATE, null);
            CarregaDados();

        imgprimeiro.setOnClickListener(new View.OnClickListener() {
            @Override   public void onClick(View v) {
                if(c.getCount() > 0){
                    c.moveToFirst();
                    indice = 1;
                    numreg = c.getInt(0);
                    txtnome.setText(c.getString(1));
                    txttelefone.setText(c.getString(2));
                    txtemail.setText(c.getString(3));
                    txtcargo.setText(c.getString(4));
                    txtstatusFunc.setText(indice + " / " + c.getCount());
                }
            }
        });

            imganterior.setOnClickListener(view -> {
                if (c.getCount() > 0) {
                    if (indice > 1) {
                        indice--;
                        c.moveToPrevious();
                        numreg = c.getInt(0);
                        txtnome.setText(c.getString(1));
                        txttelefone.setText(c.getString(2));
                        txtemail.setText(c.getString(3));
                        txtcargo.setText(c.getString(4));
                        txtstatusFunc.setText(indice + " / " + c.getCount());
                    }
                }
            });

            imgproximo.setOnClickListener(view -> {
                if (c.getCount() > 0) {
                    if(indice != c.getCount()) {
                        indice++;
                        c.moveToNext();
                        numreg = c.getInt(0);
                        txtnome.setText(c.getString(1));
                        txttelefone.setText(c.getString(2));
                        txtemail.setText(c.getString(3));
                        txtcargo.setText(c.getString(4));
                        txtstatusFunc.setText(indice + " / " + c.getCount());
                    }
                }
            });

            imgultimo.setOnClickListener(view -> {
                if (c.getCount() > 0) {
                    c.moveToLast();
                    indice = c.getCount();
                    numreg = c.getInt(0);
                    txtnome.setText(c.getString(1));
                    txttelefone.setText(c.getString(2));
                    txtemail.setText(c.getString(3));
                    txtcargo.setText(c.getString(4));
                    txtstatusFunc.setText(indice + " / " + c.getCount());
                }
            });

        diAlteraInformacoes = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Altera as informações do registro na tabela
                String nome = txtnome.getText().toString();
                String telefone = txttelefone.getText().toString();
                String email = txtemail.getText().toString();
                String cargo = txtcargo.getText().toString();

                try {
                    ContentValues valor = new ContentValues();
                    valor.put("nome", nome);
                    valor.put("telefone", telefone);
                    valor.put("email", email);
                    valor.put("cargo", cargo);
                    db.update("funcionarios", valor, "numreg=" + numreg, null);
                    MostraMensagem("Dados alterados com sucesso.");
                }   catch(Exception e) {
                    MostraMensagem("Erro: " + e.toString());
                }
            }
        };

        diExcluiRegistro = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.execSQL("delete from funcionarios where numreg = " + numreg);
                CarregaDados();
                MostraMensagem("Dados excluidos com sucesso!");
            }
        };

        btalterar.setOnClickListener(new View.OnClickListener() {
            @Override  public void onClick(View v) {
                AlertDialog.Builder dialogo = new
                        AlertDialog.Builder(FuncionariosActivity.this);
                dialogo.setTitle("Confirma");
                dialogo.setMessage("Deseja alterar as informações");
                dialogo.setNegativeButton("Não", null);
                dialogo.setPositiveButton("Sim", diAlteraInformacoes);
                dialogo.show();
            }
        });

        btexlcuir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c.getCount() > 0) {
                    android.app.AlertDialog.Builder dialogo = new
                            android.app.AlertDialog.Builder(FuncionariosActivity.this);
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

        }catch (Exception e) {
            MostraMensagem("Erro: " + e);
        }

        btvoltar.setOnClickListener(view -> {
            FuncionariosActivity.this.finish();
        });
    }
    public void CarregaDados(){
        c = db.query("funcionarios",new String []
                        {"numreg","nome","telefone","email","cargo"},
                null,null,null,null,null);
        txtnome.setText("");
        txttelefone.setText("");
        txtemail.setText("");
        if(c.getCount() > 0) {
            c.moveToFirst();
            indice = 1;
            numreg = c.getInt(0);
            txtnome.setText(c.getString(1));
            txttelefone.setText(c.getString(2));
            txtemail.setText(c.getString(3));
            txtcargo.setText(c.getString(4));
            txtstatusFunc.setText(indice + " / " + c.getCount());
        }
        else {
            txtstatusFunc.setText("Nenhum Registro");
        }
    }
    public void MostraMensagem(String srt) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(FuncionariosActivity.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(srt);
        dialogo.setMessage("");
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }
}