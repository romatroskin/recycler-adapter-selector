package romatroskin.android.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import romatroskin.android.utils.recycler.AdapterSelector;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        final RecyclerView.Adapter adapter = new SampleAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        AdapterSelector.with(recyclerView, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        }).build();

//        AdapterSelector.with(recyclerView, new ActionMode.Callback() {
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                return false;
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//
//            }
//        }).build();
    }

    private static class SampleAdapter extends RecyclerView.Adapter {
        private static final List<String> sampleItems;
        static {
            sampleItems = new ArrayList<>();
            sampleItems.add("Lorem");
            sampleItems.add("ipsum");
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final Context context = parent.getContext();
            final View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);
            return new SampleViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(sampleItems.get(position));
        }

        @Override
        public int getItemCount() {
            return sampleItems.size();
        }

        class SampleViewHolder extends RecyclerView.ViewHolder {
            SampleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
