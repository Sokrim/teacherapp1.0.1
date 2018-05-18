package kit.c_learning.teacherapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutionException;

import kit.c_learning.teacherapp.models.ThreadComment;

/**
 * Created by sokrim on 3/16/2018.
 */

public class ThreadCommentAdapter extends RecyclerSwipeAdapter<ThreadCommentAdapter.MyViewHolder> {

    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;

    private Context mContext;
    private List<ThreadComment> threadComments;
    static int index;
    static int position;
    String threadID, threadNo;
    PostActivity postActivity;

    public ThreadCommentAdapter(Context mContext, List<ThreadComment> threadComments) {
        this.mContext = mContext;
        this.threadComments = threadComments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment,parent,false);
            return new ViewHolderOne(itemView);
        } else if (viewType == TYPE_TWO) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reply, parent, false);
            return new ViewHolderTwo(itemView);
        } else {
            throw new RuntimeException("The type has to be ONE or TWO");
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_ONE:
                initLayoutOne((ViewHolderOne) holder, position);
                break;
            case TYPE_TWO:
                initLayoutTwo((ViewHolderTwo) holder, position);
                break;
            default:
                break;
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView commentOwnerDisplay;
        TextView usernameTextView, timeTextView, commentTextView, replyButton;
        CardView replyCardView;
        public MyViewHolder(View view) {
            super(view);
           /* commentOwnerDisplay = (ImageView) view.findViewById(R.id.iv_comment_owner_display);
            usernameTextView = (TextView) view.findViewById(R.id.tv_username);
            timeTextView = (TextView) view.findViewById(R.id.tv_time);
            commentTextView = (TextView) view.findViewById(R.id.tv_comment);
           // replyCardView = view.findViewById(R.id.replyCardView);
            replyButton = view.findViewById(R.id.tv_reply);
            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   replyCardView.setVisibility(View.VISIBLE);
                    Intent i = new Intent(mContext,Reply_Thread.class);
                    mContext.startActivity(i);
                }
            });*/
        }
    }

    @Override
    public int getItemCount() {
        return threadComments.size();
    }
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_reply;
    }

    // determine which layout to use for the row
    @Override
    public int getItemViewType(int position) {
        final ThreadComment itemType = threadComments.get(position);
        if (itemType.getType() == ThreadComment.ItemType.ONE_ITEM) {
            return TYPE_ONE;
        } else if (itemType.getType() == ThreadComment.ItemType.TWO_ITEM) {
            return TYPE_TWO;
        } else {
            return -1;
        }
    }

    private class ViewHolderTwo extends MyViewHolder {
        ImageView commentOwnerDisplay, commentDisplayImageVIew, commentDisplayImageVIew2, commentDisplayImageVIew3;
        TextView usernameTextView, timeTextView, commentTextView;
        SwipeLayout swipeLayout;
        LinearLayout tvDelete, tvEdit, replyLayout;

        public ViewHolderTwo(View view) {
            super(view);
            replyLayout = view.findViewById(R.id.linearLayout_reply);
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_reply);
            tvDelete = (LinearLayout) view.findViewById(R.id.swipeLinearLayout_delete);
            tvEdit = view.findViewById(R.id.swipeLinearLayout_edit);
            commentOwnerDisplay = (ImageView) view.findViewById(R.id.iv_reply_owner_display);
            usernameTextView = (TextView) view.findViewById(R.id.tv_reply_username);
            timeTextView = (TextView) view.findViewById(R.id.tv_reply_time);
            commentTextView = (TextView) view.findViewById(R.id.tv_Reply);
            commentDisplayImageVIew = view.findViewById(R.id.iv_comment_display);
            commentDisplayImageVIew2 = view.findViewById(R.id.iv_comment_display2);
            commentDisplayImageVIew3 = view.findViewById(R.id.iv_comment_display3);
            PostActivity.comment.setHint("Write a comment...");
        }
    }

    private void initLayoutTwo(final ViewHolderTwo holder, final int pos) {
        final ThreadComment comment = threadComments.get(pos);
        holder.replyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReplyOption(pos);
            }
        });
        holder.usernameTextView.setText(comment.getCommentUserName());
        if (holder.usernameTextView.getText().toString().equals("null")){
            holder.usernameTextView.setText(PostActivity.stName[pos]);
        }
        holder.commentTextView.setText(comment.getUserComment());
        if(PostActivity.fID1Reply[pos].equals("")){
            holder.commentDisplayImageVIew.setVisibility(View.GONE);
        }
        else if(!PostActivity.fID1Reply[pos].equals("")){
            holder.commentDisplayImageVIew.setVisibility(View.VISIBLE);
            if (PostActivity.fID1Reply[pos].equals(PostActivity.fID1Reply[pos])){
                String url3 = "https://kit.c-learning.jp/getfile/s3file/"+PostActivity.fID1Reply[pos];
                Picasso.with(mContext).load(url3)
                        .into(holder.commentDisplayImageVIew, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {}
                            @Override
                            public void onError() {}
                        });
            }
        }
        holder.timeTextView.setText(comment.getCommentTime());
        if (holder.usernameTextView.getText().toString().equals(MyCourses.name.getText().toString())){
            String url = "https://kit.c-learning.jp/upload/profile/t/id1.png?923977547";
            Picasso.with(mContext).load(url)
                    .into(holder.commentOwnerDisplay, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }
        else if (!holder.usernameTextView.getText().toString().equals(MyCourses.name.getText().toString())){
            holder.commentOwnerDisplay.setImageResource(R.drawable.facebook_avatar);
        }

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        // Drag From Right
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.item_list_bottom_wrapper));
        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {}
            @Override
            public void onOpen(SwipeLayout layout) {
                System.out.println("Body Reply" + holder.commentTextView.getText().toString());
                threadID = PostActivity.ccID[pos];
                threadNo = PostActivity.cIDReply[pos];
                System.out.println("CID Reply" + PostActivity.cIDReply[pos]);
            }
            @Override
            public void onStartClose(SwipeLayout layout) {}
            @Override
            public void onClose(SwipeLayout layout) {}
            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {}
            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteComment(pos);
                threadComments.remove(pos);
            }
        });
        mItemManger.bindView(holder.itemView, pos);
    }

    private class ViewHolderOne extends MyViewHolder {
        ImageView commentOwnerDisplay, commentDisplayImageVIew, commentDisplayImageVIew2, commentDisplayImageVIew3;
        TextView usernameTextView, timeTextView, commentTextView, replyButton;
        SwipeLayout swipeLayout;
        LinearLayout tvDelete, tvEdit, commentLayout;
        public ViewHolderOne(View view) {
            super(view);
            commentLayout = view.findViewById(R.id.linearLayout_comment);
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_reply);
            tvDelete = (LinearLayout) view.findViewById(R.id.swipeLinearLayout_delete);
            tvEdit = view.findViewById(R.id.swipeLinearLayout_edit);
            commentOwnerDisplay = (ImageView) view.findViewById(R.id.iv_comment_owner_display);
            usernameTextView = (TextView) view.findViewById(R.id.tv_username);
            timeTextView = (TextView) view.findViewById(R.id.tv_time);
            commentTextView = (TextView) view.findViewById(R.id.tv_comment);
            replyButton = view.findViewById(R.id.tv_reply);
            commentDisplayImageVIew = view.findViewById(R.id.iv_comment_display);
            commentDisplayImageVIew2 = view.findViewById(R.id.iv_comment_display2);
            commentDisplayImageVIew3 = view.findViewById(R.id.iv_comment_display3);
        }
    }

    private void initLayoutOne(final ViewHolderOne holder, final int pos) {
        final ThreadComment comment = threadComments.get(pos);
        holder.commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // dialogCommentOption(pos);
                holder.commentTextView.setClickable(true);
                holder.commentTextView.setCursorVisible(true);
                holder.commentTextView.setFocusableInTouchMode(true);
                holder.commentTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                holder.commentTextView.requestFocus(); //to trigger the soft input
              //  Toast.makeText(mContext,"Comment was clicked", Toast.LENGTH_SHORT ).show();
            }
        });

        holder.usernameTextView.setText(comment.getCommentUserName());
        if (holder.usernameTextView.getText().toString().equals("null")){
            holder.usernameTextView.setText(PostActivity.stName[pos]);
        }
        if(PostActivity.fID1Comment[pos].equals("")){
            holder.commentDisplayImageVIew.setVisibility(View.GONE);
        }
        else if(!PostActivity.fID1Comment[pos].equals("")) {
            holder.commentDisplayImageVIew.setVisibility(View.VISIBLE);
            if (PostActivity.fID1Comment[pos].equals(PostActivity.fID1Comment[pos])) {
                String url3 = "https://kit.c-learning.jp/getfile/s3file/" + PostActivity.fID1Comment[pos];
                Picasso.with(mContext).load(url3)
                        .into(holder.commentDisplayImageVIew, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                            }
                        });
            }
        }
        holder.commentTextView.setText(comment.getUserComment());
        holder.timeTextView.setText(comment.getCommentTime());
        holder.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostActivity.comment.setHint("Write a reply...");
                index = holder.getAdapterPosition();
               // postActivity.replyThreadAPI(PostActivity.cIDComment[index]);
               // System.out.println("------index------" + index +"\t" + PostActivity.textComment[index] + "\t"+ PostActivity.cIDComment[index]);
            }
        });

        if (holder.usernameTextView.getText().toString().equals(MyCourses.name.getText().toString())){
            String url = "https://kit.c-learning.jp/upload/profile/t/id1.png?923977547";
            Picasso.with(mContext).load(url)
                    .into(holder.commentOwnerDisplay, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }
        else if (!holder.usernameTextView.getText().toString().equals(MyCourses.name.getText().toString())){
            holder.commentOwnerDisplay.setImageResource(R.drawable.facebook_avatar);
        }
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        // Drag From Right
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.item_list_bottom_wrapper));
        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {}
            @Override
            public void onOpen(SwipeLayout layout) {
                System.out.println(holder.commentTextView.getText().toString()+"Comment ID" + PostActivity.cIDComment[pos] + PostActivity.ccID[pos]);
                threadID = PostActivity.ccID[pos];
                threadNo = PostActivity.cIDComment[pos];
            }
            @Override
            public void onStartClose(SwipeLayout layout) {}
            @Override
            public void onClose(SwipeLayout layout) {}
            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {}
            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 deleteComment(pos);
                 threadComments.remove(pos);
            }
        });
        mItemManger.bindView(holder.itemView, pos);
    }
    private void deleteComment(int position){
        ArrayMap<String, String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("cc",threadID);
        System.out.println("Thread ID " + "\t" + threadID);
        data.put("cn", threadNo);
        System.out.println("Thread No " + "\t" + threadNo);
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myURL = myHttp.execute("http://kit.c-learning.jp/t/ajax/coop/CoopDelete", "POST").get();
            notifyItemRemoved(position);
            notifyItemRangeRemoved(position,getItemCount());
            mItemManger.closeAllItems();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void dialogCommentOption(int pos){
        final String[] option = {"Edit", "Delete"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.select_dialog_item, option);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        threadID = PostActivity.ccID[pos];
                        threadNo = PostActivity.cIDReply[pos];
                        System.out.println("CID Reply(((((((" + PostActivity.cIDReply[pos]);
                        editComment();
                        //   Toast.makeText(mContext,"Edit was clicked", Toast.LENGTH_SHORT ).show();
                        break;
                    case 1:
                        threadID = PostActivity.ccID[pos];
                        threadNo = PostActivity.cIDComment[pos];
                        deleteComment(pos);
                        threadComments.remove(pos);
                        //  Toast.makeText(mContext,"Delete was clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        final AlertDialog a = builder.create();
        a.show();
    }

    private void deleteReply(int position){
        ArrayMap<String, String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("cc",threadID);
        System.out.println("Thread ID " + "\t" + threadID);
        data.put("cn", threadNo);
        System.out.println("Thread No " + "\t" + threadNo);
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myURL = myHttp.execute("http://kit.c-learning.jp/t/ajax/coop/CoopDelete", "POST").get();
            notifyItemRemoved(position);
            notifyItemRangeRemoved(position,getItemCount());
            mItemManger.closeAllItems();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void dialogReplyOption(int pos){
        final String[] option = {"Edit", "Delete"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.select_dialog_item, option);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        threadID = PostActivity.ccID[pos];
                        threadNo = PostActivity.cIDReply[pos];
                        //   Toast.makeText(mContext,"Edit was clicked", Toast.LENGTH_SHORT ).show();
                        break;
                    case 1:
                        threadID = PostActivity.ccID[pos];
                        threadNo = PostActivity.cIDReply[pos];
                        deleteReply(pos);
                        threadComments.remove(pos);
                        //  Toast.makeText(mContext,"Delete was clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        final AlertDialog a = builder.create();
        a.show();
    }

    private void editComment(){
        final Dialog ediDailog = new Dialog(mContext);
        ediDailog.setContentView(R.layout.row_comment);
     //   ediDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ediDailog.show();
    }
}
