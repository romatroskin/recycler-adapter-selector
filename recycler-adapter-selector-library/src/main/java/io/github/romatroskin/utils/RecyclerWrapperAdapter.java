package io.github.romatroskin.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * The basic wrapper for {@link android.support.v7.widget.RecyclerView.Adapter}
 * with useful {@code Callback} interface provided, which can help extend
 * adapters by anonymous callback.
 * @author romatroskin
 * @version 1
 * @since 12/7/16
 */
public class RecyclerWrapperAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    private final RecyclerView.Adapter<VH> wrappedAdapter;

    private Consumer<VH> createFunction;
    private BiConsumer<VH, Integer> bindFunction;
    private RecyclerWrapperAdapter(Builder<VH> builder,
                                   Consumer<VH> create,
                                   BiConsumer<VH, Integer> bind) {
        wrappedAdapter = builder.wrappedAdapter;
        createFunction = create;
        bindFunction = bind;
        wrappedAdapter.registerAdapterDataObserver(new DataObserver());
    }

    @Override
    public int getItemViewType(int position) {
        return wrappedAdapter.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return wrappedAdapter.getItemId(position);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        final VH holder = wrappedAdapter.onCreateViewHolder(parent, viewType);

        if(createFunction != null) {
            try {
                createFunction.accept(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if(bindFunction != null) {
            try {
                bindFunction.accept(holder, position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        wrappedAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return wrappedAdapter.getItemCount();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        wrappedAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        wrappedAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        wrappedAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
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
    public static final class Builder<VH extends RecyclerView.ViewHolder> {
        private final RecyclerView.Adapter<VH> wrappedAdapter;
        public Builder(RecyclerView.Adapter<VH> wrappedAdapter) {
            this.wrappedAdapter = wrappedAdapter;
        }

        private Consumer<VH> onCreatedCallback;
        public Builder<VH> with(Consumer<VH> consumer) {
            onCreatedCallback = consumer;
            return this;
        }

        private BiConsumer<VH, Integer> onBindCallback;
        public Builder<VH> with(BiConsumer<VH, Integer> biConsumer) {
            onBindCallback = biConsumer;
            return this;
        }

        /**
         * Returns a {@code RecyclerWrapperAdapter} built from the parameters previously set.
         *
         * @return a {@code RecyclerWrapperAdapter} built with parameters
         * of this {@code RecyclerWrapperAdapter.Builder}
         */
        RecyclerWrapperAdapter<VH> build() {
            return new RecyclerWrapperAdapter<>(this, onCreatedCallback, onBindCallback);
        }
    }
}
