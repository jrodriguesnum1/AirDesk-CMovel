package pt.ulisboa.tecnico.cmov.g15.airdesk.network;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.FileDoesNotExistsException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.WorkspaceDoesNotExistException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.Utils;

/**
 * Created by MSC on 02/04/2015.
 */
public class NetworkServiceServer implements NetworkServiceServerI {

    private AirDesk airDesk;

    public NetworkServiceServer() {}

    public NetworkServiceServer(AirDesk airDesk) {
        this.airDesk = airDesk;
    }

    public AirDesk getAirDesk() {
        return airDesk;
    }

    public void setAirDesk(AirDesk airDesk) {
        this.airDesk = airDesk;
    }

    @Override
    public String getEmail() {
        return airDesk.getUser().getEmail();
    }

    @Override
    public boolean notifyIntentionS(String workspaceName, String fileName, FileState intention, boolean force) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspaceName);

        if (ws == null)
            throw new WorkspaceDoesNotExistException(workspaceName);

        AirDeskFile f = ws.getFile(fileName);

        if(force){
            f.setState(intention);
            return true;
        }
        if (f.getState() == FileState.WRITE)
            return false;

        f.setState(intention);
        return true;
    }


    @Override
    public int getFileVersionS(String workspaceName, String fileName) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspaceName);

        if (ws != null) {
            AirDeskFile f = ws.getFile(fileName);
            return f.getVersion();
        }

        return -1;
    }


    @Override
    public FileState getFileStateS(String workspaceName, String fileName) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspaceName);

        if (ws != null) {
            AirDeskFile f = ws.getFile(fileName);
            return f.getState();
        } else {
            throw new WorkspaceDoesNotExistException(workspaceName);
        }
    }


    @Override
    public String getFileS(String workspaceName, String fileName) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspaceName);

        if (ws == null)
            throw new WorkspaceDoesNotExistException(workspaceName);

        AirDeskFile f = ws.getFile(fileName);
        if(f == null)
            throw new FileDoesNotExistsException(fileName);

        return f.readNoNetwork();
    }


    @Override
    public void sendFileS(String workspaceName, String fileName, String fileContent) {
        OwnerWorkspace workspace = airDesk.getOwnerWorkspaceByName(workspaceName);

        AirDeskFile f = workspace.getFile(fileName);
        if(f == null)
            f = workspace.createFileNoNetwork(fileName);

        f.incrementVersion();
        f.writeNoNetwork(fileContent);
    }

    @Override
    public boolean checkTags(List<String> workspaceTags, List<String> userTags) {
        for (String wt : workspaceTags) {
            for (String us : userTags) {
                if (wt.equalsIgnoreCase(us)) return true;
            }
        }
        return false;
    }

    @Override
    public void workspaceRemovedS(String userEmail, String workspaceName) {
        airDesk.workspaceWasRemoved(userEmail, workspaceName);
    }

    @Override
    public void deleteFileS(String workspaceName, String fileName) {
        AirDeskFile f;
        OwnerWorkspace ow = airDesk.getOwnerWorkspaceByName(workspaceName);
        if (ow == null)
            throw new WorkspaceDoesNotExistException(workspaceName);
        ow.deleteFile(fileName, WorkspaceType.OWNER);
    }

    @Override
    public List<String> searchWorkspacesS(String clientEmail, List<String> clientTags) {
        List<String> allowedWorkspacesR = new ArrayList<String>();
        for (OwnerWorkspace workspace : airDesk.getOwnerWorkspaces()) {

            if (workspace.userInAccessList(clientEmail)) {
                if (workspace.userHasPermissions(clientEmail)) {
                    allowedWorkspacesR.add(workspace.getName());
                }
                continue;
            }

            if (workspace.getVisibility() == WorkspaceVisibility.PUBLIC) {
                if (checkTags(workspace.getTags(), clientTags)) {
                    allowedWorkspacesR.add(workspace.getName());
                    workspace.addUserToAccessList(clientEmail);
                    continue;
                }
            }
        }



        return allowedWorkspacesR;
    }

    @Override
    public List<String> getFileList(String workspaceName) {
        OwnerWorkspace ownerWorkspace = airDesk.getOwnerWorkspaceByName(workspaceName);
        List<String> fileNames = new ArrayList<String>();
        for(AirDeskFile file : ownerWorkspace.getFiles()) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    // i don't know if when we add wifi to this the ownerEmail will be needed
    @Override
    public long getWorkspaceQuotaS(String ownerEmail, String workspaceName) {
        OwnerWorkspace ow = airDesk.getOwnerWorkspaceByName(workspaceName);
        if(ow == null)
            throw new WorkspaceDoesNotExistException(workspaceName);
        return ow.getQuota();
    }
    
    @Override
    public void fileRemovedS(String userEmail, String workspaceName, String fileName) {
        airDesk.fileWasDeleted(userEmail, workspaceName, fileName);
    }
}
