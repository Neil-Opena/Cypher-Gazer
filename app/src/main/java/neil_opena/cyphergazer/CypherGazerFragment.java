package neil_opena.cyphergazer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CypherGazerFragment extends Fragment{

    public static CypherGazerFragment newInstance(){
        return new CypherGazerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cypher_gazer, container, false);

        return v;

        //when click on text card --> expand and go to front so the user can edit it
        //also show configuration for decryption there
        //when text card closed, a material chipo will appear that will show what cypher the user chose
        //as well as the configuration

        /*
        Have a switch button, that automatically switches plain text from top to bottom
        switches encrypt button to decrypt button
         */
    }
}
