package com.example.ghuraghuri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ghuraghuri.adapters.ChatAdapter;
import com.example.ghuraghuri.helpers.SendMessageInBg;
import com.example.ghuraghuri.interfaces.BotReply;
import com.example.ghuraghuri.models.Message;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChatBot extends AppCompatActivity implements BotReply {

    RecyclerView chatView;
    ChatAdapter chatAdapter;
    List<Message> messageList = new ArrayList<>();
    EditText editMessage;
    ImageButton btnSend;

    //dialogFlow
    private SessionsClient sessionsClient;
    private SessionName sessionName;
    private final String uuid = UUID.randomUUID().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        chatView = findViewById(R.id.chatView);
        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);

        chatAdapter = new ChatAdapter(messageList, this);
        chatView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override public void onClick(View view) {
                String message = editMessage.getText().toString();
                if (!message.isEmpty()) {
                    messageList.add(new Message(message, false));
                    editMessage.setText("");
                    sendMessageToBot(message);
                    Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged();
                    Objects.requireNonNull(chatView.getLayoutManager())
                            .scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(ChatBot.this, "Please enter text!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpBot();
    }

    private void setUpBot() {
        String TAG = "mainactivity";
        try {
            InputStream stream = this.getResources().openRawResource(R.raw.bot3);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
                    FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            sessionName = SessionName.of(projectId, uuid);

            //Log.d(TAG, "projectId : " + projectId);
            System.out.println(projectId);
        } catch (Exception e) {
            Log.d(TAG, "setUpBot: " + e.getMessage());
        }
    }

    private void sendMessageToBot(String message) {
        QueryInput input = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();
        new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void callback(DetectIntentResponse returnResponse) {
        if(returnResponse!=null) {
            //String botReply = returnResponse.getQueryResult().getFulfillmentText();
            System.out.println(returnResponse.getQueryResult().getFulfillmentMessagesCount());
            //System.out.println( returnResponse.getQueryResult().getFulfillment().getMessages());
            String botReply="";
            for (Intent.Message reply:returnResponse.getQueryResult().getFulfillmentMessagesList()){
                //System.out.println(reply);
                //reply.getText().toString();

                //JSONObject json = new JSONObject(reply);

                // Access and print the value of the "text" field
                //String textValue = json.getString("text");
                //System.out.println("Text Value: " + textValue);
                //System.out.println(reply.getText().getText(0));
                //botReply=botReply+reply.getText().getText(0);
                //System.out.println(reply.getSimpleResponses());
                if(reply.hasCard()){
                    System.out.println(reply.getCard().getTitle());
                    botReply=reply.getCard().getTitle();

                    /*Intent.Message.Card card = reply.getCard();
                    try {
                        //JSONObject cardJson = new JSONObject(reply.toString());
                        //System.out.println(cardJson);
                        String res=reply.toString();
                        JSONObject cardJson=new JSONObject(res);
                        //System.out.println(cardJson);
                        if (cardJson.has("subtitle")) {
                            System.out.println("Card Subtitle: " + cardJson.getString("subtitle"));
                            botReply = cardJson.getString("subtitle");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                    //System.out.println(reply.getCard().getButtons(0).getPostback());
                    //botReply=botReply+reply.getCard().getButtons(0).getPostback();
                }
                else
                {
                    botReply=reply.getText().getText(0);
                }

                //botReply=reply.getText().getText(0);
                if(!botReply.isEmpty()){
                    messageList.add(new Message(botReply, true));
                    chatAdapter.notifyDataSetChanged();
                    Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
                }else {
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
            /*if(!botReply.isEmpty()){
                messageList.add(new Message(botReply, true));
                chatAdapter.notifyDataSetChanged();
                Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
            }else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }*/
        } else {
            Toast.makeText(this, "failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }
}