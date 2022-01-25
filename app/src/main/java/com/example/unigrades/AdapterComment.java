package com.example.unigrades;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class AdapterComment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<String> comments = new ArrayList<>();
    private Course currentCourse;
    private AdapterComment.CommentItemClickListener commentItemClickListener;

    public AdapterComment(Activity activity, ArrayList<String> comments, Course currentCourse){
        this.activity = activity;
        this.comments = comments;
        this.currentCourse = currentCourse;
    }

    public AdapterComment setCommentItemClickListener(AdapterComment.CommentItemClickListener commentItemClickListener){
        this.commentItemClickListener = commentItemClickListener;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.list_comment, viewGroup, false);
        return new AdapterComment.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AdapterComment.CommentViewHolder commentViewHolder = (AdapterComment.CommentViewHolder) holder;
        String comment = getItem(position);
        Log.d("comment_list", "position = " + position);

        commentViewHolder.commentText.setText(comment);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    private String getItem(int position) {
        return comments.get(position);
    }

    private Course getCurrentCourse() {
        return this.currentCourse;
    }

    public interface CommentItemClickListener {
        void deleteClicked(String comment);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        public AppCompatImageView deleteButton;
        public MaterialTextView commentText;
        public CommentViewHolder(final View itemView){
            super(itemView);
            this.deleteButton = itemView.findViewById(R.id.listComment_BUTTON_delete);
            this.commentText = itemView.findViewById(R.id.listComment_TEXT_comment);


            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Course currentCourse = getCurrentCourse();
                    currentCourse.getStudentComments().remove(getAdapterPosition());
                    currentCourse.addCourseToDB(); //TODO check if it really deletes it that way in db.
                    comments.remove(getAdapterPosition());

                    //TODO ??? should refresh the activity but we will see if it works
                    activity.finish();
                    activity.startActivity(activity.getIntent());
                }
            });

        }

    }
}
