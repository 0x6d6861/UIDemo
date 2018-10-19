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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LoginActivity extends AppCompatActivity {

    SocketComm mySocket;

    User[] myUsers = {
            new User("Peter Kamau", 19, "agape@live.fr", "25/05/2001", "Male", "0700928129"),
            new User("Joanna Terry", 20, "peter@live.fr", "25/05/1997", "Female", "0700928128"),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MaterialButton mapBTN = findViewById(R.id.mapBTN);

        final ImageView online_status = findViewById(R.id.socket_status);

        mySocket = new SocketComm(this,"https://ui-demo.eu-gb.mybluemix.net/", online_status);

        mySocket.connect();


        ArrayAdapter<User> usersAdapter =
                new ArrayAdapter<User>(this, 0, myUsers) {
                    @Override
                    public View getView(int position,
                                        View convertView,
                                        ViewGroup parent) {

                        User currentUser = myUsers[position];

                        if(convertView == null) {
                            convertView = getLayoutInflater()
                                    .inflate(R.layout.user_list, null, false);
                        }

                        TextView user_name = convertView.findViewById(R.id.user_name);
                        TextView user_email = convertView.findViewById(R.id.user_email);

                        user_name.setText(currentUser.getName());
                        user_email.setText(currentUser.getEmail());

                        return convertView;

                    }
                };

        ListView list_of_users = findViewById(R.id.list_of_users);
        list_of_users.setAdapter(usersAdapter);


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
