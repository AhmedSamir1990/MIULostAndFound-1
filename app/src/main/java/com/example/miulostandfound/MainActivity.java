package com.example.miulostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {
        Toolbar toolbar;
        Login log = new Login();
        TabLayout tabLayout;
        ViewPager viewPager;
        PageAdapter pageAdapter;
        TabItem tabChats;
        TabItem tabStatus;
        TabItem tabCalls;
        String personEmail;
        Button fab;
        int backButtonCount=0;
    static MainActivity instance;
        PostAdapter p;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            instance = this;
            personEmail=getIntent().getStringExtra("personEmail");
            tabLayout = findViewById(R.id.tablayout);
            tabChats = findViewById(R.id.tabChats);
            tabStatus = findViewById(R.id.tabStatus);
            tabCalls = findViewById(R.id.tabCalls);
            viewPager = findViewById(R.id.viewPager);
            pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(pageAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                    if (tab.getPosition() == 1) {

                        tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                                R.color.colorRed));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,
                                    R.color.colorRed));
                        }
                    } else if (tab.getPosition() == 2) {

                        tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                                R.color.colorRed));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,
                                    R.color.colorRed));
                        }
                    } else {

                        tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                                R.color.colorRed));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,
                                    R.color.colorRed));
                        }
                    }
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    public void onclickfab()
    {
        goAddImage(personEmail);
    }
    public void goAddImage(String personEmail)
    {
        Intent intent = new Intent(this,Main2Activity.class);
        intent.putExtra("personEmail",personEmail);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                signOut();
                return true;
            case R.id.item2:
                Toast.makeText(this,"About",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item3:
                Toast.makeText(this,"Search",Toast.LENGTH_LONG).show();
                gosearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void signOut() {

        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
//        log.signOut();


    }
    void gosearch() {

        Intent intent = new Intent(this, searchActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
    public void openDialog(int position) {
        DialogBox Dialog = new DialogBox();
        Dialog.show(getSupportFragmentManager(),"Dialog");
        Dialog.setPosition(position);
    }
}



