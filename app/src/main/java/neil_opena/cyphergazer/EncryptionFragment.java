package neil_opena.cyphergazer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import neil_opena.cyphergazer.Cyphers.Cypher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.InvocationTargetException;

public class EncryptionFragment extends Fragment {

    public static final String TAG = "EncryptionFragment";

    private static final int REQUEST_CYPHER = 0;

    private TextInputEditText mPlainTextEdit;
    private FloatingActionButton mPlayButton;
    private MaterialButton mSettingsButton;

    private Cypher mCypher;
    private String mKey;
    private int mCypherId;

    public static EncryptionFragment newInstance(){
        return new EncryptionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_encrypt, container, false);

        mPlainTextEdit = v.findViewById(R.id.plain_text);
        mPlainTextEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyBoard(v);
            }
        });

        mSettingsButton = v.findViewById(R.id.settings_btn);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings mSettings = Settings.newInstance(mCypherId, "" + mCypher, mKey);
                mSettings.setTargetFragment(EncryptionFragment.this, REQUEST_CYPHER);
                mSettings.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });

        mPlayButton = v.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cypher has not been configured or empty text
                if(!isConfigured() || mPlainTextEdit.getText().toString().isEmpty()){
                    showErrorDialog();
                    return;
                }

                mPlainTextEdit.setText(mCypher.encrypt(mPlainTextEdit.getText().toString(), mKey));
                //FIXMe delete lol
                //card increase in length
                //showing a textview where decrypted text is shown (phase 1)
                //shows how text is encrypted (phase 2)
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_CYPHER){
            String cypherString = data.getStringExtra(Settings.SELECTED_CYPHER);
            setCypher(cypherString);

            mCypherId = data.getIntExtra(Settings.SELECTED_ID, 0);
            mKey = data.getStringExtra(Settings.KEY);
        }
    }

    private void setCypher(String cypherString){
        try {
            Class cypherClass = Class.forName(getString(R.string.packageName) + cypherString);
            mCypher = (Cypher) cypherClass.getConstructors()[0].newInstance();
        } catch (ClassNotFoundException e) {

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private boolean isConfigured(){
        return mCypher != null && !mKey.isEmpty();
    }

    private void hideKeyBoard(View v){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void showErrorDialog(){
        AlertDialog errorDialog = new AlertDialog.Builder(getActivity())
                .setMessage("Please add a valid text or configure the cypher before attempting to encrypt.")
                .setTitle("Not configured")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing
                    }
                })
                .create();
        errorDialog.show();
    }
}
