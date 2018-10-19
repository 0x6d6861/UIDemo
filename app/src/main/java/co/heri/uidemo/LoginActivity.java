package co.heri.uidemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import co.heri.uidemo.Models.User;
import co.heri.uidemo.socket.SocketComm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    SocketComm mySocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MaterialButton mapBTN = findViewById(R.id.mapBTN);

        final ImageView online_status = findViewById(R.id.socket_status);

        mySocket = new SocketComm(this,"https://ui-demo.eu-gb.mybluemix.net/", online_status);

        mySocket.connect();


        mapBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v, "Proceed to login?", Snackbar.LENGTH_LONG).setAction("Proceed", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        myIntent.putExtra("name", "Heri Agape");
                        startActivity(myIntent);
                    }
                }).show();


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mySocket.disConnect();
    }
}
