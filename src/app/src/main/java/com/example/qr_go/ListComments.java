package com.example.qr_go;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;


/** list of comments to be stored into database
 *
 */
public class ListComments extends ArrayAdapter {

    private ArrayList<Comment> comments;
    private Context context;

    public ListComments(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_comments_content, parent, false);
        }

        Comment comment = comments.get(position);

//        ImageView commenterPicture = (ImageView) view.findViewById(R.id.commenterPhoto);
        TextView commenterName = (TextView) view.findViewById(R.id.commenterName);
        TextView message = (TextView) view.findViewById(R.id.message);

        // show comment's message
        commenterName.setText(comment.getUsername());
        message.setText(comment.getMessage());

        return view;

    }

    /**
     * add comment to the list
     * @param comment comment to be added
     */
    public void addComment(Comment comment){
        comments.add(comment);
    }

    /**
     * get size of the array
     * @return size of the array
     */
    public int size() {
        return comments.size();
    }
}
