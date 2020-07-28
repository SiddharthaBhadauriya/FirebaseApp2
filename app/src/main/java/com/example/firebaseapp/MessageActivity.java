package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.firebaseapp.Adapter.MessageAdapter;
import com.example.firebaseapp.Model.Chat;
import com.example.firebaseapp.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
      TextView username;
      ImageView imageView;
      RecyclerView recyclerViewy;
      EditText msg_editText;
      ImageButton sendBtn;

      private MessageAdapter messageAdapter;
      List<Chat> mChat;
      //RecyclerView recyclerView;
      String msg;
      FirebaseUser fuser;
      DatabaseReference reference;
      Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
       //widgets
        imageView = findViewById(R.id.imageview_profile);
        username = findViewById(R.id.username);
        sendBtn = findViewById(R.id.btn_send);
        msg_editText = findViewById(R.id.text_send);

        recyclerViewy = findViewById(R.id.recycler_view);
        recyclerViewy.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewy.setLayoutManager(linearLayoutManager);
       // recyclerView.setAdapter(messageAdapter);



        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar2);
       /* setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        mChat = new ArrayList<>();
        intent = getIntent();
        final String userid = intent.getStringExtra("userid");
        //Toast.makeText(MessageActivity.this,userid,Toast.LENGTH_SHORT).show();
         fuser = FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(MessageActivity.this,fuser.toString(),Toast.LENGTH_SHORT).show();
        reference= FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);



     sendBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
              msg = msg_editText.getText().toString();
             if(!msg.equals(""))
             {
                 sendMessage(fuser.getUid(),userid,msg);
             }
             else {
                 //Toast.makeText(MessageActivity.this,"Enter some text",Toast.LENGTH_SHORT).show();
             }
             msg_editText.setText("");
         }
     });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                username.setText(user.getUsername());

                if((user.getImageURL()==null) ) {
                    imageView.setImageResource(R.mipmap.ic_launcher);

                }
                else {
                    Glide.with(MessageActivity.this)
                            .load(user.getImageURL())
                            .into(imageView);
                }

                readMessages(fuser.getUid(),userid,user.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void sendMessage(String sender, String receiver, String message)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("Receiver", receiver);
        hashMap.put("Message", message);

        reference.child("Chats").push().setValue(hashMap);
    }  Chat chat;
    private void readMessages( final String myid, final String userid, final String imageurl)
    {

        //Toast.makeText(MessageActivity.this,imageurl,Toast.LENGTH_SHORT).show();
       DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chats");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               mChat.clear();


                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {      chat = dataSnapshot.getValue(Chat.class);
               // System.out.println(snapshot.getValue()+"ram");
               if(chat.getReceiver()==null)
                   Toast.makeText(MessageActivity.this,"maa",Toast.LENGTH_SHORT).show();
                assert chat!=null;
                    //chat = snapshot1.getValue(Chat.class);
                   if(chat.getReceiver()==null)
                       Toast.makeText(MessageActivity.this,"insert",Toast.LENGTH_SHORT).show();
                        // Toast.makeText(MessageActivity.this,"Enter some text",Toast.LENGTH_SHORT).show();
                    if((chat.getReceiver()!=null) && (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid))) {
                        Toast.makeText(MessageActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        mChat.add(chat);
                    }
                    //Toast.makeText(MessageActivity.this,chat.getMessage(),Toast.LENGTH_SHORT).show();
                        messageAdapter = new MessageAdapter(getApplicationContext(), mChat, imageurl);
                        recyclerViewy.setAdapter(messageAdapter);
                    //Toast.makeText(MessageActivity.this,userid,Toast.LENGTH_SHORT).show();
                }
             //System.out.println("YEs");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
          System.out.println("failed badly");
            }
        });
    }

}