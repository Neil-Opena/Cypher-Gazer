package neil_opena.cyphergazer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import neil_opena.cyphergazer.Cyphers.Cypher;

public class Settings extends DialogFragment {

    public static final String SELECTED_CYPHER =
            "neil_opena.cyphergazer.cypher";
    public static final String SELECTED_ID =
            "neil-opena.cyphergazer.id";
    public static final String KEY =
            "neil-opena.cyphergazer.key";

    private Chip mSelectedChip;
    private ChipGroup mChipGroup;
    private TextInputEditText mKeyInput;

    private String mCurrentKey;

    public static Settings newInstance(int cypherId, String cypher, String key){
        Settings dialog = new Settings();

        Bundle args = new Bundle();
        args.putString(KEY, key);
        args.putString(SELECTED_CYPHER, cypher);
        args.putInt(SELECTED_ID, cypherId);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View v = inflater.inflate(R.layout.layout_dialog, null);

        mCurrentKey = getArguments().getString(KEY);
        mKeyInput = v.findViewById(R.id.key);
        mKeyInput.setText(mCurrentKey);
        mKeyInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyBoard(v);
            }
        });

        mChipGroup = v.findViewById(R.id.cypher_group);
        mChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                mSelectedChip = v.findViewById(i);
                try {
                    String classToCheck = getString(R.string.packageName) + mSelectedChip.getText().toString();
                    Class toCheck = Class.forName(classToCheck);
                    Cypher instance = (Cypher) (toCheck.getConstructors()[0].newInstance());
                    if(instance.hasNumericalKey()){
                        mKeyInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                } catch (ClassNotFoundException e) {
                } catch (IllegalAccessException e) {
                } catch (java.lang.InstantiationException e) {
                } catch (InvocationTargetException e) {
                }
            }
        });

        int cypherId = getArguments().getInt(SELECTED_ID);
        if(cypherId != 0){
            mSelectedChip = v.findViewById(cypherId);
            mSelectedChip.setChecked(true);
        }

        builder.setView(v)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //save
                        int chipId = mChipGroup.getCheckedChipId();
                        Chip chipTest = v.findViewById(chipId);
                        String cypher = null;
                        if(chipTest != null){
                            cypher = chipTest.getText().toString();
                        }
                        String key = mKeyInput.getText().toString();

                        sendResult(Activity.RESULT_OK, chipId, cypher, key);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //don't save
                    }
                });

        return builder.create();
    }

    private void hideKeyBoard(View v){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void sendResult(int resultCode, int cypherId, String cypher, String currentKey){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(SELECTED_ID, cypherId);
        intent.putExtra(SELECTED_CYPHER, cypher);
        intent.putExtra(KEY, currentKey);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
