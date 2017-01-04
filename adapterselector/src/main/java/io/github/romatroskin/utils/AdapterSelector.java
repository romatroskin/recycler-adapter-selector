package io.github.romatroskin.utils;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by romatroskin on 12/6/16.
 */

public abstract class AdapterSelector {
    public static RecyclerAdapterSelector.Builder with(RecyclerView recyclerView) {
        return new RecyclerAdapterSelector.Builder(recyclerView);
    }

    public static RecyclerAdapterSelector.Builder with(RecyclerView recyclerView, ActionMode.Callback callback) {
        return new RecyclerAdapterSelector.Builder(recyclerView).withCallback(callback).withMultiselection(true);
    }

    public static RecyclerAdapterSelector.Builder with(RecyclerView recyclerView, PopupMenu popupMenu) {
        return new RecyclerAdapterSelector.Builder(recyclerView).withPopupMenu(popupMenu);
    }

    public static RecyclerAdapterSelector.Builder with(RecyclerView recyclerView, View.OnClickListener onClickListener) {
        return new RecyclerAdapterSelector.Builder(recyclerView).withOnClickListener(onClickListener);
    }
}
