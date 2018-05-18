package kit.c_learning.teacherapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import kit.c_learning.teacherapp.models.BulletinBoard;

/**
 * Created by sokrim on 2/20/2018.
 */

public class BulletinBoardAdapter_test extends RecyclerSwipeAdapter<BulletinBoardAdapter_test.MyViewHolder> {

    private Context mContext;
    private List<BulletinBoard> bulletinBoardList;
    Bulletin_Board_List refresh;


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_bb, parent, false);
        return new MyViewHolder(itemView);

    }

    public void delete(int position) { //removes the row
        ArrayMap<String, String> header = new ArrayMap<>();
        ArrayMap<String, String> data = new ArrayMap<>();
        data.put("ccID", Bulletin_Board_List.ccID[position]);
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
                mItemManger.closeAllItems();
                Bulletin_Board_List.executeAPI();
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(final BulletinBoardAdapter_test.MyViewHolder holder, int position) {
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
            holder.applicable.setText("Choice" + "(" +Bulletin_Board_List.ccStuNum[pos] + ")");
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
                    intent.putExtra("ccName",Bulletin_Board_List.ccBBName[pos]);
                    intent.putExtra("ccID",Bulletin_Board_List.ccID[pos]);
                    intent.putExtra("ccCharNum",Bulletin_Board_List.ccCharNum[pos]);
                    intent.putExtra("ccAnonymous",Bulletin_Board_List.ccAnonymous[pos]);
                    intent.putExtra("ccStuWrite",Bulletin_Board_List.ccStuWrite[pos]);
                    mContext.startActivity(intent);
                }
            }
        });

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        // Drag From Right
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.item_list_bottom_wrapper));
        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {}
            @Override
            public void onOpen(SwipeLayout layout) {}
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
                delete(position);
            }
        });
        holder.tvEdit.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, Create_BulletinBoard.class);
            intent.putExtra("bbName",holder.bbName.getText().toString());
            intent.putExtra("bbAnonymous", Bulletin_Board_List.ccAnonymous[pos]);
            intent.putExtra("bbStuRange", Bulletin_Board_List.ccStuRange[pos]);
            intent.putExtra("bbStuWrite", Bulletin_Board_List.ccStuWrite[pos]);
            intent.putExtra("bbID", Bulletin_Board_List.ccID[pos]);
            mContext.startActivity(intent);
        });
        // mItemManger is member in RecyclerSwipeAdapter Class
         mItemManger.bindView(holder.itemView, position);
    }

    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView bbName, lastPostDate, capacity, anonymous, userComment, wordCount, alert, applicable;
        public ImageView studentPostIcon;
        public LinearLayout layout;
        public RelativeLayout cardBorder;
        SwipeLayout swipeLayout;
        LinearLayout tvDelete, tvEdit;

        public MyViewHolder(View view) {
            super(view);
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
            tvDelete = (LinearLayout) itemView.findViewById(R.id.swipeLinearLayout_delete);
            tvEdit = itemView.findViewById(R.id.swipeLinearLayout_edit);
            bbName = view.findViewById(R.id.bulletinBoard_Name);
            lastPostDate = view.findViewById(R.id.lastPostDate);
            capacity = view.findViewById(R.id.capacity);
            anonymous = view.findViewById(R.id.anonymous);
            userComment = view.findViewById(R.id.userComment);
            wordCount = view.findViewById(R.id.wordCount);
            alert = view.findViewById(R.id.alert);
            alert.setVisibility(View.GONE);
            cardBorder = view.findViewById(R.id.cardBorder);
            applicable = view.findViewById(R.id.applicable_Spinner);
            studentPostIcon = view.findViewById(R.id.studentPost_icon);
        }
      /*  *//* Showing popup menu when tapping on 3 dots *//*
        private void showPopupMenu(View view) {
            // inflate menu
            PopupMenu popup = new PopupMenu(mContext, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_editbulletinboard, popup.getMenu());
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
            popup.show();
        }
        *//**
         * Click listener for popup menu items
         *//*
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
        }*/
    }

    public BulletinBoardAdapter_test(Context mContext, List<BulletinBoard> bulletinBoardList) {
        this.mContext = mContext;
        this.bulletinBoardList = bulletinBoardList;
    }

    @Override
    public int getItemCount() {
        return bulletinBoardList.size();
    }

}
