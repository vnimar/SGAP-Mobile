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

public class AtendimentoActivity extends AppCompatActivity {
    EditText txtpaciente, txtmedico, txtdata, txtobservacao;
    TextView txtstatusAtendimento;
    SQLiteDatabase db;
    ImageView imgprimeiro, imganterior, imgproximo, imgultimo;
    Button btvoltar, btalterar, btexlcuir;
    int indice, id;
    Cursor c;
    DialogInterface.OnClickListener diAlteraInformacoes;
    DialogInterface.OnClickListener diExcluiRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atendimento);

        txtpaciente = findViewById(R.id.paciente);
        txtmedico = findViewById(R.id.medico);
        txtdata = findViewById(R.id.data);
        txtobservacao = findViewById(R.id.observacoes);
        txtstatusAtendimento = findViewById(R.id.txtstatus_atendimento);

        imganterior = findViewById(R.id.imganterior);
        imgproximo = findViewById(R.id.imgproximo);
        imgprimeiro = findViewById(R.id.imgprimeiro);
        imgultimo = findViewById(R.id.imgultimo);

        btvoltar = findViewById(R.id.btvoltara);
        btalterar = findViewById(R.id.btalteraratendimento);
        btexlcuir = findViewById(R.id.btexcluiratendimento);

        try {
            db = openOrCreateDatabase("bd_sgap", Context.MODE_PRIVATE, null);
            CarregaDados();

            imgprimeiro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        indice = 1;
                        id = c.getInt(0);
                        txtpaciente.setText(c.getString(1));
                        txtmedico.setText(c.getString(2));
                        txtdata.setText(c.getString(3));
                        txtobservacao.setText(c.getString(4));
                        txtstatusAtendimento.setText(indice + " / " + c.getCount());
                    }
                }
            });

            imganterior.setOnClickListener(view -> {
                if (c.getCount() > 0) {
                    if (indice > 1) {
                        indice--;
                        c.moveToPrevious();
                        id = c.getInt(0);
                        txtpaciente.setText(c.getString(1));
                        txtmedico.setText(c.getString(2));
                        txtdata.setText(c.getString(3));
                        txtobservacao.setText(c.getString(4));
                        txtstatusAtendimento.setText(indice + " / " + c.getCount());
                    }
                }
            });

            imgproximo.setOnClickListener(view -> {
                if (c.getCount() > 0) {
                    if (indice != c.getCount()) {
                        indice++;
                        c.moveToNext();
                        id = c.getInt(0);
                        txtpaciente.setText(c.getString(1));
                        txtmedico.setText(c.getString(2));
                        txtdata.setText(c.getString(3));
                        txtobservacao.setText(c.getString(4));
                        txtstatusAtendimento.setText(indice + " / " + c.getCount());
                    }
                }
            });

            imgultimo.setOnClickListener(view -> {
                if (c.getCount() > 0) {
                    c.moveToLast();
                    indice = c.getCount();
                    id = c.getInt(0);
                    txtpaciente.setText(c.getString(1));
                    txtmedico.setText(c.getString(2));
                    txtdata.setText(c.getString(3));
                    txtobservacao.setText(c.getString(4));
                    txtstatusAtendimento.setText(indice + " / " + c.getCount());
                }
            });

            diAlteraInformacoes = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String pacienteNome = txtpaciente.getText().toString();
                    String medicoNome = txtmedico.getText().toString();
                    String data = txtdata.getText().toString();
                    String observacao = txtobservacao.getText().toString();

                    try {
                        Cursor pacienteCursor = db.rawQuery("SELECT id FROM pacientes WHERE nome = ?", new String[]{pacienteNome});
                        int pacienteId = -1;
                        if (pacienteCursor.moveToFirst()) {
                            pacienteId = pacienteCursor.getInt(0);
                        }
                        pacienteCursor.close();

                        Cursor medicoCursor = db.rawQuery("SELECT numreg FROM funcionarios WHERE nome = ?", new String[]{medicoNome});
                        int medicoId = -1;
                        if (medicoCursor.moveToFirst()) {
                            medicoId = medicoCursor.getInt(0);
                        }
                        medicoCursor.close();

                        if (pacienteId == -1 || medicoId == -1) {
                            MostraMensagem("Paciente ou médico não encontrado.");
                            return;
                        }

                        ContentValues valor = new ContentValues();
                        valor.put("paciente_id", pacienteId);
                        valor.put("medico_id", medicoId);
                        valor.put("data", data);
                        valor.put("observacoes", observacao);

                        db.update("atendimentos", valor, "id=" + id, null);
                        MostraMensagem("Dados alterados com sucesso.");

                    } catch (Exception e) {
                        MostraMensagem("Erro: " + e.toString());
                    }
                }
            };

            btalterar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogo = new
                            AlertDialog.Builder(AtendimentoActivity.this);
                    dialogo.setTitle("Confirma");
                    dialogo.setMessage("Deseja alterar as informações");
                    dialogo.setNegativeButton("Não", null);
                    dialogo.setPositiveButton("Sim", diAlteraInformacoes);
                    dialogo.show();
                }
            });

            diExcluiRegistro = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.execSQL("delete from atendimentos where id = " + id);
                    CarregaDados();
                    MostraMensagem("Dados excluidos com sucesso!");
                }
            };

            btexlcuir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (c.getCount() > 0) {
                        android.app.AlertDialog.Builder dialogo = new
                                android.app.AlertDialog.Builder(AtendimentoActivity.this);
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

        btvoltar.setOnClickListener(view -> {
            AtendimentoActivity.this.finish();
        });
    }

    public void CarregaDados() {
        String query = "SELECT atendimentos.id, pacientes.nome AS paciente_nome, funcionarios.nome AS medico_nome, atendimentos.data, atendimentos.observacoes " +
                "FROM atendimentos " +
                "JOIN pacientes ON atendimentos.paciente_id = pacientes.id " +
                "JOIN funcionarios ON atendimentos.medico_id = funcionarios.numreg";
        c = db.rawQuery(query, null);

        txtpaciente.setText("");
        txtmedico.setText("");
        txtdata.setText("");
        txtobservacao.setText("");
        if (c.getCount() > 0) {
            c.moveToFirst();
            indice = 1;
            id = c.getInt(0);
            txtpaciente.setText(c.getString(1));
            txtmedico.setText(c.getString(2));
            txtdata.setText(c.getString(3));
            txtobservacao.setText(c.getString(4));
            txtstatusAtendimento.setText(indice + " / " + c.getCount());
        } else {
            txtstatusAtendimento.setText("Nenhum Registro");
        }
    }

    public void MostraMensagem(String srt) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(AtendimentoActivity.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(srt);
        dialogo.setMessage("");
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }
}