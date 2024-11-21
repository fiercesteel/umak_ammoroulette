package com.example.ammoroulette;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Declaring Components
    Handler handler = new Handler();
    AnimationDrawable animation;
    SoundPool soundPool;
    MediaPlayer mediaPlayer;
    Snackbar captionsnackbar, descsnackbar;
    View customSnackView;
    int mediaPlayback, volume = 1;
    LinearLayout bulletcontainer, left_inventory, right_inventory, dealer_items;
    LinearLayout flash;
    ImageView shotgun;
    TextView aim_user, aim_dealer;
    Button user, dealer;

    //Declaring Variables
    String[] captions, desc;
    String username = "", user_who = "", dealer_who = "";
    int level = 1, round = 1, userhp = 0, dealerhp = 0;
    int bullet, loop = 0, click, live, blank, holster, reload, load;
    boolean user_inventory = false, dealer_inventory = false;

    //Bullet mechanics stuff
    int liveround = 0, blankround = 0;
    ArrayList<String> magazine = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the App to be Sensor Landscape
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        //Setting up Volume based on Hardware
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //Setting up Background Music
        mediaPlayer = MediaPlayer.create(this, R.raw.start);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(volume,volume);
        mediaPlayer.start();

        //Building SoundPool for SFX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else {
            soundPool = new SoundPool.Builder().build();
        }

        //----------------------------------------------------------------------------------------------------------//
        //------------------------------------------------ ONCREATE ------------------------------------------------//
        //----------------------------------------------------------------------------------------------------------//

        //Initializing SFX
        click = soundPool.load(this, R.raw.click, 1);
        live = soundPool.load(this, R.raw.live, 1);
        blank = soundPool.load(this, R.raw.blank, 1);
        reload = soundPool.load(this, R.raw.reload, 1);
        holster = soundPool.load(this, R.raw.holster, 1);
        load = soundPool.load(this, R.raw.load, 1);

        //Initializing Components
        //FKBlurView blurView = findViewById(R.id.blur);
        flash = findViewById(R.id.flash);
        shotgun = findViewById(R.id.main_screen);
        aim_user = findViewById(R.id.self_aim_button);
        aim_dealer = findViewById(R.id.dealer_aim_button);
        user = findViewById(R.id.show_user_inventory);
        dealer = findViewById(R.id.show_dealer_inventory);
        bulletcontainer = findViewById(R.id.bulletcontainer);
        left_inventory = findViewById(R.id.left_inventory);
        right_inventory = findViewById(R.id.right_inventory);
        dealer_items = findViewById(R.id.dealer_items);
        captions = getResources().getStringArray(R.array.captions);
        desc = getResources().getStringArray(R.array.desc);

        //Setting up SnackBar for captions
        captionsnackbar = Snackbar.make(shotgun, "", Snackbar.LENGTH_INDEFINITE);
        customSnackView = getLayoutInflater().inflate(R.layout.caption, null);
        captionsnackbar.getView().setBackgroundColor(Color.TRANSPARENT);

        //Setting SnackBar layout
        @SuppressLint("RestrictedApi") Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) captionsnackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        snackbarLayout.addView(customSnackView, 0);

        //Setting the Animation
        shotgun.setImageResource(R.drawable.a_intro);
        animation = (AnimationDrawable)shotgun.getDrawable();
        animation.setOneShot(true);
        animation.start();

        //Waiting for animation to be done
        handler.postDelayed(() -> {

            //Animating captions
            Button button = customSnackView.findViewById(R.id.descbutton);
            typewriter text = new typewriter(button, getApplicationContext(), volume);
            text.animateText(captions[0]);
            button.setOnClickListener(v -> {
                captionsnackbar.dismiss();
                text.stop();

                waiver fragment = new waiver();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in)
                        .replace(R.id.fragment_container, fragment)
                        .setReorderingAllowed(true)
                        .commit();
            });
            captionsnackbar.show();
        }, animation.getNumberOfFrames() * 50L);

