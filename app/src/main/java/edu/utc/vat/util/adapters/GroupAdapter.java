
package edu.utc.vat.util.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.utc.vat.GroupListActivity;
import edu.utc.vat.R;
import edu.utc.vat.TestingActivity;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupsViewHolder> {

    private static List<edu.utc.vat.util.adapters.listItemInfo> itemList;

    public GroupAdapter(List<edu.utc.vat.util.adapters.listItemInfo> itemList) {
        this.itemList = itemList;
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(GroupsViewHolder listViewHolder, int i) {
        listItemInfo ci = itemList.get(i);

        listViewHolder.vTitle.setText(ci.title);
        listViewHolder.vRole.setText(ci.role);
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_layout, viewGroup, false);

            return new GroupsViewHolder(itemView);
    }

    public static class GroupsViewHolder extends RecyclerView.ViewHolder {

        protected TextView vRole;
        protected TextView vTitle;

        public GroupsViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   listItemInfo ci = itemList.get(getAdapterPosition());
                    Intent intent = new Intent(v.getContext(), GroupListActivity.class);
                   // intent.putExtra("id",ci.id);

                    switch(listSelections.getSelectionType()){
                        case "org":
                            listSelections.setSelectionType("group");
                            listSelections.selectOrg(ci.id);
                        //intent.putExtra("type", "groups");
                            break;
                        case "group":
                            listSelections.setSelectionType("member");
                            listSelections.selectGroup(ci.id);
                           // intent.putExtra("type", "members");
                            break;
                        case "member":
                            listSelections.setSelectionType("task");
                            listSelections.selectMember(ci.id);
                            //intent.putExtra("type", "tasks");
                            break;
                        case "task":
                            listSelections.selectTask(ci.id);
                            intent = new Intent(v.getContext(), TestingActivity.class);
                            intent.putExtra("taskTitle", ci.title);
                            break;
                    }
                    v.getContext().startActivity(intent);
                }
            });

            vTitle = (TextView) v.findViewById(R.id.title);
            vRole= (TextView) v.findViewById(R.id.role_name);
        }
    }


}
