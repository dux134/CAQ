package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by wolfsoft on 10/11/2015.
 */
public class ProfilePagerAdapter_walkthrough extends FragmentStatePagerAdapter {



    public ProfilePagerAdapter_walkthrough(FragmentManager fm) {
        super(fm);

    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment1_walkthrough tab1 = new Fragment1_walkthrough();
                return tab1;

            case 1:
                Fragment1_walkthrough tab6 = new Fragment1_walkthrough();
                return tab6;


            case 2:
                Fragment1_walkthrough tab2 = new Fragment1_walkthrough();
                return tab2;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}