//        //Listeners for Inventory Buttons
//        user.setOnClickListener(v -> {
//
//            //Condition if the Inventory is already opened
//            if(!user_inventory){
//                user_inventory = true;
//
//                //Moving the Inventories
//                left_inventory.animate()
//                        .translationY(0).start();
//                right_inventory.animate()
//                        .translationY(0).start();
//
//                //Changing the Drawable Icon
//                Drawable drawable = getResources().getDrawable(R.drawable.icon_arrow_down);
//                user.setCompoundDrawablesWithIntrinsicBounds(drawable, null, drawable, null);
//            } else{
//                user_inventory = false;
//
//                //Moving the Inventories
//                left_inventory.animate()
//                        .translationY(300).start();
//                right_inventory.animate()
//                        .translationY(300).start();
//
//                //Changing the Drawable Icon
//                Drawable drawable = getResources().getDrawable(R.drawable.icon_arrow_up);
//                user.setCompoundDrawablesWithIntrinsicBounds(drawable, null, drawable, null);
//            }
//        });
//
//        dealer.setOnClickListener(v -> {
//
//            //Condition if the Inventory is already opened
//            if(!dealer_inventory){
//                dealer_inventory = true;
//
//                //Moving the Inventory
//                dealer_items.animate()
//                        .translationY(0).start();
//                dealer.animate()
//                        .translationY(140).start();
//
//                //Changing the Drawable Icon
//                Drawable drawable = getResources().getDrawable(R.drawable.icon_arrow_up);
//                dealer.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
//            } else{
//                dealer_inventory = false;
//
//                //Moving the Inventory
//                dealer_items.animate()
//                        .translationY(-200).start();
//                dealer.animate()
//                        .translationY(0).start();
//
//                //Changing the Drawable Icon
//                Drawable drawable = getResources().getDrawable(R.drawable.icon_arrow_down);
//                dealer.setCompoundDrawablesWithIntrinsicBounds(drawable, null, drawable, null);
//            }
//        });
    }

    //----------------------------------------------------------------------------------------------------------//
    //------------------------------------------------ METHODS -------------------------------------------------//
    //----------------------------------------------------------------------------------------------------------//

    //Method for Initializing the Bullets for the Round
    public void loadGun(){

        //Initializing bullets
        bulletcontainer.removeAllViews();
        switch(level){
            case 1:
                switch(round){
                    case 1:
                        liveround += 1;
                        blankround += 2;
                        break;
                    case 2:
                        liveround += 3;
                        blankround += 2;
                        break;
                }
                break;
            case 2:
                switch(round){
                    case 1:
                        liveround += 1;
                        blankround += 1;
                        break;
                    case 2:
                        liveround += 2;
                        blankround += 2;
                        break;
                    case 3:
                        liveround += 3;
                        blankround += 2;
                        break;
                    case 4:
                        liveround += 3;
                        blankround += 3;
                        break;
                }
                break;
            case 3:
                switch(round){
                    case 1:
                        liveround += 1;
                        blankround += 2;
                        break;
                    case 2:
                        liveround += 4;
                        blankround += 4;
                        break;
                    case 3:
                        liveround += 3;
                        blankround += 2;
                        break;
                    case 4:
                        liveround += 4;
                        blankround += 2;
                        break;
                }
                break;
        }

        //Setting up SnackBar for captions
        captionsnackbar = Snackbar.make(shotgun, "", Snackbar.LENGTH_INDEFINITE);
        customSnackView = getLayoutInflater().inflate(R.layout.caption, null);
        captionsnackbar.getView().setBackgroundColor(Color.TRANSPARENT);

        //Setting SnackBar layout
        @SuppressLint("RestrictedApi") Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) captionsnackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        snackbarLayout.addView(customSnackView, 0);

        //Animating captions
        Button button = customSnackView.findViewById(R.id.descbutton);
        typewriter text = new typewriter(button, getApplicationContext(), volume);
        text.animateText(liveround + " " + captions[1] + " " + blankround + " " + captions[2]);
        button.setOnClickListener(v -> {
            captionsnackbar.dismiss();
            text.stop();

            handler.postDelayed(() -> {

                //Moving the Container
                bulletcontainer.animate()
                        .translationX(200).start();
            }, 2000);

            //Calling the load animation method
            dealer_hover("load");
        });
        captionsnackbar.show();

        //Putting Bullets in the Magazine
        while(liveround != 0){

            //Initializing Imageview
            ImageView imageView = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
            imageView.setLayoutParams(lp);
            imageView.setImageResource(R.drawable.live);

            //Adding the bullet to the container
            bulletcontainer.addView(imageView);
            magazine.add("live");
            liveround--;
        }
        while(blankround != 0){

            //Initializing Imageview
            ImageView imageView = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
            imageView.setLayoutParams(lp);
            imageView.setImageResource(R.drawable.blank);

            //Adding the bullet to the container
            bulletcontainer.addView(imageView);
            magazine.add("blank");
            blankround--;
        }

        //Moving the Container
        bulletcontainer.animate()
                .translationX(0).start();

        Collections.shuffle(magazine);
    }

    //Method for animation every start of Round
    public void startAnimation(){

        //Setting the Animation
        shotgun.setImageResource(R.drawable.a_start);

        animation = (AnimationDrawable)shotgun.getDrawable();
        animation.setOneShot(true);
        animation.start();

        //Waiting for animation to be done
        handler.postDelayed(() -> {

            //Putting Listeners
            shotgun.setClickable(true);
            shotgun.setOnClickListener(hoverListener);
            aim_user.setOnClickListener(aim_userListener);
            aim_dealer.setOnClickListener(aim_dealerListener);
        }, animation.getNumberOfFrames() * 50L);
    }

    //Listener for Hovering the Shotgun Decision
    OnClickListener hoverListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            //Initializing Random
            Random rn = new Random();
            bullet = rn.nextInt(2);

            //closeInv();

            //Setting the ImageView
            shotgun.setClickable(false);
            shotgun.setImageResource(R.drawable.a_hover);

            //Setting the Animation
            animation = (AnimationDrawable)shotgun.getDrawable();
            animation.setOneShot(true);
            animation.start();

            //Waiting for animation to be done
            handler.postDelayed(() -> {

                //Showing Tips
                if(level == 1){
                    showDesc(0);
                }

                //Setting ImageView for Idling
                shotgun.setImageResource(R.drawable.a_idle);
                animation = (AnimationDrawable)shotgun.getDrawable();
                animation.start();
                shotgun.setOnClickListener(null);

                //Showing the Buttons for Decision
                aim_user.setVisibility(View.VISIBLE);
                aim_dealer.setVisibility(View.VISIBLE);
            }, animation.getNumberOfFrames() * 50L);

            //Playing SFX
            soundPool.play(holster, volume, volume, 0, 0, 1);
        }
    };

