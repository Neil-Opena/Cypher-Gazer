package neil_opena.cyphergazer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import neil_opena.cyphergazer.Cyphers.Cypher;

public class Settings extends DialogFragment {

    private ChipGroup mCypherGroup;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(v)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        mCypherGroup = (ChipGroup) v.findViewById(R.id.cypher_group);
        setUpChips();

        return builder.create();
    }

    private void setUpChips(){
        List<Cypher> list= ((CypherGazerActivity) getActivity()).getCypherList();
        for(Cypher cypher : list){
            Chip chip = new Chip(getActivity());
            chip.setText(cypher.toString());
            mCypherGroup.addView(chip);
        }
    }

}
