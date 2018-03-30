package io.github.romatroskin.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import io.github.romatroskin.AttrUtils;
import io.github.romatroskin.R;

/**
 * Created by romatroskin on 12/6/16.
 */

final class RecyclerAdapterSelector extends AdapterSelector {
    private PopupMenu popupMenu;
    private ActionMode.Callback callback;
    private SparseBooleanArray selectedItems;
    private View.OnClickListener onClickListener;

    private ActionMode actionMode;
    private boolean multiselection;
    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter;
    private final RecyclerView recyclerView;

    private RecyclerAdapterSelector(Builder builder) {
        popupMenu = builder.popupMenu;
        callback = builder.callback;
        onClickListener = builder.onClickListener;
        multiselection = builder.multiselection;
        recyclerView = builder.recyclerView;

        selectedItems = new SparseBooleanArray();

        this.init();
    }


    private void init() {
        if (recyclerView.getAdapter() == null) {
            throw new IllegalStateException("Adapter needs to be set!");
        }

        adapter = recyclerView.getAdapter();

        final RecyclerWrapperAdapter.Builder<? extends RecyclerView.ViewHolder> wab =
                new RecyclerWrapperAdapter.Builder<>(adapter)
                        .with(holder -> {
                            final Context context = holder.itemView.getContext();
                            final Drawable defaultBackgroundDrawable = holder.itemView.getBackground();

                            @DrawableRes int backgroundResource;
                            if(multiselection) {
                                holder.itemView.setOnLongClickListener(view -> {
                                    final AppCompatActivity activity = (AppCompatActivity) context;
                                    if(actionMode == null) {
                                        actionMode = activity.startSupportActionMode(new CallbackImpl());
                                        toggleMultiSelection(holder.getAdapterPosition());
                                    }

                                    return true;
                                });

                                holder.itemView.setOnClickListener(view -> {
                                    if(actionMode != null) {
                                        toggleMultiSelection(holder.getAdapterPosition());
                                    }
                                });

                                backgroundResource = R.drawable.adapter_selector;

                            } else {
                                holder.itemView.setOnClickListener(onClickListener != null
                                        ? onClickListener : popupMenu != null? (View.OnClickListener) view -> popupMenu.show() : null);

                                backgroundResource = AttrUtils.obtainResId(context,
                                        android.R.attr.selectableItemBackground);
                            }

                            if(holder.itemView instanceof FrameLayout) {
                                final FrameLayout frameLayout = (FrameLayout) holder.itemView;
                                frameLayout.setForeground(ContextCompat.getDrawable(context, backgroundResource));
                            } else {
                                holder.itemView.setBackgroundResource(backgroundResource);
                            }
                        })
                        .with((holder, position) -> holder.itemView.setSelected(
                                multiselection && selectedItems.get(
                                        position, false))
                        );

        this.recyclerView.setAdapter(wab.build());
    }

    private void toggleMultiSelection(int position) {
        if(selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }

        if(actionMode != null) {
            actionMode.setTitle(String.valueOf(selectedItems.size()));

            if(selectedItems.size() < 1) {
                actionMode.finish();
            } else {
                actionMode.invalidate();
            }
        }

        adapter.notifyItemChanged(position);
    }

    public List<Integer> getSelections() {
        final List<Integer> resultList = new ArrayList<>(
                selectedItems.size());
        for(int i = 0; i < selectedItems.size(); i++) {
            resultList.add(selectedItems.keyAt(i));
        }

        return resultList;
    }

    public void clearSelections() {
        for(Integer position : getSelections()) {
            selectedItems.delete(position);
            adapter.notifyItemChanged(position);
        }
    }

    private class CallbackImpl implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return callback.onCreateActionMode(mode, menu);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return callback.onPrepareActionMode(mode, menu);
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            final boolean result = callback.onActionItemClicked(mode, item);

            if(result) {
                actionMode.finish();
            }

            return result;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            callback.onDestroyActionMode(mode);

            clearSelections();
            actionMode = null;
        }
    }

    /**
     * {@code RecyclerAdapterSelector} builder static inner class.
     */
    public static final class Builder {
        private PopupMenu popupMenu;
        private ActionMode.Callback callback;
        private View.OnClickListener onClickListener;
        private boolean multiselection;
        private final RecyclerView recyclerView;

        public Builder(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        /**
         * Sets the {@code popupMenu} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code popupMenu} to set
         * @return a reference to this Builder
         */
        public Builder withPopupMenu(PopupMenu val) {
            popupMenu = val;
            return this;
        }

        /**
         * Sets the {@code callback} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code callback} to set
         * @return a reference to this Builder
         */
        public Builder withCallback(ActionMode.Callback val) {
            callback = val;
            return this;
        }

        /**
         * Sets the {@code onClickListener} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code onClickListener} to set
         * @return a reference to this Builder
         */
        public Builder withOnClickListener(View.OnClickListener val) {
            onClickListener = val;
            return this;
        }

        /**
         * Sets the {@code multiselection} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code multiselection} to set
         * @return a reference to this Builder
         */
        public Builder withMultiselection(boolean val) {
            multiselection = val;
            return this;
        }

        /**
         * Returns a {@code RecyclerAdapterSelector} built from the parameters previously set.
         *
         * @return a {@code RecyclerAdapterSelector} built with parameters of this {@code RecyclerAdapterSelector.Builder}
         */
        public RecyclerAdapterSelector build() {
            return new RecyclerAdapterSelector(this);
        }
    }
}
