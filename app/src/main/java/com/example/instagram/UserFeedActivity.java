package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearLayout);

        //photos posted by that user.
        Intent intent= getIntent();
        String activeUsername = intent.getStringExtra("username");
        setTitle(activeUsername +"'s feed");//to set title name of user which photo is there

        //get all the images posted by that user.
        ParseQuery<ParseObject> query= new ParseQuery<ParseObject>("Image");
        //we want to find all the images by the user name
        query.whereEqualTo("username",activeUsername);
        query.orderByDescending("createdAt"); // posted photo by pubished date
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null){
                    if (objects.size()>0){
                        for (ParseObject object:objects){
                            //we need to get the file i.e. the image over here that was created and download
                            ParseFile file= (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e==null&& data!= null){
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0 ,data.length);


                                        ImageView imageView = new ImageView(getApplicationContext());//getApplicationContext() becouse we are creating it.
                                        //set photo  width and height
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        //imageView.setImageDrawable(getResources().getDrawable(R.drawable.instagram));
                                        imageView.setImageBitmap(bitmap);

                                        linearLayout.addView(imageView);
                                    }
                                }
                            });
                        }
                    }else {
                        Toast.makeText(UserFeedActivity.this, " This feed is currently empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }
}