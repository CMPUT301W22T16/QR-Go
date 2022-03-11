package com.example.qr_go;

import androidx.fragment.app.Fragment;

/**
 * Abstract class which represents a fragment with a sortable view that needs to be updated
 * according to the selected sorting option on a spinner
 */
abstract class SortableFragment extends Fragment {

    /**
     * Abstract method for updating the views of a sortable fragment
     * @param sortPos Position in spinner of the sorting option
     */
    public abstract void updateSort(Integer sortPos);
}
