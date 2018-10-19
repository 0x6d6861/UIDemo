package co.heri.uidemo.socket;

import android.app.Activity;
import android.widget.ImageView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.snackbar.Snackbar;


import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.TreeMap;

import co.heri.uidemo.R;

public class SocketComm {

    private Socket mSocket;
    private final Activity context;

    private final ImageView status;

    private TreeMap<String, Emitter.Listener> attachedEvents;

    public SocketComm(Activity context, String url, ImageView status) {
        this.context = context;
        this.status = status;
        try {
            this.mSocket = IO.socket(url);
        } catch (URISyntaxException e) {}

        this.connect();
    }

    public void attachEvent(String name, Emitter.Listener event){
        mSocket.on(name, event);
        this.attachedEvents.put(name, event);
    }

    public void connect(){
        if(!mSocket.connected()){
            mSocket.connect();
        }

        mSocket.on(Socket.EVENT_CONNECT, onConnection);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisConnection);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onDisConnection);
        mSocket.on(Socket.EVENT_ERROR, onErrorConnection);
    }

    public void disConnect(){
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnection);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisConnection);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onDisConnection);
        mSocket.off(Socket.EVENT_ERROR, onErrorConnection);

        for (TreeMap.Entry<String, Emitter.Listener> event : attachedEvents.entrySet()) {
            mSocket.off(event.getKey(), event.getValue());
        }

        attachedEvents = null;

    }

    public boolean isConnected(){
        return mSocket.connected();
    }


    private Emitter.Listener onConnection = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    status.setImageDrawable(context.getResources().getDrawable(R.drawable.online_status));
                }
            });
        }

    };

    private Emitter.Listener onDisConnection = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    status.setImageDrawable(context.getResources().getDrawable(R.drawable.offline_status));
                }
            });
        }
    };

    private Emitter.Listener onErrorConnection = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    status.setImageDrawable(context.getResources().getDrawable(R.drawable.idle_status));
                    Snackbar.make(status.getRootView(), "Application disconnected", Snackbar.LENGTH_LONG).show();
                }
            });
        }
    };
}
