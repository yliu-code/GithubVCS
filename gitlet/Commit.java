package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Commit implements Serializable {
    private String id;                      // unique id generated by sha-1
    private String parent;                  // id of parent, null if initial commit
    private String logMessage;              // store commit messages
    private String branch;                  // name of branch currently on
    private HashMap<String, String> tree;   // maps filename to blob id
    private String timeStamp;               // time of commit

    public Commit(String logMessage) {
        this.logMessage = logMessage;
        this.parent = null;
        this.tree = new HashMap<>();
        this.timeStamp = currTimeStamp();
        this.id = generateID();
        this.branch = "master";
    }

    public Commit(String logMessage, Commit parent) {
        this.parent = parent.getID();
        this.logMessage = logMessage;
        this.tree = new HashMap<>(parent.tree);
        this.timeStamp = currTimeStamp();
        this.id = generateID();
        this.branch = parent.getBranch();
    }

    private String generateID() {
        return Utils.sha1(this.logMessage, this.timeStamp);
    }

    public String getID() {
        return this.id;
    }

    public HashMap<String, String> getTree() {
        return this.tree;
    }

    public boolean containsBlob(String fileName) {
        return tree.containsKey(fileName);
    }

    public String getBlob(String fileName) {
        return tree.get(fileName);
    }

    public void addBlob(String fileName, Blob blob) {
        tree.put(fileName, blob.getID());
        Persistence.writeBlob(blob.getID(), blob);
    }

    public void rmBlob(String fileName) {
        getTree().remove(fileName);
    }

    public void setParent(String newParent) {
        this.parent = newParent;
    }

    public String getParent() {
        return this.parent;
    }

    public String getLogMessage() {
        return this.logMessage;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranch() {
        return this.branch;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    private String currTimeStamp() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);

    }

}
