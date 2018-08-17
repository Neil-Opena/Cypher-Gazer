package neil_opena.cyphergazer;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class Settings extends DialogFragment {

    public static final String SELECTED_CYPHER =
            "neil_opena.cyphergazer.cypher";
    public static final String SELECTED_ID =
            "neil-opena.cyphergazer.id";

    private Chip mSelectedChip;
    private ChipGroup mChipGroup;

    public static Settings newInstance(int cypherId, String cypher){
        Settings dialog = new Settings();

        Bundle args = new Bundle();
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

        int cypherId = getArguments().getInt(SELECTED_ID);
        if(cypherId != 0){
            mSelectedChip = v.findViewById(cypherId);
            mSelectedChip.setChecked(true);
        }

        mChipGroup = v.findViewById(R.id.cypher_group);
        builder.setView(v)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //save

                        int chipId = mChipGroup.getCheckedChipId();
                        String cypher = ((Chip) v.findViewById(chipId)).getText().toString();
                        sendResult(Activity.RESULT_OK, chipId, cypher);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //don't save
                    }
                });

        return builder.create();
    }

    private void sendResult(int resultCode, int cypherId, String cypher){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(SELECTED_ID, cypherId);
        intent.putExtra(SELECTED_CYPHER, cypher);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
