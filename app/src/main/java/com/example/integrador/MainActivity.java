package com.example.integrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView textoLogin;
    TextView textoPassword;
    EditText Usuario;
    EditText Password;
    Button Ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textoLogin = findViewById(R.id.textoLogin);
        textoPassword = findViewById(R.id.Password);
        Usuario = findViewById(R.id.Usuario);
        Password = findViewById(R.id.Password);
        Ingresar = findViewById(R.id.Ingresar);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = Usuario.getText().toString();
                String pass = Password.getText().toString();
                boolean login = comprobar(user, pass);
                if (login) {
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Create the Intent here, after successful login
                    //Intent.putExtra("bloquearEditText", true);
                    Intent TablaActivity = new Intent(MainActivity.this, TablaActivity.class);
                    startActivity(TablaActivity);
                } else {
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    protected boolean comprobar(String user, String pass) {
        return Objects.equals(user, "profe") && Objects.equals(pass, "1234");
    }
}