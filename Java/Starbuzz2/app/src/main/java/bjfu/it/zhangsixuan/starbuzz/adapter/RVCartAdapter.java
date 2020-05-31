package bjfu.it.zhangsixuan.starbuzz.adapter;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.ui.cart.CartFragment;
import bjfu.it.zhangsixuan.starbuzz.utils.Utils;

public class RVCartAdapter extends RecyclerView.Adapter {

    private List<Map<String, Object>> list;
    private Context context;
    private FragmentTransaction transaction;

    public RVCartAdapter(List<Map<String, Object>> mData, Context context, FragmentTransaction transaction) {
        this.list = mData;
        this.context = context;
        this.transaction = transaction;
    }

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载购物车内单个item的布局cart_item_layout
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

        // 设置Tag为position，方便点击事件时获取点击位置
        ((RViewHolder) holder).iv_cart_img.setTag(position);
        ((RViewHolder) holder).tv_item_name.setTag(position);
        ((RViewHolder) holder).iv_add.setTag(position);
        ((RViewHolder) holder).iv_reduce.setTag(position);


        // +\- 采用默认即可

        // 点击事件+， 添加一件商品至购物车
        ((RViewHolder) holder).iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "点击了position" + v.getTag() + "的+号");
                // 获取点击的商品的stuffId
                int stuffId = (int) list.get((Integer) v.getTag()).get("stuffId");
                // 根据stuffId添加至购物车
                Utils.addOneToCart(stuffId, context);
                // Attention: 重新获取并绑定数据，刷新购物车
                list = CartFragment.getData(context);
                onBindViewHolder(holder, (Integer) v.getTag());
                // 刷新购物车的商品总价
                Utils.refreshTotalPrice(((FragmentActivity) context).findViewById(R.id.total_price), list);
            }
        });

        // 点击事件-，从购物车中删去一件商品
        ((RViewHolder) holder).iv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "点击了position" + v.getTag() + "的+号");
                // 获取点击的商品的stuffId
                int stuffId = (int) list.get((Integer) v.getTag()).get("stuffId");
                // 根据stuffId从购物车删去一件商品
                Utils.removeOneFromCart(stuffId, context);
                // Attention: 重新获取并绑定数据，刷新购物车
                list = CartFragment.getData(context);
                onBindViewHolder(holder, (Integer) v.getTag());
                // 刷新购物车的商品总价
                Utils.refreshTotalPrice(((FragmentActivity) context).findViewById(R.id.total_price), list);
            }
        });

        // 在购物车中点击图片或者名称，进入详情页面
        // 点击图片进入details
        ((RViewHolder) holder).iv_cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "点击了position" + v.getTag() + " 的img");
                // 获取点击的商品的stuffId
                int stuffId = (int) list.get((Integer) v.getTag()).get("stuffId");
                // 根据stuffId进入相应的详情页面
                Utils.toDetailFragment(stuffId, transaction);
            }
        });

        // 点击商品名称进入details
        ((RViewHolder) holder).tv_item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "点击了position" + v.getTag() + " 的name");
                int stuffId = (int) list.get((Integer) v.getTag()).get("stuffId");
                Utils.toDetailFragment(stuffId, transaction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static final class RViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_cart_img;      // 图片
        TextView tv_item_name;      // 名字
        TextView tv_item_price;     // 价格
        ImageView iv_reduce;        // 加号
        ImageView iv_add;           // 减号
        TextView tv_item_number;    // 数量

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
