package bjfu.it.zhangsixuan.starbuzz.adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.Map;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.utils.Utils;


public class ItemAdapter extends BaseAdapter {

    public static final class ItemViewHolder {
        ImageView iv_item;
        TextView tv_item;
        TextView tv_item_id;
    }


    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData;
    private FragmentTransaction transaction;


    public ItemAdapter(Context context, List<Map<String, Object>> mData,
                       FragmentTransaction transaction) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.transaction = transaction;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemViewHolder holder = null;
        if (convertView == null) {

            holder = new ItemViewHolder();

            convertView = mInflater.inflate(R.layout.item_layout, null);

            holder.iv_item = convertView.findViewById(R.id.iv_item);
            holder.tv_item = convertView.findViewById(R.id.tv_item);
            holder.tv_item_id = convertView.findViewById(R.id.tv_item_id);
            convertView.setTag(holder);

        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }


        holder.iv_item.setImageResource((int) mData.get(position).get("favorite_image"));
        holder.tv_item.setText((String) mData.get(position).get("favorite_name"));

        holder.iv_item.setTag(position);
        holder.tv_item.setTag(position);


        holder.tv_item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("debug", "进入详情页面" + v.getTag());
                int stuffId = (int) mData.get((Integer) v.getTag()).get("favorite_id");
                //TODO 进入详情
                Utils.toDetailFragment(stuffId, transaction);

            }
        });

        holder.iv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入详情页面
                Log.d("debug", "进入详情页面" + v.getTag());
                int stuffId = (int) mData.get((Integer) v.getTag()).get("favorite_id");

                //开启事务跳转
                Utils.toDetailFragment(stuffId, transaction);
            }

        });
        return convertView;
    }
}

