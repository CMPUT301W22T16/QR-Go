package com.example.qr_go;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

/**
 * Fragment state adapter for the search activity
 */
public class SearchFragmentStateAdapter extends FragmentStateAdapter {

    /**
     * List of fragments in the search. Will contain a qr search and user search fragment
     */
    private final ArrayList<SortableFragment> fragments = new ArrayList<>();

    /**
     * Updates the sort of a given fragment based on the selected position of the sort spinner
     * @param fragmentPos The index of the search fragment being updated. 0 for qr code search
     *                    1 for user search
     * @param newSortPos Position in spinner of the sorting option
     */
    public void updateSort(Integer fragmentPos, Integer newSortPos) {
        if (fragments.size()-1 < fragmentPos) {
            return;
        }
        fragments.get(fragmentPos).updateSort(newSortPos);
    }

    /**
     * Sets a given fragment as the searchable one
     * @param fragmentPos The index of the search fragment being updated. 0 for qr code search
     *                    1 for user search
     */
    public void setSearch(Integer fragmentPos) {
        if (fragments.size()-1 < fragmentPos) {
            return;
        }
        fragments.get(fragmentPos).setSearchFiltering();
    }

    /**
     * Makes a given fragment retrieve fresh data
     * @param fragmentPos The index of the search fragment being updated. 0 for qr code search
     *                    1 for user search
     */
    public void retrieveData(Integer fragmentPos) {
        if (fragments.size()-1 < fragmentPos) {
            return;
        }
        fragments.get(fragmentPos).retrieveData();
    }

    /**
     * Constructor
     * @param fragmentActivity The FragmentActivity object calling the adapter
     */
    public SearchFragmentStateAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int pos) {
        switch(pos) {
            case 0:
                QRSearchFragment qrFragment = new QRSearchFragment();
                fragments.add(qrFragment);
                qrFragment.retrieveData();
                return qrFragment;
            case 1:
                UserSearchFragment userFragment = new UserSearchFragment();
                fragments.add(userFragment);
                userFragment.retrieveData();
                return userFragment;
            default:
                return new QRSearchFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
