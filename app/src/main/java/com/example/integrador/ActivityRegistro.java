package com.example.integrador;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ActivityRegistro extends AppCompatActivity {
    private EditText nombre, contrasena;
    private Button btnRegistrar;
    private final String URL_API = "http://10.0.2.2/cambio.php"; // Cambia a la ruta real de tu PHP.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre = findViewById(R.id.Usuario);
        contrasena = findViewById(R.id.Password);
        btnRegistrar = findViewById(R.id.Cambiar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = nombre.getText().toString().trim();
                String pass = contrasena.getText().toString().trim();

                if (!usuario.isEmpty() && !pass.isEmpty()) {
                    realizarRegistroOLogin(usuario, pass);
                } else {
                    Toast.makeText(ActivityRegistro.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }private void realizarRegistroOLogin(String usuario, String pass) {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", usuario);
            jsonObject.put("contrasena", pass);
            Log.d("RequestData", "Datos enviados: " + jsonObject.toString());  // Verificar los datos enviados
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(URL_API)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(ActivityRegistro.this, "Error de conexión: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    Log.d("ServerResponse", "Respuesta del servidor: " + responseString); // Log de la respuesta del servidor

                    runOnUiThread(() -> {
                        try {
                            JSONObject responseJson = new JSONObject(responseString);
                            boolean success = responseJson.getBoolean("success");
                            String message = responseJson.getString("message");

                            if (success) {
                                Toast.makeText(ActivityRegistro.this, "Operación exitosa: " + message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ActivityRegistro.this, "Operación fallida: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ActivityRegistro.this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(ActivityRegistro.this, "Error del servidor: " + response.message(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

}
