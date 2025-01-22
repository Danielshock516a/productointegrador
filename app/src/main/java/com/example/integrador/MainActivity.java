package com.example.integrador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText nombre, contraseña;
    private Button btnLogin;
    private TextView tvResultado;
    private final String URL_API = "http://10.0.2.2/login.php"; // Cambia por la URL de tu servidor PHP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent CambiarContraseña = new Intent(MainActivity.this, ActivityRegistro.class);


        // Enlazar vistas
        nombre = findViewById(R.id.Usuario); // EditText para el nombre
        contraseña = findViewById(R.id.Password); // EditText para la contraseña
        btnLogin = findViewById(R.id.Ingresar); // Botón de inicio de sesión
        tvResultado = findViewById(R.id.tvResultado); // TextView para mostrar resultados
        Button btnCambiarContraseña = findViewById(R.id.cambio);

        // Configurar el botón de login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = nombre.getText().toString().trim();
                String contraseñaUsuario = contraseña.getText().toString().trim();

                if (!nombreUsuario.isEmpty() && !contraseñaUsuario.isEmpty()) {
                    realizarLogin(nombreUsuario, contraseñaUsuario);
                } else {
                    tvResultado.setText("Por favor, completa todos los campos.");
                }
            }
        });
        btnCambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CambiarContraseña);
            }
        });
    }

    private void realizarLogin(String nombreUsuario, String contraseñaUsuario) {
        // Crear cliente OkHttp
        OkHttpClient client = new OkHttpClient();

        // Crear cuerpo JSON
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", nombreUsuario); // Cambiar "usuario" a "nombre"
            jsonObject.put("contraseña", contraseñaUsuario); // Cambiar "password" a "contraseña"
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear RequestBody
        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        // Crear Request
        Request request = new Request.Builder()
                .url(URL_API)
                .post(body)
                .build();

        // Realizar la solicitud
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> tvResultado.setText("Error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();

                    try {
                        JSONObject responseJson = new JSONObject(responseString);
                        boolean success = responseJson.getBoolean("success");
                        String message = responseJson.getString("message");

                        runOnUiThread(() -> {
                            if (success) {
                                tvResultado.setText("Login exitoso: " + message);
                                Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                // Aquí puedes redirigir a otra actividad si es necesario
                                Intent intent = new Intent(MainActivity.this, TablaActivity.class);
                                startActivity(intent);
                            } else {
                                tvResultado.setText("Error: " + message);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> tvResultado.setText("Error al procesar la respuesta."));
                    }
                } else {
                    runOnUiThread(() -> tvResultado.setText("Error: " + response.message()));
                }
            }
        });
    }
}
