package com.example.qr_go.fragments;

import androidx.fragment.app.Fragment;

/**
 * Abstract class which represents a fragment with a sortable view that needs to be updated
 * according to the selected sorting option on a spinner
 */
public abstract class SortableFragment extends Fragment {

    /**
     * Method for getting the search bar to filter this fragment
     */
    public abstract void setSearchFiltering();

    /**
     * Method for retrieving fresh data from the search activity
     */
    public abstract void retrieveData();

    /**
     * Method for re-sorting a sortable fragment
     * @param sortPos Position in spinner of the sorting option
     */
    public abstract void updateSort(Integer sortPos);

    /**
     * Method for updating views of a sortable fragment
     */
    public abstract void updateView();

    /**
     * Optionally sets the focus to a specific place in the list
     */
    public void setFocus() {
        return;
    }
}
