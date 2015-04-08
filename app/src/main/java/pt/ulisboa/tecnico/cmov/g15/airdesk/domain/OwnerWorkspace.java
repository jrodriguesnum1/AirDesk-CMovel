package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;

/**
 * Created by diogo on 03-04-2015.
 */
public class OwnerWorkspace extends Workspace {
    private List<AccessListItem> accessList;
    private WorkspaceVisibility visibility;
    private List<String> tags;

    public OwnerWorkspace(User owner, String name, long quota) {
        super(owner, name, quota);
        tags = new ArrayList<String>();
        accessList = new ArrayList<AccessListItem>();
    }

    public List<AccessListItem> getAccessList() {
        return accessList;
    }

    public void setAccessList(List<AccessListItem> accessList) {
        this.accessList = accessList;
    }

    public WorkspaceVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(WorkspaceVisibility visibility) {
        this.visibility = visibility;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean setQuota(long newQuota) {
        if (newQuota >= this.workspaceUsage()) {
            this.quota = newQuota;
            return true;
        }
        //TODO network
        return false;
    }

    public boolean addUserToAccessList(AccessListItem item) {
        //TODO network
        return getAccessList().add(item);
    }

    public boolean removeUserFromAccessList(User user){
        AccessListItem itemToRemove = null;
        for(AccessListItem item : getAccessList())
            if(item.getUser().equals(user)) {
                itemToRemove = item;
                break;
            }
        if(itemToRemove == null) return false;
        return getAccessList().remove(itemToRemove);
    }

    public boolean hasPermission(User user) {
        
    }
}
