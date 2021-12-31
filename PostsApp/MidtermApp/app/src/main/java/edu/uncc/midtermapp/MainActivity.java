package edu.uncc.midtermapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


//Assignment HW04
// Nikhil Naresh Mandge and Saumitra Uday Apte
//File: Group25_HW04

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, SignUpFragment.SignUpFragmentListener,PostsListFragment.PostListListener,CreatePostFragment.newPostListener {
    UserToken mUserToken;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String default_key="testData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        String string_data = sharedPreferences.getString("user_token",default_key);
        String token  = sharedPreferences.getString("user_token",default_key);
        String user_id  = sharedPreferences.getString("user_id",default_key);
        String user_fullname  = sharedPreferences.getString("user_fullname",default_key);

        if(string_data != default_key)
        {



            UserToken userTokenData =  new UserToken(token,user_fullname,user_id);
            mUserToken=userTokenData;

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rootView, PostsListFragment.newInstance(userTokenData),"POSTS")
                    .commit();
        }
        else
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment())
                    .commit();

        }


    }

    @Override
    public void createAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SignUpFragment())
                .commit();
    }

    @Override
    public void loginSuccessfulGotoPosts(UserToken userToken) {


        editor.putString("user_token",userToken.getToken());
        editor.putString("user_id",userToken.getUserId());
        editor.putString("user_fullname",userToken.getFullname());

        editor.apply();




        mUserToken = userToken;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, PostsListFragment.newInstance(mUserToken),"POSTS")
                .commit();
    }

    @Override
    public void cancelSignUp() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void registerSuccessfulGotoPosts(UserToken userToken) {
        editor.clear();
        editor.apply();

        editor.putString("user_token",userToken.getToken());
        editor.putString("user_id",userToken.getUserId());
        editor.putString("user_fullname",userToken.getFullname());

        editor.apply();

        mUserToken = userToken;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, PostsListFragment.newInstance(mUserToken)).addToBackStack(null)
                .commit();
    }




    @Override
    public void goBackToPosts(UserToken userToken) {

        getSupportFragmentManager().popBackStackImmediate();

    }

    @Override
    public void cancelNewPost() {

        getSupportFragmentManager().popBackStackImmediate();



    }

    @Override
    public void logOut() {

        editor.clear();
        editor.apply();
        getSupportFragmentManager().beginTransaction().replace(R.id.rootView,new LoginFragment()).addToBackStack(null).commit();
    }

    @Override
    public void createNewPost(UserToken userToken) {
        mUserToken=userToken;
        getSupportFragmentManager().beginTransaction().replace(R.id.rootView,new CreatePostFragment(mUserToken)).addToBackStack(null).commit();

    }
}