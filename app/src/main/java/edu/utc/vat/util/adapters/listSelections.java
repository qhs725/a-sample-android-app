package edu.utc.vat.util.adapters;

/**
 * Saves selections to be be retrieved from any other class (TestingActivity being one of the most important).
 *
 */
public class listSelections {
    private static String org = null;
    private static String group = null;
    private static String member = null;
    private static String task = null;
    private static String type = "org";

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
    public static void selectGroup(String group) {
        listSelections.group = group;
    }
    
    //MEMBER
    public static String getSelectedMember() {
        return member;
    }
    public static void selectMember(String member) {
        listSelections.member = member;
    }

    //Task
    public static String getSelectedTask() {
        return task;
    }
    public static void selectTask(String task) {
        listSelections.task = task;
    }
}
