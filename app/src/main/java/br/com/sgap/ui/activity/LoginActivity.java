package br.com.sgap.ui.activity;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.sgap.R;
import br.com.sgap.ui.MainActivity;


public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnLogin;
    SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        try {
            bd = openOrCreateDatabase("bd_sgap", Context.MODE_PRIVATE, null);
            bd.execSQL("CREATE TABLE IF NOT EXISTS usuarios(id INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT NOT NULL, senha TEXT NOT NULL)");

            // Inserir um usuário de teste se a tabela estiver vazia
            Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM usuarios", null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count == 0) {
                bd.execSQL("INSERT INTO usuarios (usuario, senha) VALUES ('admin', 'admin')");
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialog.Builder dialogo = new AlertDialog.Builder(LoginActivity.this);
            dialogo.setTitle("Aviso")
                    .setMessage("Falha no Armazenamento interno do App!")
                    .setNeutralButton("OK", null)
                    .show();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (authenticate(username, password)) {
                    // Salvar o estado de login usando SharedPreferences
                    SharedPreferences.Editor editor = getSharedPreferences("sgap", MODE_PRIVATE).edit();
                    editor.putBoolean("logged_in", true);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(LoginActivity.this);
                    dialogo.setTitle("Erro")
                            .setMessage("Usuário ou senha inválidos.")
                            .setNeutralButton("OK", null)
                            .show();
                }
            }
        });
    }

    private boolean authenticate(String username, String password) {
        Cursor cursor = null;
        try {
            cursor = bd.rawQuery("SELECT id FROM usuarios WHERE usuario = ? AND senha = ?", new String[]{username, password});
            boolean authenticated = cursor.getCount() > 0;
            return authenticated;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialog.Builder dialogo = new AlertDialog.Builder(LoginActivity.this);
            dialogo.setTitle("Erro")
                    .setMessage("Erro ao autenticar: " + e.getMessage())
                    .setNeutralButton("OK", null)
                    .show();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}