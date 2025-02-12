package com.example.proyecto_last_shot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddJugadores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_add_jugadores);

        // Buscar el ImageView por su ID
        ImageView logo = findViewById(R.id.logo);
        ImageView imagenPrincipal = findViewById(R.id.imagenPrincipal);
        ImageView iconoOpcion2 = findViewById(R.id.iconoOpcion2);

        // Cargar las imágenes en segundo plano
        new DescargarImagen(logo).execute("https://via.placeholder.com/150");
        new DescargarImagen(imagenPrincipal).execute("https://via.placeholder.com/300");
        new DescargarImagen(iconoOpcion2).execute("https://via.placeholder.com/100");
    }

    // Clase interna para descargar imágenes en segundo plano
    private static class DescargarImagen extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DescargarImagen(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                // Conectar a la URL y obtener la imagen
                URL imageUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}
