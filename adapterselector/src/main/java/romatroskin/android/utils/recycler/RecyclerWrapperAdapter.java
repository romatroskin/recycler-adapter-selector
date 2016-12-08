package romatroskin.android.utils.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by romatroskin on 12/7/16.
 */

/**
 * The basic wrapper for {@link android.support.v7.widget.RecyclerView.Adapter}
 * with useful {@code Callback} interface provided, which can help extend
 * adapters by anonymous callback.
 */
public class RecyclerWrapperAdapter extends RecyclerView.Adapter {

    public interface Callback {
        /**
         * An {@link android.support.v7.widget.RecyclerView.Adapter} callback extension,
         * which will be fired immediately after wrapped adapter
         * {@link android.support.v7.widget.RecyclerView.ViewHolder} created.
         * @param holder wrapped adapter view holder
         */
        void onViewHolderCreated(RecyclerView.ViewHolder holder);

        /**
         * An {@link android.support.v7.widget.RecyclerView.Adapter} callback extension,
         * which will be fired before the wrapped adapter
         * {@link android.support.v7.widget.RecyclerView.ViewHolder} bound.
         * @param holder wrapped adapter view holder to bind
         * @param position wrapped adapter view holder position
         */
        void onViewHolderBind(RecyclerView.ViewHolder holder, int position);
    }

    final private Callback callback;
    final private RecyclerView.Adapter wrappedAdapter;

    private RecyclerWrapperAdapter(Builder builder) {
        callback = builder.callback;
        wrappedAdapter = builder.wrappedAdapter;

        this.init();
    }

    private void init() {
        this.wrappedAdapter.registerAdapterDataObserver(new DataObserver());
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder holder = wrappedAdapter.onCreateViewHolder(parent, viewType);

        if(callback != null) {
            callback.onViewHolderCreated(holder);
        }

        return holder;
    }

    //TODO: eliminate unchecked call
    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(callback != null) {
            callback.onViewHolderBind(holder, position);
        }

        wrappedAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return wrappedAdapter.getItemCount();
    }

    //TODO: eliminate unchecked call
    @Override
    @SuppressWarnings("unchecked")
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        wrappedAdapter.onViewAttachedToWindow(holder);
    }

    //TODO: eliminate unchecked call
    @Override
    @SuppressWarnings("unchecked")
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        wrappedAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        wrappedAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        wrappedAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            notifyItemRangeChanged(fromPosition, toPosition + itemCount);
        }
    }

    /**
     * {@code RecyclerWrapperAdapter} builder static inner class.
     */
    public static final class Builder {
        private final Callback callback;
        private final RecyclerView.Adapter wrappedAdapter;

        public Builder(RecyclerView.Adapter wrappedAdapter, Callback callback) {
            this.callback = callback;
            this.wrappedAdapter = wrappedAdapter;
        }

        /**
         * Returns a {@code RecyclerWrapperAdapter} built from the parameters previously set.
         *
         * @return a {@code RecyclerWrapperAdapter} built with parameters of this {@code RecyclerWrapperAdapter.Builder}
         */
        public RecyclerWrapperAdapter build() {
            return new RecyclerWrapperAdapter(this);
        }
    }
}
