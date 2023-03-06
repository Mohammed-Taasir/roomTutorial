package com.example.roomnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> notes = new ArrayList<>();
    private onItemClickListener listener;       // this is our made up onitemclicklistener.

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.txtViewTitle.setText(currentNote.getTitle());
        holder.txtViewDescription.setText(currentNote.getDescription());
        holder.txtViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes){             // our own method designed for handling incoming list of notes.
        this.notes = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position){            // our own method for getting the note at a particular position
        return notes.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder{

        private TextView txtViewTitle;
        private TextView txtViewDescription;
        private TextView txtViewPriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            txtViewTitle = itemView.findViewById(R.id.text_view_title);
            txtViewDescription = itemView.findViewById(R.id.text_view_description);
            txtViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {        // setting onclicklistener on a particular card of cardview.
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();                 // get the position of particular card which you click
                    if(listener != null && position != RecyclerView.NO_POSITION){       // check if its valid listener(matlab this.listener = listener hua hai.) and valid position
                        listener.onItemClick(notes.get(position));           // get the reference of that card that you clicked from notes.get(position) and apply listener.onItemClick() to it.
                    }
                }
            });
        }
    }

    public interface onItemClickListener{
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }


}
