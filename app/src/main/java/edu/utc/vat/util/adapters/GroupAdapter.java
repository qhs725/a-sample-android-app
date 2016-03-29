/**
 * Class summary
 * Adapter for displaying and handling onClick functions for CardView items in a RecyclerView.
 * This currently lists all the Organizations, Groups, Group Members, and tasks associated with the logged-in user.
 * The type of item displayed is based on the values in the listSelections class.
 * If the user is not an admin the Group Members part is skipped.
 */

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
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

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

        if (i == TYPE_HEADER) {
            listViewHolder.vTitle.setText(ci.title);
        } else {

            listViewHolder.vTitle.setText(ci.title);
            listViewHolder.vRole.setText(ci.role);
        }
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = null;

        if (i == TYPE_HEADER) {
            itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_header_layout, viewGroup, false);
        } else {
            itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_layout, viewGroup, false);

        }
        return new GroupsViewHolder(itemView, i);
    }


    public static class GroupsViewHolder extends RecyclerView.ViewHolder {

        protected TextView vRole;
        protected TextView vTitle;

        public GroupsViewHolder(View v, int pos) {
            super(v);

            if (pos == TYPE_HEADER) {
                vTitle = (TextView) v.findViewById(R.id.header_text);
            } else {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listItemInfo ci = itemList.get(getAdapterPosition());
                        Intent intent = new Intent(v.getContext(), GroupListActivity.class);

                        switch (listSelections.getSelectionType()) {
                            case "org":
                                listSelections.setSelectionType("group");
                                listSelections.selectOrg(ci.id);
                                break;
                            case "group":
                                if (ci.test_perm == 1) {
                                    listSelections.setSelectionType("member");
                                    listSelections.selectGroup(ci.id, ci.test_perm);
                                    break;
                                } //else: use next case
                            case "member":
                                listSelections.setSelectionType("task");
                                listSelections.selectMember(ci.id, ci.title);
                                break;
                            case "task":
                                listSelections.selectTask(ci.id, ci.title, ci.description, ci.type);
                                intent = new Intent(v.getContext(), TestingActivity.class);
                                intent.putExtra("taskTitle", ci.title);
                                break;
                        }
                        v.getContext().startActivity(intent);
                    }
                });

                vTitle = (TextView) v.findViewById(R.id.title);
                vRole = (TextView) v.findViewById(R.id.role_name);
            }
        }
    }

    //    need to override this method
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
