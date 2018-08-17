package neil_opena.cyphergazer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CypherGazerActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<tabItem> mTabItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cypher_gazer);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.viewPager);

        setUpFragments();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return mTabItemList.get(position).mFragment;
            }

            @Override
            public int getCount() {
                return mTabItemList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTabItemList.get(position).mTitle;
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
        setUpTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_cypher_gazer, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_help:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class tabItem{
        private String mTitle;
        private int mIcon;
        private Fragment mFragment;

        public tabItem(int title, int icon, Fragment fragment){
            mTitle = getString(title);
            mIcon = icon;
            mFragment = fragment;
        }
    }

    private void setUpFragments(){
        Fragment encryptionFragment = EncryptionFragment.newInstance();
        mTabItemList.add(new tabItem(R.string.encrypt, R.drawable.ic_lock, encryptionFragment));

        Fragment decryptionFragment = DecryptionFragment.newInstance();
        mTabItemList.add(new tabItem(R.string.decrypt, R.drawable.ic_unlock, decryptionFragment));
    }

    private void setUpTabs(){
        for(int i = 0; i < mTabItemList.size(); i++){
            mTabLayout.getTabAt(i).setIcon(mTabItemList.get(i).mIcon);
        }
    }
}