//    public void closeInv(){
//
//        //Condition to close and remove the inventory buttons/layout
//        if(user_inventory && dealer_inventory){
//
//            //Close and remove both buttons
//            user.performClick();
//            dealer.performClick();
//            user.animate()
//                    .translationY(90).start();
//            dealer.animate()
//                    .translationY(-90).start();
//        } else if(dealer_inventory){
//
//            //Close the opened inventory and remove both buttons
//            dealer.performClick();
//            user.animate()
//                    .translationY(90).start();
//            dealer.animate()
//                    .translationY(-90).start();
//        } else if(user_inventory){
//
//            //Close the opened inventory and remove both buttons
//            user.performClick();
//            user.animate()
//                    .translationY(90).start();
//            dealer.animate()
//                    .translationY(-90).start();
//        } else{
//
//            //Remove both buttons
//            user.animate()
//                    .translationY(90).start();
//            dealer.animate()
//                    .translationY(-90).start();
//        }
//    }

    //Listener for Aiming to the User
    OnClickListener aim_userListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            //Removing the Buttons
            aim_user.setVisibility(View.GONE);
            aim_dealer.setVisibility(View.GONE);
            //Setting the ImageView
            shotgun.setClickable(false);
            shotgun.setImageResource(R.drawable.a_aim_self);

            //Setting the Animation
            animation = (AnimationDrawable)shotgun.getDrawable();
            animation.setOneShot(true);
            animation.start();

            //Waiting for animation to be done
            handler.postDelayed(() ->
                    handler.postDelayed(() -> aim_self_fire(), 800)
            , animation.getNumberOfFrames() * 50L);

            //Playing SFX and calling the putting value on user_who string
            soundPool.play(click, volume, volume, 0, 0, 1);
            user_who = "user";
        }
    };

    //Listener for Aiming at Dealer
    OnClickListener aim_dealerListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            //Removing the Buttons
            aim_user.setVisibility(View.GONE);
            aim_dealer.setVisibility(View.GONE);

            //Setting the ImageView
            shotgun.setClickable(false);
            shotgun.setImageResource(R.drawable.a_aim_dealer);

            //Setting the Animation
            AnimationDrawable animation = (AnimationDrawable) shotgun.getDrawable();
            animation.setOneShot(true);
            animation.start();

            //Waiting for animation to be done
            handler.postDelayed(() ->
                    handler.postDelayed(() -> aim_dealer_fire(), 800)
            , animation.getNumberOfFrames() * 50L);

            //Playing SFX and calling the Checking of Animation Method
            soundPool.play(click, volume, volume, 0, 0, 1);
            user_who = "dealer";
        }
    };

    //Method for aiming at self
    private void aim_self_fire(){

        //Condition if the bullet is live or blank
        if(magazine.get(0).equals("live")){

            //Play the shooting SFX
            soundPool.play(live, volume, volume, 0, 0, 1);
            flash.setVisibility(View.VISIBLE);
            flash.setAlpha(1);
            shotgun.setImageDrawable(null);

            //User looses HP
            handler.postDelayed(() -> {

                flash.animate().alpha(0).setDuration(2000);
                shotgun.animate().alpha(1).setDuration(2000);
                shotgun.setBackgroundColor(Color.TRANSPARENT);
                shotgun.setImageResource(R.drawable.b_dealer_hover_01);
                loseHP("user", "user");
            }, 100);
        } else{

            //Play the blank SFX
            soundPool.play(blank, volume, volume, 0, 0, 1);
            handler.postDelayed(this::self_reload, 1000);
        }
    }

    //Method for aiming at dealer
    private void aim_dealer_fire(){

        //Condition if the bullet is live or blank
        if(magazine.get(0).equals("live")){

            //Play the shooting SFX
            soundPool.play(live, volume, volume, 0, 0, 1);
            flash.setVisibility(View.VISIBLE);
            flash.setAlpha(1);
            shotgun.setImageDrawable(null);
            shotgun.animate().alpha(0).setDuration(100);

            //Shake animation
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            shotgun.startAnimation(shake);

            //Handler for Damaged Dealer
            handler.postDelayed(() -> {

                flash.animate().alpha(0).setDuration(2000);
                shotgun.animate().alpha(1).setDuration(2000);
                shotgun.setBackgroundColor(Color.TRANSPARENT);
                shotgun.setImageResource(R.drawable.user_shot_dealer);
                handler.postDelayed(this::self_reload, 2000);
            }, 200);
        } else{

            //Play the blank SFX
            soundPool.play(blank, volume, volume, 0, 0, 1);
            handler.postDelayed(this::self_reload, 1000);
        }
    }

    //Method for reloading after firing
    public void self_reload(){
        
        flash.setVisibility(View.GONE);

        //Closing inventory
        //closeInv();

        //Condition if where aimed at
        if(user_who.equals("user")) {
            shotgun.setImageResource(R.drawable.a_reload_self);
        } else {
            if(magazine.get(0).equals("live")) {
                shotgun.setImageResource(R.drawable.a_reload_dealer_kill);
            } else {
                shotgun.setImageResource(R.drawable.a_reload_dealer);
            }
        }

        //Setting the ImageView
        animation = (AnimationDrawable) shotgun.getDrawable();
        animation.setOneShot(true);
        animation.start();

        //Waiting for animation to be done
        handler.postDelayed(() -> {

            //Playing SFX
            soundPool.play(holster, volume, volume, 0, 0, 1);

            if(magazine.get(0).equals("live")){

                //Dealer loses HP
                loseHP("dealer", "user");
            } else {

                //Condition to know who's turn next
                if(user_who.equals("dealer")){

                    //Setting the Animation
                    if(magazine.get(0).equals("live")){
                        shotgun.setImageResource(R.drawable.a_face_dealer_dead);
                    } else{
                        shotgun.setImageResource(R.drawable.a_face_dealer);
                    }

                    animation = (AnimationDrawable)shotgun.getDrawable();
                    animation.setOneShot(true);
                    animation.start();

                    //Waiting for animation to be done
                    handler.postDelayed(() -> {

                        //Dealers Turn
                        magazine.remove(0);
                        if(magazine.size() == 0){
                            round++;
                            loadGun();
                        } else {
                            handler.postDelayed(() -> {

                                //Still dealer turn
                                dealer_hover("turn");
                                dealer_who = "";
                            }, 1000);
                        }
                    }, animation.getNumberOfFrames() * 50L);
                } else {

                    //Check if theres still bullet
                    magazine.remove(0);
                    if(magazine.size() == 0){

                        //Setting up animation
                        shotgun.setImageResource(R.drawable.a_face_dealer);
                        animation = (AnimationDrawable)shotgun.getDrawable();
                        animation.setOneShot(true);
                        animation.start();

                        //Waiting for animation to be done
                        handler.postDelayed(() -> {

                            //Next Round
                            round++;
                            loadGun();
                        }, animation.getNumberOfFrames() * 50L);
                    } else {

                        //Still user turn
                        shotgun.setOnClickListener(hoverListener);
                        aim_user.setOnClickListener(aim_userListener);
                        aim_dealer.setOnClickListener(aim_dealerListener);
                        user_who = "";
                    }
                }
            }
        }, animation.getNumberOfFrames() * 50L);

        //Playing SFX
        if(user_who.equals("user")) {
            handler.postDelayed(() ->
                    soundPool.play(reload, volume, volume, 0, 0, 1), 1000);
        } else {
            handler.postDelayed(() ->
                    soundPool.play(reload, volume, volume, 0, 0, 1), 600);
        }
    }

    //----------------------------------------------------------------------------------------------------------//
    //------------------------------------------------ AI STUFF ------------------------------------------------//
    //----------------------------------------------------------------------------------------------------------//

    //Method for Dealer's hover at the gun
    public void dealer_hover(String choice){

        //Setting the Animation
        shotgun.setImageResource(R.drawable.a_dealer_hover);
        animation = (AnimationDrawable)shotgun.getDrawable();
        animation.setOneShot(true);
        animation.start();

        //Waiting for animation to be done
        handler.postDelayed(() -> {

            if(choice.equals("load")){

                //Setting up SnackBar for captions
                captionsnackbar = Snackbar.make(shotgun, "", Snackbar.LENGTH_INDEFINITE);
                customSnackView = getLayoutInflater().inflate(R.layout.caption, null);
                captionsnackbar.getView().setBackgroundColor(Color.TRANSPARENT);

                //Setting SnackBar layout
                @SuppressLint("RestrictedApi") Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) captionsnackbar.getView();
                snackbarLayout.setPadding(0, 0, 0, 0);
                snackbarLayout.addView(customSnackView, 0);

                //Animating captions
                Button button = customSnackView.findViewById(R.id.descbutton);
                typewriter text = new typewriter(button, getApplicationContext(), volume);
                text.animateText(captions[3]);
                button.setOnClickListener(v -> {
                    captionsnackbar.dismiss();
                    text.stop();

                    //Calling the Loading animation method
                    load(magazine.size());
                });
                captionsnackbar.show();
            } else {
                Random rn = new Random();
                int x = rn.nextInt(100);
                if (x < 50) {
                    dealer_aim_self();
                } else {
                    dealer_aim_user();
                }
            }
        }, animation.getNumberOfFrames() * 50L);
    }

    //Method for Dealer's returning of gun
    public void dealer_return(){

        //Setting the Animation
        shotgun.setImageResource(R.drawable.a_dealer_return);
        animation = (AnimationDrawable)shotgun.getDrawable();
        animation.setOneShot(true);
        animation.start();

        handler.postDelayed(() -> {

            //Starting animation
            startAnimation();

//            if(level != 1){
//
//                //Putting the inventory buttons back
//                user.animate()
//                        .translationY(0).start();
//                dealer.animate()
//                        .translationY(0).start();
//            }
        }, ((animation.getNumberOfFrames() - 4) * 50L) + (4 * 75L));
    }

    //Method for Dealer's aiming at self
    public void dealer_aim_self(){
        dealer_who = "dealer";

        //Setting the ImageView
        shotgun.setClickable(false);
        shotgun.setImageResource(R.drawable.a_dealer_aim_self);

        //Setting the Animation
        AnimationDrawable animation = (AnimationDrawable) shotgun.getDrawable();
        animation.setOneShot(true);
        animation.start();

        //Waiting for animation to be done
        handler.postDelayed(() -> {

            //Condition if the bullet is live or blank
            if(magazine.get(0).equals("live")){

                //Play the shooting SFX
                soundPool.play(live, volume, volume, 0, 0, 1);
                flash.setVisibility(View.VISIBLE);
                flash.setAlpha(1);
                shotgun.setImageDrawable(null);

                //Dealer looses HP
                handler.postDelayed(() -> {

                    flash.animate().alpha(0).setDuration(2000);
                    shotgun.animate().alpha(1).setDuration(2000);
                    shotgun.setBackgroundColor(Color.TRANSPARENT);
                    shotgun.setImageResource(R.drawable.b_face_dealer_dead_08);
                    loseHP("dealer", "dealer");
                }, 100);
            } else{

                //Play the blank SFX
                soundPool.play(blank, volume, volume, 0, 0, 1);
                handler.postDelayed(this::dealer_aim_self_reload, 1000);
            }
        }, animation.getNumberOfFrames() * 50L);
    }

    public  void dealer_aim_user(){
        dealer_who = "user";

        //Setting the ImageView
        shotgun.setClickable(false);
        shotgun.setImageResource(R.drawable.a_dealer_aim_user);

        //Setting the Animation
        AnimationDrawable animation = (AnimationDrawable) shotgun.getDrawable();
        animation.setOneShot(true);
        animation.start();

        //Waiting for animation to be done
        handler.postDelayed(() -> {

            //Condition if the bullet is live or blank
            if(magazine.get(0).equals("live")){

                //Play the shooting SFX
                soundPool.play(live, volume, volume, 0, 0, 1);
                flash.setVisibility(View.VISIBLE);
                flash.setAlpha(1);
                shotgun.setImageDrawable(null);
                shotgun.animate().alpha(0).setDuration(100);

                //Shake animation
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                shotgun.startAnimation(shake);

                //Handler for Damaged User
                handler.postDelayed(() -> {

                    flash.animate().alpha(0).setDuration(2000);
                    shotgun.animate().alpha(1).setDuration(2000);
                    shotgun.setBackgroundColor(Color.TRANSPARENT);
                    shotgun.setImageResource(R.drawable.b_face_dealer_dead_08);
                    loseHP("user", "dealer");
                }, 200);
            } else{

                //Play the blank SFX
                soundPool.play(blank, volume, volume, 0, 0, 1);
                handler.postDelayed(this::dealer_aim_self_reload, 1000);
            }
        },animation.getNumberOfFrames() * 50L);

        //Playing SFX and calling the Checking of Animation Method
        soundPool.play(click, volume, volume, 0, 0, 1);
    }

    public void dealer_aim_self_reload(){

        //Condition if where aimed at
        if(dealer_who.equals("dealer")){
            shotgun.setImageResource(R.drawable.a_dealer_reload_self);
        } else{
            shotgun.setImageResource(R.drawable.a_dealer_reload_user);
        }

        //Setting the ImageView
        animation = (AnimationDrawable) shotgun.getDrawable();
        animation.setOneShot(true);
        animation.start();

        //Waiting for animation to be done
        handler.postDelayed(() -> {

            //Playing SFX
            soundPool.play(holster, volume, volume, 0, 0, 1);

            if(!magazine.get(0).equals("live")){

                //Condition to know who's turn next
                if(dealer_who.equals("user")){

                    //Check if theres still bullet
                    magazine.remove(0);
                    if(magazine.size() == 0){
                        round++;
                        loadGun();
                    } else {

                        //Putting Listeners
                        shotgun.setClickable(true);
                        shotgun.setOnClickListener(hoverListener);
                        aim_user.setOnClickListener(aim_userListener);
                        aim_dealer.setOnClickListener(aim_dealerListener);
                        dealer_who = "";
                    }

                } else {

                    //Setting the ImageView
                    shotgun.setImageResource(R.drawable.a_face_dealer);
                    animation = (AnimationDrawable) shotgun.getDrawable();
                    animation.setOneShot(true);
                    animation.start();

                    //Waiting for animation to be done
                    handler.postDelayed(() -> {

                        //Check if theres still bullet
                        magazine.remove(0);
                        if(magazine.size() == 0){
                            round++;
                            loadGun();
                        } else {

                            //Still dealer turn
                            dealer_hover("turn");
                            dealer_who = "";
                        }
                    }, animation.getNumberOfFrames() * 50L);
                }
            }
        }, animation.getNumberOfFrames() * 50L);

        //Playing SFX
        if(user_who.equals("user")) {
            handler.postDelayed(() ->
                    soundPool.play(reload, volume, volume, 0, 0, 1), 1000);
        } else {
            handler.postDelayed(() ->
                    soundPool.play(reload, volume, volume, 0, 0, 1), 600);
        }
    }

    //----------------------------------------------------------------------------------------------------------//
    //------------------------------------------------ GENERAL -------------------------------------------------//
    //----------------------------------------------------------------------------------------------------------//

    public void loseHP(String who_loses, String who_did){

        flash.setVisibility(View.GONE);

        //Closing inventory
        //closeInv();

        //Remove current bullet
        magazine.remove(0);
        if(magazine.size() == 0){
            who_did = "nextround";
        }

        //Passing action to fragment
        healthtab fragment = new healthtab();
        Bundle arguments = new Bundle();
        arguments.putString("who_loses", who_loses);
        arguments.putString("who_did", who_did);
        fragment.setArguments(arguments);

        //Showing the Health Monitor Tab
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in)
                .replace(R.id.fragment_container, fragment)
                .setReorderingAllowed(true)
                .commit();
    }

    //Method for Dealer loading the gun
    public void load(int loop){

        //Checking if still looping
        if(loop != 0){

            //Setting the Animation
            shotgun.setImageResource(R.drawable.a_load);
            animation = (AnimationDrawable)shotgun.getDrawable();
            animation.setOneShot(true);

            //Waiting for animation to be done
            handler.postDelayed(() -> {
                load(loop - 1);
            }, animation.getNumberOfFrames() * 50L);
            animation.start();

            //Playing SFX
            soundPool.play(load, volume, volume, 0, 0, 1);
        } else{
            dealer_return();
        }
    }

    //Method for Showing descriptions
    public void showDesc(int index){

        //Setting up SnackBar for descriptions
        descsnackbar = Snackbar.make(shotgun, "", Snackbar.LENGTH_LONG);
        customSnackView = getLayoutInflater().inflate(R.layout.desc, null);
        descsnackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        descsnackbar.setAnchorView(R.id.main_screen);

        //Setting SnackBar layout
        @SuppressLint("RestrictedApi") Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) descsnackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        snackbarLayout.addView(customSnackView, 0);
        Button button = customSnackView.findViewById(R.id.descbutton);
        button.setText(desc[index]);
        descsnackbar.show();
    }

    //Method to return to title page
    public void toTitle(){

        //Intent to Title Page
        startActivity(new Intent(MainActivity.this, title_page.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (captionsnackbar != null && captionsnackbar.isShown()) {

                Rect sRect = new Rect();
                captionsnackbar.getView().getHitRect(sRect);

                //This way the snackbar will only be dismissed if
                //the user clicks outside it.
                if (!sRect.contains((int)ev.getX(), (int)ev.getY())) {
                    Button button = customSnackView.findViewById(R.id.descbutton);
                    button.performClick();
                    captionsnackbar.dismiss();
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mediaPlayer.pause();
        mediaPlayback = mediaPlayer.getCurrentPosition();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mediaPlayer = MediaPlayer.create(this, R.raw.title);
        mediaPlayer.seekTo(mediaPlayback);
        mediaPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mediaPlayer.stop();
        mediaPlayer.release();
        soundPool.release();
    }
}