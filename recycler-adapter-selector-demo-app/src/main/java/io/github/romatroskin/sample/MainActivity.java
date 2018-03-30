package io.github.romatroskin.sample;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.romatroskin.utils.AdapterSelector;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_main);
//        final AdapterBuilder adapterBuilder = AdapterBuilder;
        final RecyclerView.Adapter adapter = new SampleAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        AdapterSelector.with(recyclerView, new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.sample_context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                final MenuItem editItem = menu.findItem(R.id.menu_item_edit);
                final Drawable editDrawable = DrawableCompat.wrap(editItem.getIcon());
                DrawableCompat.setTint(editDrawable, Color.WHITE);
                editItem.setIcon(editDrawable);

                final MenuItem deleteItem = menu.findItem(R.id.menu_item_delete);
                final Drawable deleteDrawable = DrawableCompat.wrap(deleteItem.getIcon());
                DrawableCompat.setTint(deleteDrawable, Color.WHITE);
                deleteItem.setIcon(deleteDrawable);

                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {}
        }).build();
    }

    private static class SampleAdapter extends RecyclerView.Adapter {
        private static final List<String> sampleItems;
        static {
            sampleItems = new ArrayList<>();
            sampleItems.add("Kafka");
            sampleItems.add("left");
            sampleItems.add("his");
            sampleItems.add("work");
            sampleItems.add("both");
            sampleItems.add("published");
            sampleItems.add("and");
            sampleItems.add("unpublished");
            sampleItems.add("to");
            sampleItems.add("his");
            sampleItems.add("friend");
            sampleItems.add("and");
            sampleItems.add("literary");
            sampleItems.add("executor");
            sampleItems.add("Max");
            sampleItems.add("Brod");
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final Context context = parent.getContext();
            final View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.sample_item_view, parent, false);
            return new SampleViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((SampleViewHolder) holder).setText(sampleItems.get(position));
        }

        @Override
        public int getItemCount() {
            return sampleItems.size();
        }

        class SampleViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            SampleViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.sample_text_view);
            }

            public void setText(CharSequence text) {
                textView.setText(text);
            }
        }
    }
}
