package bjfu.it.zhangsixuan.starbuzz.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.Map;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.utils.Utils;



public class Item2Adapter extends BaseAdapter {
    static final class ViewHolder {
        TextView tv_stuffId;
        TextView tv_stuffName;
        TextView tv_add_to_cart;
    }

    private List<Map<String, Object>> mData;
    private LayoutInflater mInflater;
    private FragmentTransaction transaction;
    private Context context;

    public Item2Adapter(Context context, List<Map<String, Object>> mData, FragmentTransaction transaction) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.transaction = transaction;
        this.context =context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {

            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.stuff_list_item_layout, null);

            holder.tv_stuffId = convertView.findViewById(R.id.stuff_id);
            holder.tv_stuffName = convertView.findViewById(R.id.stuff_name);
            holder.tv_add_to_cart = convertView.findViewById(R.id.tv_add_to_cart);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_stuffName.setText((String) mData.get(position).get("stuffName"));
        holder.tv_stuffId.setText(String.valueOf(mData.get(position).get("stuffId")));

        holder.tv_stuffName.setTag(position);
        holder.tv_add_to_cart.setTag(position);


        holder.tv_add_to_cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 加入购物车
                Log.d("debug", "加入购物车" + v.getTag());
                int stuffId = (int) mData.get((Integer) v.getTag()).get("stuffId");
                Utils.addOneToCart(stuffId, context);
            }
        });

        holder.tv_stuffName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入详情页面
                Log.d("debug", "进入详情页面" + v.getTag());
                //开启事务跳转
                int stuffId = (int) mData.get((Integer) v.getTag()).get("stuffId");
                Utils.toDetailFragment(stuffId, transaction);
            }

        });
        return convertView;
    }
}