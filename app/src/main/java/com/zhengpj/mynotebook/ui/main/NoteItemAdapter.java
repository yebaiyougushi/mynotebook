package com.zhengpj.mynotebook.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengpj.mynotebook.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteItemAdapter extends RecyclerView.Adapter<NoteItemAdapter.ViewHolder> implements ItemTouchHelperListener {
    private List<String> notes;
    private Context context;

    public void setNotes(List<String> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.textView.setText(notes.get(position));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "已完成",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes == null ? 0 : notes.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        String fromNote = notes.remove(fromPosition);
        notes.add(toPosition > fromPosition ? toPosition-1 : toPosition, fromNote);
        notifyItemMoved(fromPosition, toPosition);
    }

     public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.note_text);
            button = itemView.findViewById(R.id.button_finish);
        }
    }
}
