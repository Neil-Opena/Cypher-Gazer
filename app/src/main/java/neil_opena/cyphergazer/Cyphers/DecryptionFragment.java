package neil_opena.cyphergazer.Cyphers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import neil_opena.cyphergazer.R;

public class DecryptionFragment extends Fragment{

    public static DecryptionFragment newInstance(){
        return new DecryptionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_decrypt, container, false);

        return v;
    }
}
