package coms309.sb_c_4_cydisc;

import android.os.Bundle;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.Serializable;
import java.net.URISyntaxException;


/**
 * Created by David Kirshenbaum on 11/7/2017.
 */

/**
 * Class for connecting socket to server
 */
public class SocketManager implements Serializable{
    Socket socket;
    public String socketLobbyId;
    JSONArray scores;


    /**
     * Create new SocketManager and connect to socket
     */
    public SocketManager()
    {
        try{
            socket = IO.socket("http://proj-309-sb-c-4.cs.iastate.edu:3000");
            socket.connect();
        }catch(URISyntaxException e){
            e.printStackTrace();
        }
        socket.on("getLobbyId", getLobbyId);
        socket.on("returnedScores", returnedScores);


    }

    /**
     * get the socket connection
     * @return The socket
     */
    public Socket getSocket()
    {
        return socket;
    }

    /**
     * Send scores to the server
     * @param holeNumber Hole number the user is on
     * @param holeScore Score the user got on the hole
     * @param username Username of the user
     */
    public void sendScore(int holeNumber, int holeScore, String username)
    {
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("hole", holeNumber);
            json.put("score", holeScore);
            json.put("lobbyId", socketLobbyId);
        }catch(JSONException e){
            e.printStackTrace();
        }

        socket.emit("addScore", json);
    }

    /**
     * Get an array of the scores and names of the players
     * @param lobbyId The lobby id the user is currently in
     */
    public void getScores(String lobbyId)
    {
        JSONObject json = new JSONObject();
        try {
            json.put("lobbyId", lobbyId);
        }catch(JSONException e){
            e.printStackTrace();
        }
        socket.emit("getScores", json.toString());
    }

    /**
     * Save your scores to the database
     * @param username username of the user
     */
    public void saveGameToDatabase(String username)
    {
        JSONObject json = new JSONObject();
        try{
            json.put("username", username);

        }catch(JSONException e){
            e.printStackTrace();
        }

        socket.emit("saveGameToDatabase", json.toString());
    }

    /**
     * Create a new lobby on the server
     * @param username username of the user
     */
    public void createLobby(String username)
    {
        JSONObject json = new JSONObject();
        try{
            json.put("username", username);

        }catch(JSONException e){
            e.printStackTrace();
        }

        socket.emit("newLobby", json.toString());
    }

    /**
     * Insert the game into  the database
     * @param username Username of the user
     * @param lobbyId Lobby id of the user that they are in
     */
    public void insertIntoDatabase(String username, String lobbyId)
    {
        JSONObject json = new JSONObject();
        try {
            json.put("lobbyId", lobbyId);
            json.put("username", username);
        }catch(JSONException e){
            e.printStackTrace();
        }
        socket.emit("saveGameToDatabase", json.toString());
    }

    /**
     * Join an existing lobby on the server
     * @param lobbyId The lobby the user wants to join
     * @param username The username of the user
     */
    public void joinLobby(String lobbyId, String username)
    {
        JSONObject json = new JSONObject();
        try {
            json.put("lobbyId", lobbyId);
            json.put("username", username);
        }catch(JSONException e){
            e.printStackTrace();
        }
        socket.emit("joinLobby", json.toString());
    }

    /**
     * Listen for the lobby of a newly created lobby
     */
    private Emitter.Listener getLobbyId = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            String lobbyId = (String) args[0];
            socketLobbyId = lobbyId;
        }
    };

    /**
     * Listen for the scores to be returned
     */
    private Emitter.Listener returnedScores = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            JSONArray data = (JSONArray) args[0];


            scores = data;


        }
    };

}
