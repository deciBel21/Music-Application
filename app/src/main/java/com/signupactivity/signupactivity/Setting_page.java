package com.signupactivity.signupactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Setting_page extends AppCompatActivity  {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    //TextView nametext;
    //TextView emailtext;
    String name;
    String email;
    User userprofile;
    DatabaseReference reffernce1;
    HashMap<String, List<String>> listDataChild;
    private FirebaseUser user1;
    String UserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        //nametext=(TextView)findViewById(R.id.textView2);
        //emailtext=(TextView)findViewById(R.id.textView3);
        expListView = (ExpandableListView) findViewById(R.id.settingspage);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        user1=FirebaseAuth.getInstance().getCurrentUser();
        UserID=user1.getUid();
         reffernce1 =FirebaseDatabase.getInstance().getReference("users").child(UserID);
        reffernce1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userprofile=snapshot.getValue(User.class);
                name=userprofile.fullName.trim();
                email=userprofile.email.trim();
               // nametext.setText("Name:"+""+name+".");
                //emailtext.setText("Email:"+""+email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void prepareListData() {


        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Social Media");
        listDataHeader.add("About");
        listDataHeader.add("Support");
        listDataHeader.add("Contact");
        listDataHeader.add("Founder");
        listDataHeader.add("Logout");
        listDataHeader.add("Policy");
        listDataHeader.add("Update");
        listDataHeader.add("Version");

        List<String> Version= new ArrayList<String>();
        Version.add("1.0.0");

        List<String> SocialMedia= new ArrayList<String>();
        SocialMedia.add("Youtube");
        SocialMedia.add("Instagram");
        SocialMedia.add("Twitter");

        List<String> About= new ArrayList<String>();
        About.add("Website");
        List<String> Support= new ArrayList<String>();
       Support.add("Enjoy the app and please give me suggestions to improve it by sending email.");
        List<String> Founder= new ArrayList<String>();
        Founder.add("Dave");
         List<String> Logout=new ArrayList<String>();
         Logout.add("Logout!");

        List<String> Policy=new ArrayList<String>();
        Policy.add("Hey guys this a random project created by me while learning Android, if you have any query please send me an email on the id provided in contact section.");
        List<String> Update=new ArrayList<String>();
        Update.add("Next Update:New Albums,New lofi Songs");
        List<String> Contact=new ArrayList<String>();
        Contact.add("decibelinc21@gmail.com");


        listDataChild.put(listDataHeader.get(0), SocialMedia);
        listDataChild.put(listDataHeader.get(1), About);
        listDataChild.put(listDataHeader.get(2), Support);
        listDataChild.put(listDataHeader.get(3), Contact);
        listDataChild.put(listDataHeader.get(4), Founder);
        listDataChild.put(listDataHeader.get(5),Logout);
        listDataChild.put(listDataHeader.get(6),Policy);
        listDataChild.put(listDataHeader.get(7),Update);
        listDataChild.put(listDataHeader.get(8), Version);

       expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
           @Override
           public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
               final String selected=(String)listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
               switch (selected)
               {
                   case "decibelinc21@gmail.com":
                       ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                       ClipData clipData=ClipData.newPlainText("Email","decibelinc21@gmail.com");
                       clipboardManager.setPrimaryClip(clipData);
                       Toast.makeText(Setting_page.this, "Email Copied!", Toast.LENGTH_SHORT).show();
                       break;
                   case "Twitter":
                       Intent intenttweet=new Intent(Setting_page.this,webviewwebsite.class);
                       intenttweet.putExtra("instalink","https://twitter.com/deciBel26311045");
                       startActivity(intenttweet);
                       break;
                   case "Instagram":
                       Intent intentinsta=new Intent(Setting_page.this,webviewwebsite.class);
                       intentinsta.putExtra("instalink","https://www.instagram.com/decibelinc21/");
                       startActivity(intentinsta);
                       break;
                   case "Youtube":
                       Intent intentyou=new Intent(Setting_page.this,webviewwebsite.class);
                       intentyou.putExtra("instalink","https://www.youtube.com/channel/UCzqDP7x5kDnFHBekgYkVpwg");
                       startActivity(intentyou);
                       break;
                   case "Website":
                       Intent intentweb=new Intent(Setting_page.this,webviewwebsite.class);
                       intentweb.putExtra("instalink","https://decibel21.github.io/deciBel21/");
                       startActivity(intentweb);
                       break;
                   case "Logout!":
                       FirebaseAuth.getInstance().signOut();
                      startActivity(new Intent(Setting_page.this,LoginActivity.class));
                      finish();
               }
               return true;

           }
       });
    }


}