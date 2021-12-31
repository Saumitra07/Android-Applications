package com.example.inclass08;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    User us;
    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    EditText email;
    EditText password;
    public boolean Validation(String email,String pass)
    {
        if(email.length()==0)
        {
            Toast.makeText(getActivity(),"Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(pass.length()==0)
        {
            Toast.makeText(getActivity(),"Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }



        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getActivity(), "Enter valid Email address !", Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Login");
        View view= inflater.inflate(R.layout.fragment_login, container, false);

        email=view.findViewById(R.id.enter_email);
        password=view.findViewById(R.id.enter_password);
        view.findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em=email.getText().toString();
                String pw=password.getText().toString();

                if(Validation(em,pw))
                {
                    mAuth=FirebaseAuth.getInstance();
                    db=FirebaseFirestore.getInstance();
                    mAuth.signInWithEmailAndPassword(em,pw).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if(task.isSuccessful())
                            {
                                Log.d("demo","user logged in");
                                Log.d("demo",mAuth.getCurrentUser().getUid());

                                db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        for(QueryDocumentSnapshot val:task.getResult())
                                        {

                                            //Log.d("demo",mAuth.getCurrentUser().getEmail());
                                            //Log.d("demo",val.getData().get("email").toString());
                                            if(mAuth.getCurrentUser().getEmail().toString().equals(val.getData().get("email").toString()))
                                            {
                                                us=new User(val.getData().get("email").toString(),val.getData().get("fullname").toString());
                                                Log.d("demo",us.getEmail()+" "+us.getFullname());
                                                //getFragmentManager().beginTransaction().replace(R.id.container1,ForumsFragment.newInstance(us)).commit();
                                                listener.goToForums(us);
                                            }
                                        }
                                    }
                                });

                                //getFragmentManager().beginTransaction().replace(R.id.container1,new ForumsFragment(us)).commit();
                            }
                            else
                            {

                             //   Toast.makeText(getActivity(),"Exception "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                               // Log.d("demo","user login failed");
                                Log.d("demo",task.getException().toString());

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Error!").setMessage(task.getException().toString()).create().show();



                            }

                        }
                    });


                }
                else
                {

                    //Toast.makeText(getActivity(),"Invalid Credentials", Toast.LENGTH_SHORT).show();

                }




            }
        });
        view.findViewById(R.id.button_create_acc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getFragmentManager().beginTransaction().replace(R.id.container1,new RegisterFragment()).commit();

                listener.goToRegister();

            }
        });


        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof LoginListener)
        {
            listener = (LoginListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + "Must impl listner");
        }
    }

    LoginListener listener;
    interface LoginListener{

        void goToForums(User us);
        void goToRegister();


    }
}