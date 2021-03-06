package pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists;

/**
 * Created by MSC on 06/04/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.storage.FileSystemManager;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.LoginActivity;

public class SettingsFragment extends Fragment {


    private Button mSaveButton;
    private Button mDeleteUserButton;
    private EditText mEmailET;
    private EditText mNickNameET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_layout, container, false);
        mDeleteUserButton = (Button) rootView.findViewById(R.id.settings_deleteUserBtn);
        mSaveButton = (Button) rootView.findViewById(R.id.saveSettingsBtn);
        mEmailET = (EditText) rootView.findViewById(R.id.settingsEmailET);
        mNickNameET = (EditText) rootView.findViewById(R.id.settingsNicknameET);

        AirDesk airDesk = (AirDesk) getActivity().getApplication();
        mEmailET.setText(airDesk.getUser().getEmail());
        mNickNameET.setText(airDesk.getUser().getUserName());
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSaveButton(v);
            }
        });
        mDeleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeleteUserButton(v);
            }
        });
        return rootView;
    }

    public void onClickDeleteUserButton(View v) {
        SharedPreferences preferences = this.getActivity().getSharedPreferences(LoginActivity.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();

        AirDesk airDesk = (AirDesk) getActivity().getApplication();

        FileSystemManager.deleteRecursively(new File(Environment.getExternalStorageDirectory() + "/AirDesk/"));
        airDesk.reset();
        Toast.makeText(getActivity().getApplicationContext(), "User deleted", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this.getActivity(), LoginActivity.class));
        this.getActivity().finish();
    }

    public void onClickSaveButton(View v) {
        //TODO verificar que email é unico
        AirDesk airDesk = (AirDesk) getActivity().getApplication();
        airDesk.getUser().setEmail(mEmailET.getText().toString().trim());
        airDesk.getUser().setUserName(mNickNameET.getText().toString().trim());

        SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(LoginActivity.STATE_EMAIL, airDesk.getUser().getEmail());
        editor.putString(LoginActivity.STATE_NICKNAME, airDesk.getUser().getUserName());
        editor.commit();

        Toast.makeText(getActivity().getApplicationContext(),
                "User has been changed", Toast.LENGTH_SHORT).show();
    }

}
