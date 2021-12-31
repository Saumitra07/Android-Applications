package com.example.inclass08;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

//Group25_HW05.zip
//Nikhil Naresh Mandge and Saumitra Uday Apte
//HW05 - 08 NOV 2021
//Note: Application/Package Name is InClass08 as new tasks for HW05 have been added in previously completed Inclass08.


public class MainActivity extends AppCompatActivity implements CreateNewForum.newForumListener, ForumsFragment.forumFragmentListener,LoginFragment.LoginListener,RegisterFragment.RegisterListener{
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();

        if(auth.getCurrentUser()==null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container1,new LoginFragment()).commit();
        }

        else
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container1,new ForumsFragment()).commit();
        }
    }

    @Override
    public void newForumAdded() {
        getSupportFragmentManager().popBackStack();

    }

    @Override
    public void goToForums() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToCreateNewForum(User us) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container1,CreateNewForum.newInstance(us)).addToBackStack(null).commit();
    }

    @Override
    public void goToLogin() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container1,new LoginFragment()).commit();
    }

    @Override
    public void goToMainToForumComments(Forums fr,User us) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container1,new ForumCommentsFragment(fr,us)).addToBackStack(null).commit();
    }

    @Override
    public void goToForums(User us) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container1,ForumsFragment.newInstance(us)).commit();
    }

    @Override
    public void goToRegister() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container1,new RegisterFragment()).commit();
    }

    @Override
    public void goToNewForums(User us) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container1,ForumsFragment.newInstance(us)).commit();
    }

    @Override
    public void goBackToLogin() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container1,new LoginFragment()).commit();
    }
}