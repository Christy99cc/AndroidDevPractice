package bjfu.it.zhangsixuan.starbuzz.adapter;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import bjfu.it.zhangsixuan.starbuzz.R;

public class RVCartAdapter extends RecyclerView.Adapter {

    List<Map<String, Object>> list;

    public RVCartAdapter(List<Map<String, Object>> mData) {
        this.list = mData;
    }

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.cart_item_layout, null);
        return new RViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 注意强制转换，一定要是显式的
        ((RViewHolder) holder).iv_cart_img.setImageResource((int) list.get(position).get("stuffImgId"));
        ((RViewHolder) holder).tv_item_name.setText((String) list.get(position).get("stuffName"));
        ((RViewHolder) holder).tv_item_price.setText(String.valueOf(list.get(position).get("stuffPrice")));
        ((RViewHolder) holder).tv_item_number.setText(String.valueOf(list.get(position).get("stuffNumber")));
        // +\- 采用默认即可

        // 点击事件
        // +
        ((RViewHolder) holder).iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("debug", "点击了position 的+号");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_cart_img;  // 图片
        TextView tv_item_name;  // 名字
        TextView tv_item_price; // 价格
        ImageView iv_reduce;
        ImageView iv_add;
        TextView tv_item_number; // 数量

        RViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_cart_img = itemView.findViewById(R.id.iv_cart_item);
            tv_item_name = itemView.findViewById(R.id.tv_cart_item);
            tv_item_price = itemView.findViewById(R.id.tv_cart_item_price);
            iv_reduce = itemView.findViewById(R.id.iv_reduce);
            iv_add = itemView.findViewById(R.id.iv_add);
            tv_item_number = itemView.findViewById(R.id.tv_amount);
        }
    }

}
