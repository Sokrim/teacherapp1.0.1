package kit.c_learning.teacherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import kit.c_learning.teacherapp.models.BulletinBoard;

/**
 * Created by sokrim on 2/20/2018.
 */

public class BulletinBoardAdapter extends RecyclerView.Adapter<BulletinBoardAdapter.MyViewHolder> {

    private Context mContext;
    private List<BulletinBoard> bulletinBoardList;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bulletinboard_list, parent, false);
        return new MyViewHolder(itemView);

    }

    public void delete(int position) { //removes the row
        ArrayMap<String, String> header = new ArrayMap<>();
        ArrayMap<String, String> data = new ArrayMap<>();
        data.put("ccID", Bulletin_Board_List.ccID[position]);
       // System.out.println("__________________________________" + Bulletin_Board_List.ccID[position]);
        data.put("ttID","id1");
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myUrl = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/catedelete", "POST").get();
            JSONObject jsonObject = new JSONObject(myUrl);
            String mes = jsonObject.getString("mes");
            if (mes.equals("sucess")){
                bulletinBoardList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
                System.out.println("-------------------------" + getItemCount());
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBindViewHolder(final BulletinBoardAdapter.MyViewHolder holder, int position) {
        final BulletinBoard bb = bulletinBoardList.get(position);
        final int pos = position;
        holder.bbName.setText(bb.getBbName());

        if (Bulletin_Board_List.ccLastDate[pos].equals("0000-00-00 00:00:00")){
            holder.lastPostDate.setText("");
        } else {
            holder.lastPostDate.setText(bb.getLastPostDate());
        }

        if (Bulletin_Board_List.ccTotalSize[pos].equals("null")){
            holder.capacity.setText("0 B");
        } else {
            holder.capacity.setText(bb.getCapacity()+" B");
        }
        if(Bulletin_Board_List.ccCharNum[pos].equals("null")){
            holder.wordCount.setText("0 words");
        }else {
            holder.wordCount.setText(bb.getWordCount()+ " words");
        }

        if(Bulletin_Board_List.ccAnonymous[pos].equals("0")){
            holder.anonymous.setText("Anonymous");
        }else if(Bulletin_Board_List.ccAnonymous[pos].equals("1")){
            holder.anonymous.setText("Registered instructor");
        }else if( Bulletin_Board_List.ccAnonymous[pos].equals("2")){
            holder.anonymous.setText("Registered");
        }
        holder.userComment.setText(bb.getUserComment() + "");

        if( Bulletin_Board_List.ccStuRange[pos].equals("0")){
            holder.applicable.setText("None");
            holder.applicable.setTextColor(Color.parseColor("#000000"));
            holder.cardBorder.setBackgroundResource(R.drawable.bulletinboard_card_view_black);
        }else if(Bulletin_Board_List.ccStuRange[pos].equals("1")){
            holder.applicable.setText("Choice");
            holder.applicable.setTextColor(Color.parseColor("#11ff04"));
            holder.cardBorder.setBackgroundResource(R.drawable.bulletinboard_card_view_green);
        }else if(Bulletin_Board_List.ccStuRange[pos].equals("2")){
            holder.applicable.setText("All");
            holder.applicable.setTextColor(Color.parseColor("#10b9d7"));
            holder.cardBorder.setBackgroundResource(R.drawable.bulletinboard_card_view);
        }

       if (Bulletin_Board_List.ccStuWrite[pos].equals("0")){
           holder.studentPostIcon.setImageResource(R.drawable.ic_action_cross);
       } else if(Bulletin_Board_List.ccStuWrite[pos].equals("1")){
           holder.studentPostIcon.setImageResource(R.drawable.ic_action_circle);
       }

        holder.bbName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bulletin_Board_List.ccBBName[pos].equals(holder.bbName.getText().toString())){
                    Intent intent = new Intent(mContext,Make_Thread.class);
                   /* prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    prefEditor = prefs.edit();*/
                    intent.putExtra("ccName",Bulletin_Board_List.ccBBName[pos]);
                    intent.putExtra("ccID",Bulletin_Board_List.ccID[pos]);
                    intent.putExtra("ccCharNum",Bulletin_Board_List.ccCharNum[pos]);
                    intent.putExtra("ccAnonymous",Bulletin_Board_List.ccAnonymous[pos]);
                    intent.putExtra("ccStuWrite",Bulletin_Board_List.ccStuWrite[pos]);
                   // prefEditor.apply();
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView bbName, lastPostDate, capacity, anonymous, userComment, wordCount, alert, applicable;
        public ImageView studentPostIcon;
        public LinearLayout layout;
        public RelativeLayout cardBorder;

        public MyViewHolder(View view) {
            super(view);
            bbName = view.findViewById(R.id.bulletinBoard_Name);
            lastPostDate = view.findViewById(R.id.lastPostDate);
            capacity = view.findViewById(R.id.capacity);
            anonymous = view.findViewById(R.id.anonymous);
            userComment = view.findViewById(R.id.userComment);
            wordCount = view.findViewById(R.id.wordCount);
            alert = view.findViewById(R.id.alert);
            alert.setVisibility(View.GONE);
            cardBorder = view.findViewById(R.id.cardBorder);
           /* layout = view.findViewById(R.id.edit_boardIcon);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(layout);
                }
            });*/
            applicable = view.findViewById(R.id.applicable_Spinner);
            studentPostIcon = view.findViewById(R.id.studentPost_icon);

        }


        /* Showing popup menu when tapping on 3 dots */
        private void showPopupMenu(View view) {
            // inflate menu
            PopupMenu popup = new PopupMenu(mContext, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_editbulletinboard, popup.getMenu());
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
            popup.show();
        }

        /**
         * Click listener for popup menu items
         */
        class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            public MyMenuItemClickListener() {
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.edit_BulletinBoard:
                        Toast.makeText(mContext, "Edit Bulletin Board Information", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.deleteBulletinBoard:
                        Toast.makeText(mContext, "Delete Bulletin Board", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        }
    }


    public BulletinBoardAdapter(Context mContext, List<BulletinBoard> bulletinBoardList) {
        this.mContext = mContext;
        this.bulletinBoardList = bulletinBoardList;
    }

    @Override
    public int getItemCount() {
        return bulletinBoardList.size();
    }
}
