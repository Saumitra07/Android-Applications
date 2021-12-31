package com.example.inclass08;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    FirebaseAuth mAuth;
    FirebaseFirestore fb;


    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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

    EditText reg_email;
    EditText reg_fullname;
    EditText reg_password;

    public boolean Validation(String email,String pass,String fname)
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
        else if(fname.length()==0)
        {
            Toast.makeText(getActivity(),"Enter full name", Toast.LENGTH_SHORT).show();
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
        getActivity().setTitle("Create New Account");

        View view= inflater.inflate(R.layout.fragment_register, container, false);

        reg_email=view.findViewById(R.id.reg_enterEmail);
        reg_fullname=view.findViewById(R.id.reg_entername);
        reg_password=view.findViewById(R.id.reg_enterpassword);

        String em=reg_email.getText().toString();
        String fname=reg_fullname.getText().toString();
        String pw=reg_password.getText().toString();


        view.findViewById(R.id.reg_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    mAuth=FirebaseAuth.getInstance();
                    String em=reg_email.getText().toString();
                    String fname=reg_fullname.getText().toString();
                    String pw=reg_password.getText().toString();
                if(Validation(em,pw,fname))
                {

                    mAuth.createUserWithEmailAndPassword(em,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                fb=FirebaseFirestore.getInstance();

                                User us=new User(em,fname);

                                Map<String, Object> newuser = new HashMap<>();
                                newuser.put("email", em);
                                newuser.put("fullname", fname);


                                fb.collection("users").add(newuser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                       // getFragmentManager().beginTransaction().replace(R.id.container1,ForumsFragment.newInstance(us)).commit();
                                        rlistener.goToNewForums(us);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("demo", "user failed in db");

                                    }
                                });


                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Error!").setMessage(task.getException().toString()).create().show();
                            }



                        }
                    });

                }
                else
                {

                }






            }
        });

        view.findViewById(R.id.reg_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   getFragmentManager().beginTransaction().replace(R.id.container1,new LoginFragment()).commit();
                rlistener.goBackToLogin();

            }
        });




        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof RegisterListener)
        {
            rlistener = (RegisterListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + "Must impl listner");
        }
    }
    RegisterListener rlistener;

    interface RegisterListener{

    void goToNewForums(User us);

    void goBackToLogin();

    }
}