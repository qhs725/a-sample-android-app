package edu.utc.vat.util.adapters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Saves selections to be be retrieved from any other class (TestingActivity being one of the most important).
 */
public class listSelections {
    private static String org = null;
    private static String group = null;
    private static JSONObject member = new JSONObject();
    private static JSONObject task = new JSONObject();
    private static String type = "org";
    private static int perm = 0;

    //set type
    public static String getSelectionType() {
        return type;
    }

    public static void setSelectionType(String type) {
        listSelections.type = type;
    }

    //ORG
    public static String getSelectedOrg() {
        return org;
    }

    public static void selectOrg(String org) {
        listSelections.org = org;
    }

    //GROUP
    public static String getSelectedGroup() {
        return group;
    }

    public static int getGroupPerm() {
        return perm;
    }

    public static void selectGroup(String group, int perm) {
        listSelections.group = group;
        listSelections.perm = perm;
    }


    //MEMBER
    public static JSONObject getSelectedMember() {
        return member;
    }

    public static void selectMember(String id, String name) {

        try {
            listSelections.member.put("id", id);
            listSelections.member.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //listSelections.member = member;
    }

    //Task
    public static JSONObject getSelectedTask() {
        return task;
    }

    public static void selectTask(String id, String name, String desc, String type) {
        try {
            listSelections.task.put("id", id);
            listSelections.task.put("name", name);
            listSelections.task.put("description", desc);
            listSelections.task.put("type", type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
