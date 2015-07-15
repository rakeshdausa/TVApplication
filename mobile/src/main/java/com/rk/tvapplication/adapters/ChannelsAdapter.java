/**
 * MyTasksAdapter.java
 * Created Date :: 12/06/2015
 * @Author :: Rakesh
 * Populates data on MyTasks list and handel
 * OnItem click events to show task details.
 */
package com.rk.tvapplication.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rk.tvapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChannelsAdapter extends RecyclerView.Adapter
        <ChannelsAdapter.ListItemViewHolder> {
    private static final String TAG = ChannelsAdapter.class.getName();

    private List<JSONObject> items;
    private SparseBooleanArray selectedItems;

    public ChannelsAdapter(List<JSONObject> modelData) {
        if (modelData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        items = modelData;
        selectedItems = new SparseBooleanArray();
    }

    /**
     * Adds and item into the underlying data set
     * at the position passed into the method.
     *
     * @param newModelData The item to add to the data set.
     * @param position The index of the item to remove.
     */
    public void addData(JSONObject newModelData, int position) {
        items.add(position, newModelData);
        notifyItemInserted(position);
    }

    /**
     * Adds an List into the underlying data .
     *
     * @param nextList The item to add to the data set.
     */
    public void addAll(List<JSONObject> nextList) {
        items.addAll(nextList);
        notifyDataSetChanged();
    }

    /**
     * Removes the item that currently is at the passed in position from the
     * underlying data set.
     *
     * @param position The index of the item to remove.
     */
    public void removeData(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public JSONObject getItem(int position) {
        return items.get(position);
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.channel_list_item, viewGroup, false);
        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        JSONObject model = items.get(position);
        try {
            Picasso.with(viewHolder.imageView.getContext()).load(model.getString("logo")).into(viewHolder.imageView);
//            viewHolder.imageView.setImageResource(R.drawable.my_tasks);
            viewHolder.name.setText(model.getString("name"));
            /*viewHolder.progress.setText(String.format("%d%%\nComplete", model.getInt("percentageCompleted")));
            JSONObject assignedTo = model.getJSONObject("assignedTo");
            viewHolder.description.setText(String.format("Assigned to %s to be",assignedTo.getString("firstName")));

            String dateStr = model.getString("dueDate");
            SimpleDateFormat ft =
                    new SimpleDateFormat ("yyyy-MM-dd");
            Date dueDate = ft.parse(dateStr);
            dateStr = DateUtils.formatDateTime(
                viewHolder.name.getContext(), dueDate.getTime(),
                DateUtils.FORMAT_SHOW_DATE);
            viewHolder.dateTime.setText(String.format("completed by %s",dateStr));
            viewHolder.itemView.setActivated(selectedItems.get(position, false));*/
        }catch (JSONException e){
            Log.e(TAG,e.getMessage());
        } /*catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }*/
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView description;
        TextView dateTime;
        TextView name;
        TextView progress;
        ImageView imageView;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.image_icon);
            itemView.findViewById(R.id.section_ripple).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            /*Intent updateIntent = new Intent(view.getContext(), UpdateTaskActivity.class);
            updateIntent.putExtra(AppConstants.TASK_DATA,items.get(getPosition()).toString());
            view.getContext().startActivity(updateIntent);
*/
        }
    }
}
