package neil_opena.cyphergazer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;

import androidx.fragment.app.Fragment;

public class CypherGazerFragment extends Fragment{

    private MaterialCardView mPlainTextCard;
    private MaterialCardView mCryptTextCard;

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

        mPlainTextCard = (MaterialCardView) v.findViewById(R.id.plain_text_card);
        mPlainTextCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCryptTextCard.setBackgroundColor(getResources().getColor(R.color.clouds));
            }
        });

        mCryptTextCard = (MaterialCardView) v.findViewById(R.id.crypt_text_card);

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
