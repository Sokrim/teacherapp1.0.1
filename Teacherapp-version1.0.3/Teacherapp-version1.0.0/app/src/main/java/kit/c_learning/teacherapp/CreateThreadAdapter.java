package kit.c_learning.teacherapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutionException;

import kit.c_learning.teacherapp.models.ThreadList;

/**
 * Created by sokrim on 3/9/2018.
 */

public class CreateThreadAdapter extends RecyclerView.Adapter<CreateThreadAdapter.MyViewHolder> {
    private Context mContext;
    private List<ThreadList> threadLists;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String fID1, threadNo, threadID;
    int index;
    String url3;

    public CreateThreadAdapter(Context mContext, List<ThreadList> threadList) {
        this.mContext = mContext;
        this.threadLists = threadList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView postOwnerDisplayImageView, more_Option;
        TextView postOwnerUsernameTextView;
        TextView postTimeCreatedTextView;
        ImageView postDisplayImageVIew, postDisplayImageVIew2, postDisplayImageVIew3;
        TextView postTextTextView;
        LinearLayout postLikeLayout;
        LinearLayout postCommentLayout;
        TextView postNumReadsTextView;
        TextView postNumCommentsTextView;
        TextView postBodyTextView;
        CardView threadCardView;

        public MyViewHolder(View view) {
            super(view);
            postOwnerDisplayImageView = (ImageView) view.findViewById(R.id.iv_post_owner_display);
            postOwnerUsernameTextView = (TextView) view.findViewById(R.id.tv_post_username);
            postTimeCreatedTextView = (TextView) view.findViewById(R.id.tv_time);
            postDisplayImageVIew = (ImageView) view.findViewById(R.id.iv_post_display);
            postDisplayImageVIew2 = (ImageView) view.findViewById(R.id.iv_post_display2);
            postDisplayImageVIew3 = (ImageView) view.findViewById(R.id.iv_post_display3);
            postLikeLayout = (LinearLayout) view.findViewById(R.id.like_layout);
            postCommentLayout = (LinearLayout) view.findViewById(R.id.comment_layout);
            postNumReadsTextView = (TextView) view.findViewById(R.id.tv_likes);
            postNumCommentsTextView = (TextView) view.findViewById(R.id.tv_comments);
            postTextTextView = (TextView) view.findViewById(R.id.tv_post_text);
            postBodyTextView = (TextView) view.findViewById(R.id.tv_post_body);
            threadCardView = view.findViewById(R.id.threadCardView);
            threadCardView.setOnCreateContextMenuListener(this);
            more_Option = view.findViewById(R.id.more_option);
            more_Option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index = getAdapterPosition();
                    threadID = Make_Thread.ccID[index];
                    threadNo = Make_Thread.cNo[index];
                    final String[] option = {"Edit", "Delete"};
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.select_dialog_item, option);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Thread Title:\t " + postTextTextView.getText().toString());
                    builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    editThreadDailog(postTextTextView.getText().toString(), postBodyTextView.getText().toString());
                                    //   Toast.makeText(mContext,"Edit was clicked", Toast.LENGTH_SHORT ).show();
                                    break;
                                case 1:
                                    deleteThreadDialog(index);
                                    threadLists.remove(index);
                                    notifyItemRemoved(index);
                                    notifyItemRangeRemoved(index,getItemCount());
                                    Make_Thread.executeAPI();
                                    //  Toast.makeText(mContext,"Delete was clicked", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                    final AlertDialog a = builder.create();
                    a.show();
                }
            });
        }

        @SuppressLint("ResourceType")
        @Override
        public void onCreateContextMenu(final ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            index = getAdapterPosition();
            threadID = Make_Thread.ccID[index];
            threadNo = Make_Thread.cNo[index];
            final String[] option = {"Edit", "Delete"};
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.select_dialog_item, option);
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Thread Title:\t " + postTextTextView.getText().toString());
            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                           editThreadDailog(postTextTextView.getText().toString(), postBodyTextView.getText().toString());
                         //   Toast.makeText(mContext,"Edit was clicked", Toast.LENGTH_SHORT ).show();
                            break;
                        case 1:
                            deleteThreadDialog(index);
                            threadLists.remove(index);
                            break;
                    }
                }
            });
            final AlertDialog a = builder.create();
            a.show();
        }
    }

    private void deleteThreadDialog(int position){
        ArrayMap<String, String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("cc",threadID);
        data.put("cn", threadNo);
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myURL = myHttp.execute("http://kit.c-learning.jp/t/ajax/coop/CoopDelete", "POST").get();
           /* notifyItemChanged(position);
            notifyItemRangeChanged(position,getItemCount());*/
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void editThreadDailog(String title, String body){
        TextView txtView;
        final EditText edtTitle, edtBody;
        final Button edtButton;
        final Dialog ediDailog = new Dialog(mContext);
        ediDailog.setContentView(R.layout.edit_thread_layout);
        txtView = ediDailog.findViewById(R.id.txtclose);
        ediDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ediDailog.show();

        txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ediDailog.dismiss();
            }
        });
        edtTitle = ediDailog.findViewById(R.id.thread_Title_edt);
        edtTitle.setText(title);
        edtBody = ediDailog.findViewById(R.id.thread_Body_edt);
        edtBody.setText(body);
        edtButton = ediDailog.findViewById(R.id.btnfollow);
        edtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editThreadAPI(edtTitle.getText().toString(), edtBody.getText().toString());
                System.out.println("--0000000000000---" + edtTitle.getText().toString());
                System.out.println("----5666---" + edtBody.getText().toString());
                Intent intent = new Intent(mContext, Bulletin_Board_List.class);
                mContext.startActivity(intent);
                ediDailog.dismiss();
            }
        });
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ThreadList thread = threadLists.get(position);
        final int pos = position;

        holder.postOwnerUsernameTextView.setText(thread.getUserName());
        if (Make_Thread.bbAnonymous.equals("0") || Make_Thread.bbAnonymous.equals("1")){
            if (!holder.postOwnerUsernameTextView.getText().toString().equals(MainActivity.ttName)){
                holder.postOwnerUsernameTextView.setText("Anonymous");
            }
            else if (holder.postOwnerUsernameTextView.getText().toString().equals(MainActivity.ttName)) {
                holder.postOwnerUsernameTextView.setText(Make_Thread.ttName[pos]);
            }
        }else if(Make_Thread.bbAnonymous.equals("2")){
            if (holder.postOwnerUsernameTextView.getText().toString().equals("null")){
                holder.postOwnerUsernameTextView.setText(Make_Thread.stName[pos]);
            }
            else if (holder.postOwnerUsernameTextView.getText().toString().equals(Make_Thread.ttName[pos])) {
                holder.postOwnerUsernameTextView.setText(Make_Thread.ttName[pos]);
            }
        }
        if (holder.postOwnerUsernameTextView.getText().toString().equals(MyCourses.name.getText().toString())){
            String url = "https://kit.c-learning.jp/upload/profile/t/id1.png?923977547";
            Picasso.with(mContext).load(url)
                    .into(holder.postOwnerDisplayImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {}
                        @Override
                        public void onError() {}
                    });
        }
        else if (!holder.postOwnerUsernameTextView.getText().toString().equals(MyCourses.name.getText().toString())){
            holder.postOwnerDisplayImageView.setImageResource(R.drawable.facebook_avatar);
        }
        holder.postTextTextView.setText(thread.getThreadTitle());
        holder.postNumCommentsTextView.setText(thread.getNumComments());
        holder.postNumReadsTextView.setText(thread.getNumReads());
        holder.postBodyTextView.setText(thread.getThreadBody());
        holder.postTimeCreatedTextView.setText(thread.getTimeCreated());

        if (Make_Thread.fID1[pos].equals("")){
            holder.postDisplayImageVIew.setVisibility(View.GONE);
        }
        else if(!Make_Thread.fID1[pos].equals("")){
            holder.postDisplayImageVIew.setVisibility(View.VISIBLE);
            if (Make_Thread.fID1[pos].equals(Make_Thread.fID1[pos])){
                url3 = "https://kit.c-learning.jp/getfile/s3file/"+Make_Thread.fID1[pos];
                Picasso.with(mContext).load(url3)
                        .into(holder.postDisplayImageVIew, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {}
                            @Override
                            public void onError() {}
                        });
            }
            holder.postDisplayImageVIew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ImagePopup imagePopup = new ImagePopup(mContext);
                    imagePopup.setBackgroundColor(Color.BLACK);
                    imagePopup.setFullScreen(true);
                    imagePopup.setHideCloseIcon(true);
                    imagePopup.setImageOnClickClose(true);
                    imagePopup.initiatePopupWithPicasso("https://kit.c-learning.jp/getfile/s3file/"+Make_Thread.fID1[pos]);
                    imagePopup.viewPopup();
                }
            });
        }

        if (Make_Thread.fID3[pos].equals("")){
            holder.postDisplayImageVIew3.setVisibility(View.GONE);
        }
        else if(!Make_Thread.fID3[pos].equals("")){
            holder.postDisplayImageVIew3.setVisibility(View.VISIBLE);
            if (Make_Thread.fID3[pos].equals(Make_Thread.fID3[pos])){
                url3 = "https://kit.c-learning.jp/getfile/s3file/"+Make_Thread.fID3[pos];
                Picasso.with(mContext).load(url3)
                        .into(holder.postDisplayImageVIew3, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {}
                            @Override
                            public void onError() {}
                        });
            }
            holder.postDisplayImageVIew3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ImagePopup imagePopup = new ImagePopup(mContext);
                    imagePopup.setBackgroundColor(Color.BLACK);
                    imagePopup.setFullScreen(true);
                    imagePopup.setHideCloseIcon(true);
                    imagePopup.setImageOnClickClose(true);
                    imagePopup.initiatePopupWithPicasso("https://kit.c-learning.jp/getfile/s3file/"+Make_Thread.fID3[pos]);
                    imagePopup.viewPopup();
                }
            });
        }
        if (Make_Thread.fID2[pos].equals("")){
            holder.postDisplayImageVIew2.setVisibility(View.GONE);
        }
        else if(!Make_Thread.fID2[pos].equals("")){
            holder.postDisplayImageVIew2.setVisibility(View.VISIBLE);
            if (Make_Thread.fID2[pos].equals(Make_Thread.fID2[pos])){
                url3 = "https://kit.c-learning.jp/getfile/s3file/"+Make_Thread.fID2[pos];
                Picasso.with(mContext).load(url3)
                        .into(holder.postDisplayImageVIew2, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {}
                            @Override
                            public void onError() {}
                        });
            }
            holder.postDisplayImageVIew2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ImagePopup imagePopup = new ImagePopup(mContext);
                    imagePopup.setBackgroundColor(Color.BLACK);
                    imagePopup.setFullScreen(true);
                    imagePopup.setHideCloseIcon(true);
                    imagePopup.setImageOnClickClose(true);
                    imagePopup.initiatePopupWithPicasso("https://kit.c-learning.jp/getfile/s3file/"+Make_Thread.fID2[pos]);
                    imagePopup.viewPopup();
                }
            });
        }
       /* if (Make_Thread.imgDecodableString.equals("null")){
            System.out.println("Image not null---------------");
            holder.postDisplayImageVIew.setImageBitmap(BitmapFactory.decodeFile(Make_Thread.imgDecodableString));
        }*/
        holder.threadCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)
                        mContext,v.findViewById(R.id.threadCardView),"threadCard");*/
                Intent intent = new Intent(mContext,PostActivity.class);
               /* prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                prefEditor = prefs.edit();*/
                intent.putExtra("stName", Make_Thread.stName[pos]);
                intent.putExtra("ccID", Make_Thread.ccID[pos]);
                intent.putExtra("cNO", Make_Thread.cNo[pos]);
                intent.putExtra("cTitle",Make_Thread.cTitle[pos]);
                intent.putExtra("cText",Make_Thread.cText[pos]);
                intent.putExtra("ttName",Make_Thread.ttName[pos]);
                intent.putExtra("cAlreadyNum",Make_Thread.cAlreadyNum[pos]);
                intent.putExtra("cDate",Make_Thread.cDate[pos]);
                intent.putExtra("imageFile","https://kit.c-learning.jp/getfile/s3file/"+Make_Thread.fID1[pos]);
                intent.putExtra("fID1",Make_Thread.fID1[pos]);
                intent.putExtra("imageFile2","https://kit.c-learning.jp/getfile/s3file/"+Make_Thread.fID2[pos]);
                intent.putExtra("fID2",Make_Thread.fID2[pos]);
                intent.putExtra("imageFile3","https://kit.c-learning.jp/getfile/s3file/"+Make_Thread.fID3[pos]);
                intent.putExtra("fID3",Make_Thread.fID3[pos]);
                intent.putExtra("userProfileComment","https://kit.c-learning.jp/upload/profile/t/id1.png?923977547");
                //  prefEditor.apply();
                mContext.startActivity(intent/*optionsCompat.toBundle()*/);
            }
        });
    }

    private void editThreadAPI(String edtTitle, String edtBoay){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("m","edit");
        data.put("ct","c398223976");
        data.put("cc", threadID);
        data.put("cn",threadNo);
        data.put("c_title",edtTitle);
        data.put("c_text",edtBoay);
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myURL = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/CoopRes","POST").get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    protected void setFilter(List<ThreadList> threadList){
        //threadLists = new ArrayList<>();
        threadList.clear();
        threadList.addAll(threadList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return threadLists.size();
    }

}
