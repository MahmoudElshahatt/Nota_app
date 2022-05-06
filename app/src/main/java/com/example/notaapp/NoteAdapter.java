package com.example.notaapp;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private ArrayList<Note> notes;
    private Activity activity;

    public NoteAdapter(ArrayList<Note> notes, Activity activity) {
        this.notes = notes;
        this.activity = activity;
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        holder.titleTV.setText(notes.get(position).getTitle());
        holder.descTV.setText(notes.get(position).getDescription());
        holder.cardView.setOnClickListener(v -> {
            Intent intent=new Intent(activity,NoteDetailsActivity.class);
            intent.putExtra("id",notes.get(position).getId());
            intent.putExtra("title",notes.get(position).getTitle());
            intent.putExtra("description",notes.get(position).getDescription());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTV, descTV;
        private final CardView cardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.tv_title);
            descTV = itemView.findViewById(R.id.tv_description);
            cardView = itemView.findViewById(R.id.list_itemCV);
        }
    }
}
