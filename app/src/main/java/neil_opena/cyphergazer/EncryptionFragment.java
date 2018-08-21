package neil_opena.cyphergazer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import neil_opena.cyphergazer.Cyphers.Cypher;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class EncryptionFragment extends Fragment {

    public static final String TAG = "EncryptionFragment";

    private static final int REQUEST_CYPHER = 0;

    private TextInputEditText mPlainTextEdit;
    private TextInputEditText mCryptTextEdit;
    private TextInputLayout mPlainTextLayout;
    private TextInputLayout mCryptTextLayout;
    private TextInputLayout mHiddenLayout;
    private FloatingActionButton mPlayButton;
    private MaterialButton mSettingsButton;
    private LinearLayout mLettersContainer;

    private HashMap<String, Cypher> mCypherMap = new LinkedHashMap<>();

    private Cypher mCypher;
    private String mKey;
    private int mCypherId;

    public static EncryptionFragment newInstance(){
        return new EncryptionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setUpCyphers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_encrypt, container, false);

        mPlainTextLayout = v.findViewById(R.id.plain_text_layout);
        mCryptTextLayout = v.findViewById(R.id.crypt_text_layout);
        mCryptTextLayout.setVisibility(View.GONE);
        mHiddenLayout = v.findViewById(R.id.hidden_crypt_text_layout);
        mHiddenLayout.setVisibility(View.GONE);
        mLettersContainer = v.findViewById(R.id.letters_container);
        setUpLetterContainer();

        mPlainTextEdit = v.findViewById(R.id.plain_text);
        mPlainTextEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyBoard(v);
            }
        });

        mCryptTextEdit = v.findViewById(R.id.crypt_text);
        mCryptTextEdit.setEnabled(false);

        mSettingsButton = v.findViewById(R.id.settings_btn);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocusFromTouch();
                Settings mSettings = Settings.newInstance(mCypherMap, mCypherId, "" + mCypher, mKey);
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

                v.requestFocusFromTouch();

                showHiddenLayout();
                new EncryptTask().execute();
                mPlainTextEdit.setEnabled(false);
                /*
                animate:


                play --> fast forward --> rewind

                when played: cant edit text until rewind button pressed
                 */
            }
        });

        autoFill();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_CYPHER){
            String cypherString = data.getStringExtra(Settings.SELECTED_CYPHER);
            mCypher = mCypherMap.get(cypherString);
            mCypherId = data.getIntExtra(Settings.SELECTED_ID, 0);
            mKey = data.getStringExtra(Settings.KEY);
        }
    }

    private void setUpCyphers(){
        String[] stringArray = getResources().getStringArray(R.array.cyphers);
        for(int i = 0; i < stringArray.length; i++){
            String cypherXML = stringArray[i];
            try{
                Class cypherClass = Class.forName(getString(R.string.packageName) + cypherXML);
                Cypher instance = (Cypher) cypherClass.getConstructors()[0].newInstance();
                mCypherMap.put(cypherXML, instance);
            } catch (ClassNotFoundException e) {
            } catch (IllegalAccessException e) {
            } catch (java.lang.InstantiationException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    private boolean isConfigured(){
        return mCypher != null && !mKey.isEmpty();
    }

    private void hideKeyBoard(View v){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void showHiddenLayout(){
        mHiddenLayout.setVisibility(View.INVISIBLE);
        mCryptTextLayout.setVisibility(View.VISIBLE);
        mCryptTextLayout.setAlpha(0.0f);

        mCryptTextLayout.animate()
                .translationY(mPlainTextLayout.getHeight() + mSettingsButton.getHeight() + mLettersContainer.getHeight())
                .alpha(1.0f)
                .setListener(null);
    }

    private void hideHiddenLayout(){
        mHiddenLayout.setVisibility(View.GONE);
        mCryptTextLayout.setAlpha(0.0f);

        mCryptTextLayout.animate()
                .translationY(-mPlainTextEdit.getHeight() - mSettingsButton.getHeight() - mLettersContainer.getHeight())
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mCryptTextLayout.setVisibility(View.GONE);
                    }
                });
    }

    private void setUpLetterContainer(){
        for(int i = 0; i < mLettersContainer.getChildCount(); i++){
            LinearLayout letterContainer = (LinearLayout) mLettersContainer.getChildAt(i);

            TextView plainLetter = (TextView) letterContainer.getChildAt(0);
            TextView cryptLetter = (TextView) letterContainer.getChildAt(1);
            char letter = (char) ('A' + i);
            plainLetter.setText("" + letter);
            cryptLetter.setText("" + letter);
        }
    }

    //FIXME DELETE
    private void autoFill(){
        mPlainTextEdit.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        mCypher = mCypherMap.get("Caesar");
        mKey = "1";
    }

    private void showErrorDialog(){
        AlertDialog errorDialog = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.error_not_valid_message)
                .setTitle(R.string.error_not_valid_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing
                    }
                })
                .create();
        errorDialog.show();
    }

    /*
    For each letter in the text view:
        color each text
     */

    /**
     * Class is responsible for encrypting and visually displaying
     * the encryption process to the user
     */
    private class EncryptTask extends AsyncTask<Void, Integer, Void>{

        private String plainText;
        private String cryptText;

        @Override
        protected Void doInBackground(Void... params){
            plainText = mPlainTextEdit.getText().toString();
            cryptText = mCypher.encrypt(plainText, mKey);

            for(int i = 0; i < cryptText.length(); i++){
                try {
                    Thread.sleep(1000);
                    publishProgress(i);
                } catch (InterruptedException e) {

                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int curr = values[0];
            int colorAccent = getResources().getColor(R.color.accent);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(colorAccent);

            mPlainTextEdit.getText().setSpan(colorSpan, curr, curr + 1, 0);

            //mPlainTextEdit.getText().replace(values[0], values[0] + 1, "" + cryptText.charAt(values[0]));

            mCryptTextEdit.append("" + cryptText.charAt(values[0]));
            mCryptTextEdit.getText().setSpan(colorSpan, curr, curr + 1, 0);


        }
    }
}
